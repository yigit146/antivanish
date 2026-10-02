package com.antivanish;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class AntiVanishScreen extends Screen {
    private ButtonWidget toggleButton;

    public AntiVanishScreen() {
        super(Text.literal("AntiVanish"));
    }

    private Text toggleText() {
        return Text.literal("Tespit: " + (AntiVanishMod.enabled ? "ACIK" : "KAPALI"));
    }

    @Override
    protected void init() {
        toggleButton = ButtonWidget.builder(toggleText(), button -> {
            AntiVanishMod.enabled = !AntiVanishMod.enabled;
            button.setMessage(toggleText());
        }).dimensions(this.width / 2 - 75, this.height / 2 - 10, 150, 20).build();
        this.addDrawableChild(toggleButton);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), b -> this.close())
                .dimensions(this.width / 2 - 75, this.height / 2 + 20, 150, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFFFF);
    }
}
