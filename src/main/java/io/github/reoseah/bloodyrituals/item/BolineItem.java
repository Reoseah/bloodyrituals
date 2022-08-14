package io.github.reoseah.bloodyrituals.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

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
}
