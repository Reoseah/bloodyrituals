package io.github.reoseah.bloodyrituals;

import io.github.reoseah.bloodyrituals.block.CenterGlyphBlock;
import io.github.reoseah.bloodyrituals.block.GlyphBlock;
import io.github.reoseah.bloodyrituals.item.BolineItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodyRituals implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bloodyrituals");

    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(id("main"), () -> new ItemStack(Items.BLOOD_STAINED_COBBLESTONE));

    @Override
    public void onInitialize() {
        Blocks.register();
        Items.register();

        // FIXME move to client side only
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BLOOD_RUNE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CENTER_GLYPH, RenderLayer.getCutout());
    }

    public static Identifier id(String path) {
        return new Identifier("bloodyrituals", path);
    }

    public static class Blocks {
        public static final Block BLOOD_STAINED_COBBLESTONE = new Block(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(2.0F, 6.0F));
        public static final Block BLOOD_RUNE = new GlyphBlock(AbstractBlock.Settings.of(Material.DECORATION).strength(3.0F).noCollision().nonOpaque());
        public static final Block CENTER_GLYPH = new CenterGlyphBlock(AbstractBlock.Settings.of(Material.DECORATION).strength(3.0F).noCollision().nonOpaque());

        public static void register() {
            register("blood_stained_cobblestone", BLOOD_STAINED_COBBLESTONE);
            register("glyph", BLOOD_RUNE);
            register("center_glyph", CENTER_GLYPH);
        }

        private static void register(String name, Block entry) {
            Registry.register(Registry.BLOCK, BloodyRituals.id(name), entry);
        }
    }

    public static class Items {
        public static final Item BLOOD_STAINED_COBBLESTONE = new BlockItem(Blocks.BLOOD_STAINED_COBBLESTONE, new Item.Settings().group(GROUP));

        public static final Item BOLINE = new BolineItem(ToolMaterials.BOLINE, 1, -2, new Item.Settings().group(GROUP));

        public static final Item BLOOD_CLOT = new AliasedBlockItem(Blocks.BLOOD_RUNE, new Item.Settings().group(GROUP));
        public static final Item BEAST_FANG = new Item(new Item.Settings().group(GROUP));
        public static final Item WITCH_EYE = new Item(new Item.Settings().group(GROUP).food(new FoodComponent.Builder().hunger(1).statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 10 * 20), 1.0F).alwaysEdible().build()));

        public static void register() {
            register("blood_stained_cobblestone", BLOOD_STAINED_COBBLESTONE);

            register("boline", BOLINE);
            register("blood_clot", BLOOD_CLOT);
            register("beast_fang", BEAST_FANG);
            register("witch_eye", WITCH_EYE);
        }

        private static void register(String name, Item entry) {
            Registry.register(Registry.ITEM, BloodyRituals.id(name), entry);
        }
    }

    public enum ToolMaterials implements ToolMaterial {
        BOLINE(1, 131, 4.0F, 1.0F, 5, Ingredient.ofItems(net.minecraft.item.Items.IRON_INGOT));

        private final int miningLevel;
        private final int itemDurability;
        private final float miningSpeed;
        private final float attackDamage;
        private final int enchantability;
        private final Ingredient repairIngredient;

        ToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Ingredient repairIngredient) {
            this.miningLevel = miningLevel;
            this.itemDurability = itemDurability;
            this.miningSpeed = miningSpeed;
            this.attackDamage = attackDamage;
            this.enchantability = enchantability;
            this.repairIngredient = repairIngredient;
        }

        @Override
        public int getDurability() {
            return this.itemDurability;
        }

        @Override
        public float getMiningSpeedMultiplier() {
            return this.miningSpeed;
        }

        @Override
        public float getAttackDamage() {
            return this.attackDamage;
        }

        @Override
        public int getMiningLevel() {
            return this.miningLevel;
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.repairIngredient;
        }
    }
}
