package btw.community.example;

import api.achievement.AchievementEvents;
import api.achievement.AchievementProvider;
import api.achievement.AchievementTab;
import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.minecraft.src.*;

import java.util.Locale;

/**
 * Covers defining new achievements and achievement tabs.
 * @see btw.achievement.BTWAchievements
 * @see AchievementEvents
 */
public class ExampleAchievements {
    public static final AchievementTab EXAMPLE;

    // When defining a new Achievement, you must make sure the parameterized type matches the
    // associated AchievementEvent's parameterized type
    public static final Achievement<ItemStack> EXAMPLE_OBTAIN;
    public static final Achievement<ItemStack> EXAMPLE_EAT;
    public static final Achievement<AchievementEvents.EntityInteractedEventData> EXAMPLE_KILL;
    public static final Achievement<ExampleAchievementTrigger.WorldData> EXAMPLE_EXIST;

    // Important to call this during addon initialization, so that everything in our static block runs
    public static void init() {
    }

    static {
        // Registering a new tab for your addon's achievements can be done like so.
        // The string provided in the constructor determines the translation key.
        // In this instance, the translation key resolves to "achievementtab.api_example"
        // You can set the icon to either an item or a block
        EXAMPLE = new AchievementTab("api_example")
                .setIcon(BTWBlocks.companionCube);

        // Registering a new achievement is more complex. Achievements are created using
        // a builder system. Each step is broken down with an explanation:
        EXAMPLE_OBTAIN = AchievementProvider
                // Here we are getting a builder that uses the type contained within an AchievementEvent,
                // in this case we are giving it the ItemEvent, which has the type ItemStack.
                .getBuilder(AchievementEvents.ItemEvent.class)

                // This assigns a unique ID to this achievement, which determines it's name
                // and description keys. In this case, this would resolve as:
                // "achievement.btwce_api_example.example_1" - Name key
                // "achievement.btwce_api_example.example_1.desc" - Description key
                .name(new ResourceLocation("btwce_api_example", "example_1"))

                // The icon set for this achievement, can be an Item, Block or ItemStack.
                .icon(BTWItems.boiledPotato)

                // The display location for the achievement. All achievements exist on a fixed
                // grid. 0,0 is a standard position for a root achievement.
                .displayLocation(0, 0)

                // The predicate that determines when this achievement should trigger. In our case,
                // this fires every ItemEvent, and will only pass if the stack passed in is the
                // boiled potato item. What information is available depends on the AchievementEvent used.
                .triggerCondition(stack -> stack.itemID == BTWItems.boiledPotato.itemID)

                // Builds a new achievement object with the data provided
                .build()

                // Registers the achievement under our previously defined EXAMPLE tab. You can also
                // register achievements under existing tabs by referencing them from BTWAchievements,
                // however this is discouraged if you are trying to be compatible with other addons.
                .registerAchievement(EXAMPLE);

        // Example two has a different trigger, but shares the same type as the previous achievement event,
        // so a bulk of the previous code and trigger condition can be reused verbatim.
        EXAMPLE_EAT = AchievementProvider
                .getBuilder(AchievementEvents.EatenEvent.class)
                .name(new ResourceLocation("btwce_api_example", "example_2"))
                .icon(BTWItems.boiledPotato)
                .displayLocation(0, 1)
                .triggerCondition(stack -> stack.itemID == BTWItems.boiledPotato.itemID)

                // You can set the parent of an achievement to another already defined one.
                // This will draw a line between the achievements.
                .parents(EXAMPLE_OBTAIN)
                .build()

                // When an achievement is marked as hidden, it will be completely invisible.
                .setHidden()
                .registerAchievement(EXAMPLE);

        // Example three has a different trigger with different predicate logic.
        EXAMPLE_KILL = AchievementProvider
                .getBuilder(AchievementEvents.EntityKilledEvent.class)
                .name(new ResourceLocation("btwce_api_example", "example_3"))
                .icon(Item.beefRaw)
                .displayLocation(2, 0)
                .triggerCondition(data -> data.entity() instanceof EntityCow)
                .parents(EXAMPLE_OBTAIN)
                .build()

                // When an achievement is marked as secret, it will be visible, but the
                // method to obtain it and the icon will be obfuscated.
                .setSecret()
                .registerAchievement(EXAMPLE);

        // This final example uses a custom trigger defined in ExampleAchievementTrigger.
        EXAMPLE_EXIST = AchievementProvider
                .getBuilder(ExampleAchievementTrigger.ExampleEvent.class)
                .name(new ResourceLocation("btwce_api_example", "example_4"))
                .icon(Item.pocketSundial)
                .displayLocation(1, -2)
                .triggerCondition(worldData -> worldData.dimension().toLowerCase(Locale.ROOT).equals("overworld") && worldData.ticksElapsed() > 100)
                .parents(EXAMPLE_KILL)
                .build()
                .registerAchievement(EXAMPLE);
    }
}
