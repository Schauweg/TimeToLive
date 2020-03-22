package schauweg.timetolive;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TNTCountdownRenderer {

    @SubscribeEvent
    public void onNameplateRender(RenderNameplateEvent event){
        if (event.getEntity() instanceof TNTEntity && event.getEntity() != null){
            TNTEntity entity = (TNTEntity)(event.getEntity());
            EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
            IRenderTypeBuffer renderTypeBuffer = event.getRenderTypeBuffer();

            renderTag(manager, entity, ticksToTime(entity.getFuse()), event.getMatrixStack(), renderTypeBuffer);
        }
    }

    private void renderTag(EntityRendererManager manager, Entity entity, String text, MatrixStack stack, IRenderTypeBuffer impl) {
        float height = entity.getHeight();

        stack.push();
        stack.translate(0.0D, height + 0.5F, 0.0D);
        stack.rotate(manager.getCameraOrientation());
        stack.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matrix4f = stack.getLast().getMatrix();
        FontRenderer fontRenderer = manager.getFontRenderer();
        float offset = (float)(-fontRenderer.getStringWidth(text) / 2);

        fontRenderer.renderString(text, offset, 0F, 553648127, false, matrix4f, impl, true, 1056964608, 15728640);
        fontRenderer.renderString(text, offset, 0F, -1, false, matrix4f, impl, false, 0, 15728640);

        stack.pop();
    }

    private String ticksToTime(int ticks){
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
