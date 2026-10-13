package btw.community.example.mixin;

import btw.community.example.ExampleAchievementTrigger;
import net.minecraft.src.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldServer.class)
public class WorldServerMixin {
    /**
     * Hook that is called at the end of each server tick
     * @see ExampleAchievementTrigger
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void api$achievementTriggerHook(CallbackInfo ci) {
        ExampleAchievementTrigger.onServerTick((WorldServer) (Object) this);
    }
}
