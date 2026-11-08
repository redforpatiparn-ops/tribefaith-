package com.example.tribefaith.client;

import com.example.tribefaith.data.Buffed;
import com.example.tribefaith.data.Religion;
import com.example.tribefaith.data.Tribe;
import com.example.tribefaith.net.ModNetwork;
import com.example.tribefaith.net.SelectionPacket;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class SelectionScreen extends Screen {
    private static final int BTN_W = 150;
    private static final int TOP = 40;
    private static final int STEP = 22;

    private final List<Button> tribeButtons = new ArrayList<>();
    private final List<Button> religionButtons = new ArrayList<>();
    private Button confirm;
    private Tribe selTribe;
    private Religion selReligion;
    private int leftX;
    private int rightX;

    public SelectionScreen() {
        super(Component.literal("เลือกเผ่าและศาสนาของคุณ"));
    }

    @Override
    protected void init() {
        tribeButtons.clear();
        religionButtons.clear();
        leftX = this.width / 2 - BTN_W - 6;
        rightX = this.width / 2 + 6;

        Tribe[] tribes = Tribe.values();
        for (int i = 0; i < tribes.length; i++) {
            Tribe t = tribes[i];
            Button b = Button.builder(Component.empty(), btn -> {
                selTribe = t;
                refresh();
            }).bounds(leftX, TOP + i * STEP, BTN_W, 20).build();
            tribeButtons.add(this.addRenderableWidget(b));
        }

        Religion[] religions = Religion.values();
        for (int i = 0; i < religions.length; i++) {
            Religion r = religions[i];
            Button b = Button.builder(Component.empty(), btn -> {
                selReligion = r;
                refresh();
            }).bounds(rightX, TOP + i * STEP, BTN_W, 20).build();
            religionButtons.add(this.addRenderableWidget(b));
        }

        confirm = this.addRenderableWidget(Button.builder(Component.literal("ยืนยัน"), btn -> onConfirm())
                .bounds(this.width / 2 - 60, this.height - 26, 120, 20).build());
        refresh();
    }

    private void refresh() {
        Tribe[] tribes = Tribe.values();
        for (int i = 0; i < tribes.length; i++) {
            tribeButtons.get(i).setMessage(label(tribes[i].displayName(), tribes[i] == selTribe));
        }
        Religion[] religions = Religion.values();
        for (int i = 0; i < religions.length; i++) {
            religionButtons.get(i).setMessage(label(religions[i].displayName(), religions[i] == selReligion));
        }
        confirm.active = selTribe != null && selReligion != null;
    }

    private static Component label(String name, boolean selected) {
        return selected
                ? Component.literal("> " + name + " <").withStyle(ChatFormatting.GREEN)
                : Component.literal(name);
    }

    private void onConfirm() {
        if (selTribe == null || selReligion == null) {
            return;
        }
        ModNetwork.CHANNEL.sendToServer(new SelectionPacket(selTribe.id(), selReligion.id()));
        this.onClose();
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(g);
        g.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);
        g.drawCenteredString(this.font, Component.literal("เผ่า"), leftX + BTN_W / 2, 27, 0xFFD966);
        g.drawCenteredString(this.font, Component.literal("ศาสนา"), rightX + BTN_W / 2, 27, 0xFFD966);

        super.render(g, mouseX, mouseY, partialTick);

        int descY = TOP + Math.max(Tribe.values().length, Religion.values().length) * STEP + 6;
        drawDescription(g, hoveredOr(tribeButtons, Tribe.values(), selTribe, mouseX, mouseY), leftX, descY);
        drawDescription(g, hoveredOr(religionButtons, Religion.values(), selReligion, mouseX, mouseY), rightX, descY);
    }

    private static <T> T hoveredOr(List<Button> buttons, T[] values, T fallback, int mouseX, int mouseY) {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isMouseOver(mouseX, mouseY)) {
                return values[i];
            }
        }
        return fallback;
    }

    private void drawDescription(GuiGraphics g, Buffed b, int x, int y) {
        if (b == null) {
            return;
        }
        g.drawString(this.font, Component.literal(b.displayName()).withStyle(ChatFormatting.YELLOW), x, y, 0xFFFFFF, false);
        int ly = y + 12;
        for (FormattedCharSequence line : this.font.split(Component.literal(b.description()), BTN_W)) {
            g.drawString(this.font, line, x, ly, 0xCCCCCC, false);
            ly += 10;
        }
    }

    /** The player must choose; ESC does nothing. */
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
