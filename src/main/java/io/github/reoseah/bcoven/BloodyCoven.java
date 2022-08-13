package io.github.reoseah.bcoven;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodyCoven implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bcoven");

    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(id("main"), () -> new ItemStack(Items.BLOOD_STAINED_COBBLESTONE));

    @Override
    public void onInitialize() {
        Blocks.register();
        Items.register();
    }

    public static Identifier id(String path) {
        return new Identifier("bcoven", path);
    }

    public static class Blocks {
        public static final Block BLOOD_STAINED_COBBLESTONE = new Block(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(2.0F, 6.0F));

        public static void register() {
            register("blood_stained_cobblestone", BLOOD_STAINED_COBBLESTONE);
        }

        private static void register(String name, Block entry) {
            Registry.register(Registry.BLOCK, BloodyCoven.id(name), entry);
        }
    }

    public static class Items {
        public static final Item BLOOD_STAINED_COBBLESTONE = new BlockItem(Blocks.BLOOD_STAINED_COBBLESTONE, new Item.Settings().group(GROUP));

        public static void register() {
            register("blood_stained_cobblestone", BLOOD_STAINED_COBBLESTONE);
        }

        private static void register(String name, Item entry) {
            Registry.register(Registry.ITEM, BloodyCoven.id(name), entry);
        }
    }
}
