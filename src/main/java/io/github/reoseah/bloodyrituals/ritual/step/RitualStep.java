package io.github.reoseah.bloodyrituals.ritual.step;

import io.github.reoseah.bloodyrituals.block.entity.CenterGlyphBlockEntity;

public abstract class RitualStep {
    public enum TickResult {
        CONTINUE, COMPLETE, ABORT; // TODO: sustain rituals while starting next one
    }

    public abstract TickResult tick(CenterGlyphBlockEntity glyph, int time);

    public void abort(CenterGlyphBlockEntity glyph) {

    }
}
