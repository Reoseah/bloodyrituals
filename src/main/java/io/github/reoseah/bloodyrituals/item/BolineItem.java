package io.github.reoseah.bloodyrituals.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BolineItem extends SwordItem {
    private static final EntityAttributeModifier REACH_MODIFIER = new EntityAttributeModifier(UUID.fromString("62BC55A1-EA10-435D-8BD3-3E8FB8B090CB"), "Weapon modifier", -0.5, EntityAttributeModifier.Operation.ADDITION);

    public BolineItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = LinkedHashMultimap.create(super.getAttributeModifiers(slot));
        if (slot == EquipmentSlot.MAINHAND) {
            map.put(ReachEntityAttributes.ATTACK_RANGE, REACH_MODIFIER);
        }
        return map;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof PlayerEntity) {
            stack.getOrCreateNbt().putUuid("TargetUUID", target.getUuid());
            stack.getOrCreateNbt().putString("TargetName", target.getEntityName());
            stack.getOrCreateNbt().putBoolean("TargetIsPlayer", true);
        } else if (target instanceof MerchantEntity || target instanceof WitchEntity || target instanceof IllagerEntity) {
            stack.getOrCreateNbt().putUuid("TargetUUID", target.getUuid());
            stack.getOrCreateNbt().putString("TargetName", target.getDisplayName().toString());
            stack.getOrCreateNbt().putBoolean("TargetIsPlayer", false);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        if (hasTarget(stack)) {
            return "item.bloodyrituals.boline.blood_covered";
        }
        return this.getTranslationKey();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        super.appendStacks(group, stacks);
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && this.isIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateNbt().putUuid("TargetUUID", player.getUuid());
            stack.getOrCreateNbt().putString("TargetName", player.getEntityName());
            stack.getOrCreateNbt().putBoolean("TargetIsPlayer", true);
            stacks.add(stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (hasTarget(stack) && world != null) {
            UUID uuid = getTargetUUID(stack);
            for (PlayerEntity player : world.getPlayers()) {
                if (player.getUuid().equals(uuid)) {
                    tooltip.add(Text.translatable("item.bloodyrituals.boline.target_player", player.getName()).formatted(Formatting.GRAY));
                    return;
                }
            }
            tooltip.add(Text.translatable("item.bloodyrituals.boline.target_player", getTargetName(stack)).formatted(Formatting.GRAY));
        }
    }

    public static boolean hasTarget(ItemStack stack) {
        return stack.getNbt() != null && stack.getNbt().contains("TargetUUID");
    }

    public static UUID getTargetUUID(ItemStack stack) {
        return stack.getNbt() != null ? stack.getNbt().getUuid("TargetUUID") : null;
    }

    public static Text getTargetName(ItemStack stack) {
        return stack.getNbt() != null ? Text.literal(stack.getNbt().getString("TargetName")) : Text.translatable("item.bloodyrituals.boline.unknown_target");
    }

    public static boolean isTargetPlayer(ItemStack stack) {
        return stack.getNbt() != null && stack.getNbt().getBoolean("TargetIsPlayer");
    }
}
