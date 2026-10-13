package btw.community.example;

import api.achievement.AchievementEventDispatcher;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.WorldServer;

/**
 * Covers defining a new achievement trigger class.
 * <p>
 * For the vanilla hook used as an example, see {@link btw.community.example.mixin.WorldServerMixin}.
 *
 * @see api.achievement.AchievementEventDispatcher
 * @see api.achievement.AchievementEventDispatcher.AchievementEvent
 */
public class ExampleAchievementTrigger {
    // To create a new trigger, we must define our own AchievementEvent.
    // It's important to consider what data you want to pass through the generic type;
    // in our case we will pass in the player's dimension and time elapsed.
    public static class ExampleEvent extends AchievementEventDispatcher.AchievementEvent<WorldData> {
        public ExampleEvent() {}
    }
    public record WorldData(String dimension, long ticksElapsed) {}

    // Using the AchievementEventDispatcher, we can register our event type.
    public static void init() {
        AchievementEventDispatcher.registerEventType(ExampleEvent.class);
    }

    // Once an achievement event is defined, we must handle triggering it ourselves.
    // You can either call the trigger in your own classes, or add hooks to vanilla
    // logic using mixins. This method will fire every tick on the server.
    public static void onServerTick(WorldServer world) {

        // Since we are updating from a server tick, we need to iterate over
        // all players in the server. With most hooks/methods, you will usually
        // have a player reference ready without needing to do this.
        for (Object o : world.playerEntities) {
            EntityPlayer player = (EntityPlayer) o;

            // Provide the trigger our event class, the player we will test on,
            // and the data required by the event's generic type.
            AchievementEventDispatcher.triggerEvent(
                    ExampleEvent.class,
                    player,
                    new WorldData(world.provider.getDimensionName(), world.getWorldTime())
            );
        }
    }
}
