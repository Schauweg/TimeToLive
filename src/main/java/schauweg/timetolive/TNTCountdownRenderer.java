package schauweg.timetolive;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class TNTCountdownRenderer {

    @SubscribeEvent
    public void onWorldRenderLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        Entity cameraEntity = mc.getRenderViewEntity();
        BlockPos renderingVector = cameraEntity.getPosition();
        Frustum frustum = new Frustum();

        float partialTicks = event.partialTicks;
        double viewX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * partialTicks;
        double viewY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * partialTicks;
        double viewZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * partialTicks;
        frustum.setPosition(viewX, viewY, viewZ);

        WorldClient client = mc.theWorld;
        Iterable<Entity> entitiesById = client.getLoadedEntityList();

        for(Entity entity : entitiesById) {
            if (entity != null && entity instanceof EntityTNTPrimed && entity.isInRangeToRender3d(renderingVector.getX(), renderingVector.getY(), renderingVector.getZ())){
                renderFuseCountdown((EntityTNTPrimed) entity, partialTicks, 0.5F);
            }
        }
    }


    private void renderFuseCountdown(EntityTNTPrimed passedEntity, float partialTicks, float nameOffset) {
        Minecraft mc = Minecraft.getMinecraft();
        float pastTranslate = 0F;

        int fuse = passedEntity.fuse;

        String fuseText = ticksToTime(fuse);

        double x = passedEntity.lastTickPosX + (passedEntity.posX - passedEntity.lastTickPosX) * partialTicks;
        double y = passedEntity.lastTickPosY + (passedEntity.posY - passedEntity.lastTickPosY) * partialTicks;
        double z = passedEntity.lastTickPosZ + (passedEntity.posZ - passedEntity.lastTickPosZ) * partialTicks;

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        double renderPosX = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "field_78725_b"); //renderPosX
        double renderPosY = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "field_78726_c"); //renderPosY
        double renderPosZ = ObfuscationReflectionHelper.getPrivateValue(RenderManager.class, renderManager, "field_78723_d"); //renderPosZ

        GlStateManager.translate(0F, pastTranslate, 0F);

        float scale = 0.026666672F;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (x - renderPosX), (float) (y - renderPosY + passedEntity.height) + nameOffset, (float) (z - renderPosZ));
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        int j = mc.fontRendererObj.getStringWidth(fuseText) / 2;

        wr.begin(7, DefaultVertexFormats.POSITION_COLOR);
        wr.pos((double)(-j - 1), (double)(-1 ), 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos((double)(-j - 1), (double)(8), 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos((double)(j + 1), (double)(8), 0.0D).color(0, 0, 0, 64).endVertex();
        wr.pos((double)(j + 1), (double)(-1), 0.0D).color(0, 0, 0, 64).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        mc.fontRendererObj.drawString(fuseText, -j,0, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        if(lighting)
            GlStateManager.enableLighting();
        mc.fontRendererObj.drawString(fuseText, -j,0, -1);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
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
