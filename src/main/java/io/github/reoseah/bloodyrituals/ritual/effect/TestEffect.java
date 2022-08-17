package io.github.reoseah.bloodyrituals.ritual.effect;

import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;
import io.github.reoseah.bloodyrituals.ritual.step.RitualStep;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Objects;

public class TestEffect extends RitualEffect {
    @Override
    public void addSteps(Collection<RitualStep> steps) {
        steps.add(new RitualStep() {
            @Override
            public Result tick(CenterGlyphBlockEntity glyph, int time) {
                if (time % 20 == 0) {
                    World world = Objects.requireNonNull(glyph.getWorld());
                    BlockPos pos = Objects.requireNonNull(glyph.getPos());

                    for (int dx = -5; dx <= 5; dx++) {
                        for (int dz = -5; dz <= 5; dz++) {
                            if (world.isAir(pos.add(dx, 0, dz)) && world.canPlace(Blocks.FIRE.getDefaultState(), pos.add(dx, 0, dz), ShapeContext.absent())) {
                                world.setBlockState(pos.add(dx, 0, dz), Blocks.FIRE.getDefaultState());
                            }
                        }
                    }

                    return Result.COMPLETE;
                }
                return Result.CONTINUE;
            }
        });
    }
}
