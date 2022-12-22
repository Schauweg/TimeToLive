package schauweg.timetolive;

import com.google.common.collect.Iterables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import schauweg.timetolive.config.TTLConfigManger;
import schauweg.timetolive.mixin.CreeperEntityMixin;

import java.util.stream.StreamSupport;

public class CountdownRenderer {

    public static void render(MatrixStack matrices, float partialTicks, Camera camera, Matrix4f projection, Frustum capturedFrustum) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if(mc.world == null || !MinecraftClient.isHudEnabled()) {
            return;
        }
        final Entity cameraEntity = camera.getFocusedEntity() != null ? camera.getFocusedEntity() : mc.player; //possible fix for optifine (see https://github.com/UpcraftLP/Orderly/issues/3)
        assert cameraEntity != null : "Camera Entity must not be null!";

        Vec3d cameraPos = camera.getPos();
        final Frustum frustum;
        if(capturedFrustum != null) {
            frustum = capturedFrustum;
        }
        else {
            frustum = new Frustum(matrices.peek().getPositionMatrix(), projection);
            frustum.setPosition(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
        }

        StreamSupport.stream(mc.world.getEntities().spliterator(), false).filter(entity -> entity instanceof CreeperEntity && entity != cameraEntity && entity.isAlive() && Iterables.isEmpty(entity.getPassengersDeep()) && entity.shouldRender(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ()) && (entity.ignoreCameraFrustum || frustum.isVisible(entity.getBoundingBox()))).map(CreeperEntity.class::cast).forEach(entity -> {

            if (entity.isIgnited()){
                int fuse = ((CreeperEntityMixin)entity).getFuseTime() - ((CreeperEntityMixin)entity).getCurrentFuseTime();
                renderCountdown(entity, matrices, partialTicks, camera, cameraEntity, fuse);
            }

        });

        StreamSupport.stream(mc.world.getEntities().spliterator(), false).filter(entity -> entity instanceof TntEntity && entity != cameraEntity && entity.isAlive() && Iterables.isEmpty(entity.getPassengersDeep()) && entity.shouldRender(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ()) && (entity.ignoreCameraFrustum || frustum.isVisible(entity.getBoundingBox()))).map(TntEntity.class::cast).forEach(entity -> renderCountdown(entity, matrices, partialTicks, camera, cameraEntity, entity.getFuse()));
    }


    @SuppressWarnings( "deprecation" )
    private static void renderCountdown(Entity passedEntity, MatrixStack matrices, float partialTicks, Camera camera, Entity viewPoint, int fuse){
        MinecraftClient mc = MinecraftClient.getInstance();

        matrices.push();
        double x = passedEntity.prevX + (passedEntity.getX() - passedEntity.prevX) * partialTicks;
        double y = passedEntity.prevY + (passedEntity.getY() - passedEntity.prevY) * partialTicks;
        double z = passedEntity.prevZ + (passedEntity.getZ() - passedEntity.prevZ) * partialTicks;

        EntityRenderDispatcher renderManager = MinecraftClient.getInstance().getEntityRenderDispatcher();
        matrices.translate(x - renderManager.camera.getPos().x, y - renderManager.camera.getPos().y + passedEntity.getHeight() + 0.5F, z - renderManager.camera.getPos().z);

        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        Quaternion rotation = camera.getRotation().copy();
        rotation.scale(-1.0F);
        matrices.multiply(rotation);

        matrices.scale(-0.025F, -0.025F, 0.025F);

        String time = TTLConfigManger.getConfig().isDisplayInTicks() ? fuse + " t" : ticksToTime(fuse);
        float offset = (float)(-mc.textRenderer.getWidth(time)/2);
        Matrix4f modelViewMatrix = matrices.peek().getPositionMatrix();
        mc.textRenderer.draw(time, offset, 0, 553648127, false, modelViewMatrix, immediate, true, 1056964608, 15728640);
        mc.textRenderer.draw(time, offset, 0, -1, false, modelViewMatrix, immediate, false, 0, 15728640);

        matrices.pop();
    }

    private static String ticksToTime(int ticks){
        if(ticks > 20*3600){
            int h = ticks/20/3600;
            return h+" h";
        } else if(ticks > 20*60){
            int m = ticks/20/60;
            return m+" m";
        } else {
            int s = ticks / 20;
            int ms = (ticks % 20) / 2;
            return s+"."+ms+" s";
        }
    }
}
