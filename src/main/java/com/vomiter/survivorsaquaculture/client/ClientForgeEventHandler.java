package com.vomiter.survivorsaquaculture.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.vomiter.survivorsaquaculture.SAquaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
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
        bus.addListener(ClientForgeEventHandler::onRenderGameOverlayPost_FishMountName);
    }

    private static void drawCenteredText(Minecraft minecraft, GuiGraphics graphics, Component text, int centerX, int baselineY) {
        Font font = minecraft.font;
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        int paddingX = 3;
        int paddingY = 2;

        int boxWidth  = textWidth + paddingX * 2;
        int boxHeight = textHeight + paddingY * 2;

        int boxX = centerX - boxWidth / 2;
        int boxY = baselineY - textHeight - paddingY;

        PoseStack pose = graphics.pose();
        pose.pushPose();

        int z = 400;

        graphics.drawManaged(() -> {
            int bgStart     = 0x08100010;
            int bgEnd       = 0x08100010;
            int borderStart = 0x00000000;
            int borderEnd   = 0x00000000;

            TooltipRenderUtil.renderTooltipBackground(
                    graphics,
                    boxX,
                    boxY,
                    boxWidth,
                    boxHeight,
                    z,
                    bgStart,
                    bgEnd,
                    borderStart,
                    borderEnd
            );
        });

        pose.translate(0.0F, 0.0F, (float) z);

        int textX = boxX + paddingX;
        int textY = boxY + paddingY;

        graphics.drawString(
                font,
                text,
                textX,
                textY,
                0xFFFFFF,
                true  // 陰影
        );

        pose.popPose();
    }

    public static void onRenderGameOverlayPost_FishMountName(RenderGuiOverlayEvent.Post event){
        if(!SAquaConfig.COMMON.ADJUST_FISH_SIZE.get()) return;
        final GuiGraphics guiGraphics = event.getGuiGraphics();
        final Minecraft minecraft = Minecraft.getInstance();
        final HitResult hitResult = minecraft.hitResult;
        final Player player = minecraft.player;
        if (hitResult instanceof EntityHitResult entityHitResult && player != null) {
            if(entityHitResult.getEntity() instanceof FishMountEntity fishMount && fishMount.entity != null && player.distanceTo(fishMount) < 3){
                Window window = event.getWindow();
                int x = window.getGuiScaledWidth() / 2 + 3;
                int y = window.getGuiScaledHeight() / 2 - 32;
                drawCenteredText(minecraft, guiGraphics, fishMount.entity.getDisplayName(), x, y);
            }
        }
    }
}