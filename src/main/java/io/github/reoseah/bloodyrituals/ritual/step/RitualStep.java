package io.github.reoseah.bloodyrituals.ritual.step;

import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;

public abstract class RitualStep {
    public enum Result {
        CONTINUE, COMPLETE, ABORT; // TODO: sustained rituals while starting next one
    }

    public abstract Result tick(CenterGlyphBlockEntity glyph, int time);

    public void abort(CenterGlyphBlockEntity glyph) {

    }
}
