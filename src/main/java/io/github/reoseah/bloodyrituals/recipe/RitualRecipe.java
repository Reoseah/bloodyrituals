package io.github.reoseah.bloodyrituals.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.reoseah.bloodyrituals.BloodyRituals;
import io.github.reoseah.bloodyrituals.ritual.effect.RitualEffect;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipe implements Recipe<Inventory> {
    public final Identifier id;
    public final DefaultedList<Ingredient> ingredients;
    public final RitualEffect effect;

    public RitualRecipe(Identifier id, DefaultedList<Ingredient> ingredients, RitualEffect effect) {
        this.id = id;
        this.ingredients = ingredients;
        this.effect = effect;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        List<ItemStack> list = deepCopy(inventory);

        ingredient:
        for (Ingredient ingredient : this.ingredients) {
            for (ItemStack stack : list) {
                if (ingredient.test(stack)) {
                    stack.decrement(1);
                    continue ingredient;
                }
            }
            return false;
        }
        return true;
    }

    private static List<ItemStack> deepCopy(Inventory inventory) {
        List<ItemStack> result = new ArrayList<>(inventory.size());
        for (int i = 0; i < inventory.size(); i++) {
            result.add(inventory.getStack(i).copy());
        }
        return result;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return BloodyRituals.RecipeTypes.RITUAL;
    }

    public static class Serializer implements RecipeSerializer<RitualRecipe> {
        @Override
        public RitualRecipe read(Identifier id, JsonObject json) {
            DefaultedList<Ingredient> ingredients = Serializer.getIngredients(JsonHelper.getArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for ritual recipe");
            }
            RitualEffect effect = BloodyRituals.Registries.RITUAL_EFFECTS.get(new Identifier(JsonHelper.getString(json, "effect")));
            return new RitualRecipe(id, ingredients, effect);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i));
                if (ingredient.isEmpty()) {
                    continue;
                }
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        @Override
        public RitualRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(buf.readVarInt(), Ingredient.EMPTY);
            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromPacket(buf));
            }
            RitualEffect effect = BloodyRituals.Registries.RITUAL_EFFECTS.get(buf.readIdentifier());
            return new RitualRecipe(id, ingredients, effect);
        }

        @Override
        public void write(PacketByteBuf buf, RitualRecipe recipe) {
            buf.writeVarInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.write(buf);
            }
            buf.writeIdentifier(BloodyRituals.Registries.RITUAL_EFFECTS.getId(recipe.effect));
        }
    }
}
