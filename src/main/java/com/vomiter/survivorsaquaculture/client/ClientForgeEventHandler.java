package com.vomiter.survivorsaquaculture.client;

import com.mojang.blaze3d.platform.Window;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientForgeEventHandler {

    public static void init(){
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ClientForgeEventHandler::onRenderGameOverlayPost);
    }

    private static void drawCenteredText(Minecraft minecraft, GuiGraphics graphics, Component text, int x, int y)
    {
        final int textWidth = minecraft.font.width(text) / 2;
        graphics.drawString(minecraft.font, text, x - textWidth, y, 0xCCCCCC, false);
    }

    public static void onRenderGameOverlayPost(RenderGuiOverlayEvent.Post event){
        final GuiGraphics guiGraphics = event.getGuiGraphics();
        final Minecraft minecraft = Minecraft.getInstance();
        final HitResult hitResult = minecraft.hitResult;
        final Player player = minecraft.player;
        if (hitResult instanceof EntityHitResult entityHitResult) {
            if(entityHitResult.getEntity() instanceof FishMountEntity fishMount && fishMount.entity != null){
                Window window = event.getWindow();
                int x = window.getGuiScaledWidth() / 2 + 3;
                int y = window.getGuiScaledHeight() / 2 - 32;
                drawCenteredText(minecraft, guiGraphics, fishMount.entity.getDisplayName(), x, y);
            }
        }
    }
}