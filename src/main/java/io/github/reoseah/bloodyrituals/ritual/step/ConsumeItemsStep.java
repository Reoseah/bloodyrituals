package io.github.reoseah.bloodyrituals.ritual.step;

import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;
import io.github.reoseah.bloodyrituals.recipe.RitualRecipe;
import io.github.reoseah.bloodyrituals.ritual.RitualEvent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
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
    public Result tick(CenterGlyphBlockEntity glyph, int time) {
        if (time % 20 != 0) {
            return Result.CONTINUE;
        }
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

                glyph.sendEvent(RitualEvent.CONSUME_ITEM, (float) entity.getPos().getX(), (float) entity.getPos().getY(), (float) entity.getPos().getZ());

                return this.ingredients.isEmpty() ? Result.COMPLETE : Result.CONTINUE;
            }
        }

        return Result.ABORT;
    }

    @Override
    public void abort(CenterGlyphBlockEntity glyph) {
        ItemScatterer.spawn(glyph.getWorld(), glyph.getPos(), this.consumedItems);
    }
}
