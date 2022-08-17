package io.github.reoseah.bloodyrituals.ritual.effect;

import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;
import io.github.reoseah.bloodyrituals.ritual.step.RitualStep;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.Collection;

public class LifeTransfusionEffect extends RitualEffect {
    @Override
    public void addSteps(Collection<RitualStep> steps) {
        steps.add(new RitualStep() {
            @Override
            public Result tick(CenterGlyphBlockEntity glyph, int time) {
                if (time % 20 != 0) {
                    return Result.CONTINUE;
                }
                LivingEntity sacrifice = glyph.getSacrifice();
//                int sacrificeMaxHp = sacrifice.defaultMaxHealth;
                // FIXME: increase player max hp

                if (sacrifice != null) {
                    sacrifice.damage(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE); // TODO blood magic damage source...

                    return Result.COMPLETE;
                }
                return Result.ABORT;
            }
        });
    }
}
