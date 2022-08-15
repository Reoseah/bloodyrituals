package io.github.reoseah.bloodyrituals.ritual.effect;

import io.github.reoseah.bloodyrituals.ritual.step.RitualStep;

import java.util.Collection;

public abstract class RitualEffect {
    public abstract void addSteps(Collection<RitualStep> steps);
}
