package io.github.reoseah.bloodyrituals.block.entity;

import io.github.reoseah.bloodyrituals.BloodyRituals;
import io.github.reoseah.bloodyrituals.recipe.RitualRecipe;
import io.github.reoseah.bloodyrituals.ritual.RitualEvent;
import io.github.reoseah.bloodyrituals.ritual.step.RitualStep;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.*;

public class CenterGlyphBlockEntity extends BlockEntity {
    protected Queue<RitualStep> steps;
    protected int ticks;
    protected PlayerEntity initiator;
    protected LivingEntity sacrifice;

    public CenterGlyphBlockEntity(BlockPos pos, BlockState state) {
        super(BloodyRituals.BlockEntityTypes.CENTER_GLYPH, pos, state);
    }

    public static void tickServer(World world, BlockPos pos, BlockState state, CenterGlyphBlockEntity be) {
        if (be.steps != null && !be.steps.isEmpty()) {
            be.ticks++;

            RitualStep currentStep = be.steps.peek();
            RitualStep.Result result = currentStep.tick(be, be.ticks);
            switch (result) {
                case ABORT -> {
                    for (RitualStep step : be.steps) {
                        step.abort(be);
                        be.steps = null;
                    }
                }
                case COMPLETE -> {
                    be.steps.poll();
                    if (be.steps.isEmpty()) {
                        be.steps = null;
                        be.ticks = 0;
                    }
                }
            }
        }
    }

    public ActionResult activate(PlayerEntity player, Hand hand, BlockHitResult hit) {
        World world = Objects.requireNonNull(this.world);

        List<ItemStack> stacks = new ArrayList<>();
        for (ItemEntity entity : world.getEntitiesByClass(ItemEntity.class, new Box(this.pos.add(-2, -2, -2), this.pos.add(2, 2, 2)), itemEntity -> true)) {
            stacks.add(entity.getStack().copy());
        }

        Inventory inventory = new SimpleInventory(stacks.size());
        for (int i = 0; i < stacks.size(); i++) {
            inventory.setStack(i, stacks.get(i));
        }

        List<RitualRecipe> rituals = world.getRecipeManager().listAllOfType(BloodyRituals.RecipeTypes.RITUAL);
        Queue<RitualStep> steps = new ArrayDeque<>();
        for (RitualRecipe ritual : rituals) {
            if (ritual.matches(inventory, world)) {
                ritual.addSteps(steps);
            }
        }

        if (!steps.isEmpty()) {
            this.steps = steps;
            this.ticks = 0;

            this.sendEvent(RitualEvent.START, this.pos.getX() + 0.5F, this.pos.getY() + 0.25F, this.pos.getZ() + 0.5F);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    public void sendEvent(RitualEvent type, float x, float y, float z) {
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeFloat(x);
        buffer.writeFloat(y);
        buffer.writeFloat(z);
        buffer.writeVarInt(type.ordinal());

        PlayerLookup.tracking(this).forEach(p -> ServerPlayNetworking.send(p, BloodyRituals.createId("ritual_event"), buffer));
    }

    public LivingEntity getSacrifice() {
        return sacrifice;
    }

    public void setSacrifice(LivingEntity sacrifice) {
        this.sacrifice = sacrifice;
        this.markDirty();
    }
}
