package com.example.tribefaith.data;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum Tribe implements Buffed {
    HUMAN("human", "มนุษย์",
            "หัวใจ +1 ดวง, ความเร็วโจมตี +10%, โชค +0.5. สมดุล ไม่มีข้อเสีย",
            List.of(
                    new AttrMod(Attributes.MAX_HEALTH, 2.0, Operation.ADDITION),
                    new AttrMod(Attributes.ATTACK_SPEED, 0.10, Operation.MULTIPLY_BASE),
                    new AttrMod(Attributes.LUCK, 0.5, Operation.ADDITION))),

    DWARF("dwarf", "คนแคระเผ่าภูผา",
            "เกราะ +3, ต้านแรงผลัก +30%, ขุดเร็ว (Haste I). ข้อเสีย: เคลื่อนที่ช้าลง 8%",
            List.of(
                    new AttrMod(Attributes.ARMOR, 3.0, Operation.ADDITION),
                    new AttrMod(Attributes.KNOCKBACK_RESISTANCE, 0.3, Operation.ADDITION),
                    new AttrMod(Attributes.MOVEMENT_SPEED, -0.08, Operation.MULTIPLY_BASE))),

    ELF("elf", "เอลฟ์เผ่าพงไพร",
            "เคลื่อนที่เร็ว +15%, กระโดดสูง (Jump Boost I), โชค +1. ข้อเสีย: หัวใจ -2 ดวง",
            List.of(
                    new AttrMod(Attributes.MOVEMENT_SPEED, 0.15, Operation.MULTIPLY_BASE),
                    new AttrMod(Attributes.LUCK, 1.0, Operation.ADDITION),
                    new AttrMod(Attributes.MAX_HEALTH, -4.0, Operation.ADDITION))),

    MERFOLK("merfolk", "เงือกเผ่าสมุทร",
            "หายใจใต้น้ำได้, ว่ายน้ำเร็ว (Dolphin's Grace). ข้อเสีย: เคลื่อนที่ช้าลง 5%",
            List.of(
                    new AttrMod(Attributes.MOVEMENT_SPEED, -0.05, Operation.MULTIPLY_BASE))),

    EMBER("ember", "เผ่าเพลิง",
            "ทนไฟและลาวา, พลังโจมตี +1.5. ข้อเสีย: หัวใจ -2 ดวง",
            List.of(
                    new AttrMod(Attributes.ATTACK_DAMAGE, 1.5, Operation.ADDITION),
                    new AttrMod(Attributes.MAX_HEALTH, -4.0, Operation.ADDITION)));

    private final String id;
    private final String displayName;
    private final String description;
    private final List<AttrMod> mods;

    Tribe(String id, String displayName, String description, List<AttrMod> mods) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.mods = mods;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public List<AttrMod> mods() {
        return mods;
    }

    @Override
    public void tickEffects(ServerPlayer p) {
        switch (this) {
            case DWARF -> Effects.give(p, MobEffects.DIG_SPEED, 0);
            case ELF -> Effects.give(p, MobEffects.JUMP, 0);
            case MERFOLK -> {
                Effects.give(p, MobEffects.WATER_BREATHING, 0);
                if (p.isInWater()) {
                    Effects.give(p, MobEffects.DOLPHINS_GRACE, 0);
                }
            }
            case EMBER -> Effects.give(p, MobEffects.FIRE_RESISTANCE, 0);
            default -> {
            }
        }
    }

    public static Tribe byId(String id) {
        for (Tribe t : values()) {
            if (t.id.equals(id)) {
                return t;
            }
        }
        return null;
    }
}
