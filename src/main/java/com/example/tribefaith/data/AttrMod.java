package com.example.tribefaith.data;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

/** One attribute change. Each tribe/religion must use an attribute at most once. */
public record AttrMod(Attribute attribute, double amount, AttributeModifier.Operation op) {
}
