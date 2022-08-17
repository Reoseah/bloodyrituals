package io.github.reoseah.bloodyrituals.ritual.step;

import io.github.reoseah.bloodyrituals.BloodyRituals;
import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;
import io.github.reoseah.bloodyrituals.item.BolineItem;
import io.github.reoseah.bloodyrituals.ritual.RitualEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.UUID;

public class LocateSacrificeStep extends RitualStep {
    @Override
    public Result tick(CenterGlyphBlockEntity glyph, int time) {
        if (time % 20 != 0) {
            return Result.CONTINUE;
        }
        List<ItemEntity> itemEntities = glyph.getWorld().getEntitiesByClass(ItemEntity.class, new Box(glyph.getPos().add(-2, -2, -2), glyph.getPos().add(2, 2, 2)), itemEntity -> true);

        ItemEntity boline = null;
        for (ItemEntity entity : itemEntities) {
            ItemStack stack = entity.getStack();
            if (stack.isOf(BloodyRituals.Items.BOLINE)) {
                boline = entity;
                break;
            }
        }
        if (boline != null) {
            ItemStack stack = boline.getStack();

            UUID sacrificeUuid = BolineItem.getTargetUUID(stack);
            if (sacrificeUuid != null) {
                for (Entity entity : glyph.getWorld().getOtherEntities(null, new Box(glyph.getPos().add(-15, -15, -15), glyph.getPos().add(15, 15, 15)))) {
                    if (entity.getUuid().equals(sacrificeUuid) && entity instanceof LivingEntity livingEntity) {
                        glyph.setSacrifice(livingEntity);

                        BolineItem.removeTarget(stack);
                        Vec3d pos = boline.getPos();
                        boline.damage(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
                        ItemEntity result = new ItemEntity(glyph.getWorld(), pos.getX(), pos.getY(), pos.getZ(), stack);
                        glyph.getWorld().spawnEntity(result);

                        glyph.sendEvent(RitualEvent.CONSUME_ITEM, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
                        return Result.COMPLETE;
                    }
                }
            }
        }
        return Result.ABORT;
    }
}
