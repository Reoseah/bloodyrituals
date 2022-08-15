package io.github.reoseah.bloodyrituals.ritual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Util;

public enum RitualEvent {
    START, CONSUME_ITEM;

    @Environment(value = EnvType.CLIENT)
    public void apply(ClientWorld world, float x, float y, float z) {
        for (int i = 0; i < 8; i++) {
            float x2 = x - 0.25F + world.random.nextFloat() * 0.5F;
            float y2 = y - 0.25F + world.random.nextFloat() * 0.5F;
            float z2 = z - 0.25F + world.random.nextFloat() * 0.5F;

            world.addParticle(ParticleTypes.DUST.getParametersFactory().read(ParticleTypes.DUST, Util.make(PacketByteBufs.create(), buf -> {
                buf.writeFloat(1);
                buf.writeFloat(0);
                buf.writeFloat(0);
                buf.writeFloat(1);
            })), x2, y2, z2, 255, 0, 0);
        }
    }
}
