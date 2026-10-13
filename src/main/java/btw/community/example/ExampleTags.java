package btw.community.example;

import api.AddonHandler;
import api.item.tag.Tag;
import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.item.BTWTags;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ResourceLocation;

/**
 * Covers defining tags and usage
 * @see Tag
 * @see api.item.tag.TagOrStack
 * @see api.item.tag.TagInstance
 */
public class ExampleTags {
    // NOTE: You should hold onto tag instances you create, so you can reference
    // them in other areas of your codebase. You can alternatively retrieve tags
    // you've made (and other tags) by using Tag#getFromID, but is less convenient
    public static final Tag TAG_EXAMPLE;
    public static final Tag TAG_EXAMPLE2;

    // Important to call this during addon initialization, so that everything in our static block runs
    public static void init() {
    }

    private static <T> void printTest(T thing, boolean value) {
        AddonHandler.logMessage("[Tag Test] %s tested: %b".formatted(thing.toString(), value));
    }

    static {
        // Tags are a way to group different blocks and items. You can set them up
        // using the Tag#of method.
        TAG_EXAMPLE = Tag
                // Here we define the ID for our tag, namespaced to this addon.
                .of(new ResourceLocation("btwce_api_example", "example"))

                // For simple items, blocks, and stacks, you can use Tag#add.
                .add(BTWItems.gimpHelmet)
                .add(BTWBlocks.companionCube)
                .add(new ItemStack(Block.leaves, 1, 2))

                // You can even add other tag contents into your own tag.
                .add(BTWTags.grassBlocks);

        // Here's another tag definition, covering items with subtypes.
        TAG_EXAMPLE2 = Tag
                // For simple tags, you can skip a lot of the extra methods and
                // define a list of blocks, items, stacks or tags to represent in
                // the initial creation method
                .of(new ResourceLocation("btwce_api_example", "example2"), TAG_EXAMPLE)

                // For items with subtypes, you can use Tag#addIgnoringMeta to
                // have all subtypes of the given item(s) represented in the tag.
                //
                // NOTE: This is a shorthand for registering an ItemStack
                // with a damage value of Short.MAX (which makes it a wildcard)
                //
                // NOTE: registering an item this way only shows the first
                // subtype of the item when viewing the tag in EMI, but it will still
                // technically represent all subtypes of the provided item. If you want
                // all subtypes to show up, use one of the methods below instead.
                .addIgnoringMeta(Item.dyePowder)

                // Another method for working with subtypes is Tag#addUntilDamage,
                // which will only add subtypes up to the provided stack damage value.
                // This example will add Oak and Spruce logs to the tag.
                .addUntilDamage(1, Block.wood)

                // Finally, a more finely controlled method for adding a range of
                // subtypes. This example adds Birch and Jungle logs.
                .addFromAndUntilDamage(2, 3, Block.wood);

        // Tags can be checked using Tag#test (run the game and check logs to see results!)
        ItemStack beef = new ItemStack(Item.beefRaw);
        printTest(beef, TAG_EXAMPLE2.test(beef));

        ItemStack gimpHelmet = new ItemStack(BTWItems.gimpHelmet);
        printTest(gimpHelmet, TAG_EXAMPLE2.test(gimpHelmet));

        ItemStack leaves = new ItemStack(Block.leaves);
        printTest(leaves, TAG_EXAMPLE2.test(leaves));

        // Tags can also be used as ingredients in recipes. This is generally preferred
        // over using the legacy wildcard system, and most recipes have migrated to
        // use this system.

        // WIP
    }
}
