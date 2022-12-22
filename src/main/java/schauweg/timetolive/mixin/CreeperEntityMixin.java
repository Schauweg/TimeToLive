package schauweg.timetolive.mixin;


import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreeperEntity.class)
public interface CreeperEntityMixin {

    @Accessor("currentFuseTime")
    int getCurrentFuseTime();

    @Accessor("fuseTime")
    int getFuseTime();

}
