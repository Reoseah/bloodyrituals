package io.github.reoseah.bcoven;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
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

        public static final Item BLOOD_CLOT = new Item(new Item.Settings().group(GROUP).food(new FoodComponent.Builder().hunger(1).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 10 * 20), 0.9F).alwaysEdible().build()));
        public static final Item BEAST_FANG = new Item(new Item.Settings().group(GROUP));
        public static final Item WITCH_EYE = new Item(new Item.Settings().group(GROUP).food(new FoodComponent.Builder().hunger(1).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 10 * 20), 1.0F).alwaysEdible().build()));

        public static void register() {
            register("blood_stained_cobblestone", BLOOD_STAINED_COBBLESTONE);

            register("blood_clot", BLOOD_CLOT);
            register("beast_fang", BEAST_FANG);
            register("witch_eye", WITCH_EYE);
        }

        private static void register(String name, Item entry) {
            Registry.register(Registry.ITEM, BloodyCoven.id(name), entry);
        }
    }
}
