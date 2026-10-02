package com.antivanish;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AntiVanishMod implements ClientModInitializer {
    public static boolean enabled = true;

    private static KeyBinding openKey;
    private static final Set<UUID> warned = new HashSet<>();

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("antivanish", "main"));
        openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.antivanish.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_L,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        while (openKey.wasPressed()) {
            if (client.currentScreen == null) {
                client.setScreen(new AntiVanishScreen());
            }
        }

        if (client.world == null || client.player == null || client.getNetworkHandler() == null) {
            warned.clear();
            return;
        }
        if (!enabled) return;

        Set<UUID> present = new HashSet<>();
        for (AbstractClientPlayerEntity p : client.world.getPlayers()) {
            UUID id = p.getUuid();
            if (id.equals(client.player.getUuid())) continue;
            present.add(id);

            // Dunyada gorunuyor ama tab listesinde yok
            if (client.getNetworkHandler().getPlayerListEntry(id) == null && warned.add(id)) {
                client.player.sendMessage(
                        Text.literal("[AntiVanish] UYARI: " + p.getName().getString() + " vanish'te olabilir!")
                                .formatted(Formatting.RED),
                        false
                );
            }
        }
        // Gorunmeyenleri listeden cikar, tekrar gelirse yeniden uyarsin
        warned.retainAll(present);
    }
}
