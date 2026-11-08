package com.example.tribefaith.data;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;

public enum Religion implements Buffed {
    SUN("sun", "ลัทธิสุริยัน",
            "กลางวันและอยู่กลางแจ้ง ฟื้นฟูพลังชีวิตต่อเนื่อง (Regeneration I). กลางคืนไม่ได้รับพร",
            List.of()),

    MOON("moon", "ลัทธิจันทรา",
            "กลางคืน มองเห็นในที่มืด (Night Vision) และพลังแกร่ง (Strength I). กลางวันไม่ได้รับพร",
            List.of()),

    EARTH("earth", "ลัทธิธรณี",
            "เกราะทนทาน +2, ต้านแรงผลัก +10%, หัวใจ +1 ดวง",
            List.of(
                    new AttrMod(Attributes.ARMOR_TOUGHNESS, 2.0, Operation.ADDITION),
                    new AttrMod(Attributes.KNOCKBACK_RESISTANCE, 0.1, Operation.ADDITION),
                    new AttrMod(Attributes.MAX_HEALTH, 2.0, Operation.ADDITION))),

    STORM("storm", "ลัทธิวายุ",
            "ความเร็วโจมตี +20%, เคลื่อนที่เร็ว +8%. ข้อเสีย: หัวใจ -2 ดวง",
            List.of(
                    new AttrMod(Attributes.ATTACK_SPEED, 0.20, Operation.MULTIPLY_BASE),
                    new AttrMod(Attributes.MOVEMENT_SPEED, 0.08, Operation.MULTIPLY_BASE),
                    new AttrMod(Attributes.MAX_HEALTH, -4.0, Operation.ADDITION))),

    NONE("none", "ไม่นับถือศาสนา",
            "พึ่งพาตนเอง พลังโจมตี +1, โชค +1",
            List.of(
                    new AttrMod(Attributes.ATTACK_DAMAGE, 1.0, Operation.ADDITION),
                    new AttrMod(Attributes.LUCK, 1.0, Operation.ADDITION)));

    private final String id;
    private final String displayName;
    private final String description;
    private final List<AttrMod> mods;

    Religion(String id, String displayName, String description, List<AttrMod> mods) {
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
            case SUN -> {
                if (p.level().isDay() && p.level().canSeeSky(p.blockPosition())) {
                    Effects.give(p, MobEffects.REGENERATION, 0);
                }
            }
            case MOON -> {
                if (!p.level().isDay()) {
                    Effects.give(p, MobEffects.NIGHT_VISION, 0);
                    Effects.give(p, MobEffects.DAMAGE_BOOST, 0);
                }
            }
            default -> {
            }
        }
    }

    public static Religion byId(String id) {
        for (Religion r : values()) {
            if (r.id.equals(id)) {
                return r;
            }
        }
        return null;
    }
}
