package io.github.reoseah.bloodyrituals.ritual.step;

import io.github.reoseah.bloodyrituals.BloodyRituals;
import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;
import io.github.reoseah.bloodyrituals.recipe.RitualRecipe;
import io.github.reoseah.bloodyrituals.ritual.RitualEvent;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class ConsumeItemsStep extends RitualStep {
    protected final Queue<Ingredient> ingredients;
    protected DefaultedList<ItemStack> consumedItems = DefaultedList.of();

    public ConsumeItemsStep(Queue<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static ConsumeItemsStep of(RitualRecipe recipe) {
        Queue<Ingredient> ingredients = new ArrayDeque<>(recipe.ingredients);
        return new ConsumeItemsStep(ingredients);
    }

    @Override
    public TickResult tick(CenterGlyphBlockEntity glyph, int time) {
        if (Objects.requireNonNull(glyph.getWorld()).getTime() % 20 == 0) {
            Ingredient ingredient = Objects.requireNonNull(this.ingredients.peek());

            List<ItemEntity> itemEntities = glyph.getWorld().getEntitiesByClass(ItemEntity.class, new Box(glyph.getPos().add(-2, -2, -2), glyph.getPos().add(2, 2, 2)), itemEntity -> true);
            for (ItemEntity entity : itemEntities) {
                ItemStack stack = entity.getStack();
                if (ingredient.test(stack)) {
                    ItemStack consumed = stack.split(1);
                    this.consumedItems.add(consumed);
                    entity.setStack(stack); // it should be tracked, but anyway
                    this.ingredients.poll();
                    // TODO: maybe spawn item remainder?

                    PacketByteBuf buffer = PacketByteBufs.create();
                    buffer.writeFloat(glyph.getPos().getX() + 0.5F);
                    buffer.writeFloat(glyph.getPos().getY() + 0.25F);
                    buffer.writeFloat(glyph.getPos().getZ() + 0.5F);
                    buffer.writeVarInt(RitualEvent.CONSUME_ITEM.ordinal());

                    PlayerLookup.tracking(glyph).forEach(p -> ServerPlayNetworking.send(p, BloodyRituals.createId("ritual_event"), buffer));


                    return this.ingredients.isEmpty() ? TickResult.COMPLETE : TickResult.CONTINUE;
                }
            }

            // missing an ingredient - return items consumed so far and abort
            return TickResult.ABORT;
        }
        return TickResult.CONTINUE;
    }

    @Override
    public void abort(CenterGlyphBlockEntity glyph) {
        ItemScatterer.spawn(glyph.getWorld(), glyph.getPos(), this.consumedItems);
    }
}
