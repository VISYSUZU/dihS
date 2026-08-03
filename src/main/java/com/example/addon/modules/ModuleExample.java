package com.example.addon.modules;

import com.example.addon.Addon;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;

public class CartPvPModule extends Module {

    private boolean wasShooting = false;

    public CartPvPModule() {
        super(Addon.CATEGORY, "cart-pvp", "Automates Cart PvP actions for bow and crossbow.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null) return;

        handleCrossbowCart();
        handleInstaCart();
    }

    private void handleCrossbowCart() {
        if (mc.player.getMainHandStack().isOf(Items.RAIL)) {
            HitResult hit = mc.crosshairTarget;
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHit = (BlockHitResult) hit;
                if (blockHit.getSide() == Direction.UP) {
                    executeCrossbowCart();
                }
            }
        }
    }

    private void handleInstaCart() {
        boolean isUsingItem = mc.options.useKey.isPressed();
        boolean holdingBow = mc.player.getMainHandStack().isOf(Items.BOW);

        if (holdingBow && wasShooting && !isUsingItem) {
            executeInstaCart();
        }

        wasShooting = holdingBow && isUsingItem;
    }

    private void executeCrossbowCart() {
        FindItemResult tntCart = InvUtils.findInHotbar(Items.TNT_MINECART);
        FindItemResult flint = InvUtils.findInHotbar(Items.FLINT_AND_STEEL);
        FindItemResult crossbow = InvUtils.findInHotbar(Items.CROSSBOW);

        interactBlock();

        if (tntCart.found()) {
            InvUtils.swap(tntCart.slot(), false);
            interactBlock();
        }

        if (flint.found()) {
            InvUtils.swap(flint.slot(), false);
            interactBlock();
        }

        if (crossbow.found()) {
            InvUtils.swap(crossbow.slot(), false);
        }
    }

    private void executeInstaCart() {
        FindItemResult rail = InvUtils.findInHotbar(Items.RAIL);
        FindItemResult tntCart = InvUtils.findInHotbar(Items.TNT_MINECART);

        if (rail.found() && tntCart.found()) {
            InvUtils.swap(rail.slot(), false);
            interactBlock();
            InvUtils.swap(tntCart.slot(), false);
            interactBlock();
        }
    }

    private void interactBlock() {
        HitResult hit = mc.crosshairTarget;
        if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) hit;
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit);
        }
    }
}        .description("The color of the marker.")
        .defaultValue(Color.MAGENTA)
        .build()
    );

    /**
     * The {@code name} parameter should be in kebab-case.
     */
    public ModuleExample() {
        super(AddonTemplate.CATEGORY, "world-origin", "An example module that highlights the center of the world.");
    }

    /**
     * Example event handling method.
     * Requires {@link AddonTemplate#getPackage()} to be setup correctly, otherwise the game will crash whenever the module is enabled.
     */
    @EventHandler
    private void onRender3d(Render3DEvent event) {
        // Create & expand the marker object
        AABB marker = new AABB(BlockPos.ZERO);
        marker = marker.expandTowards(
            scale.get() * marker.getXsize(),
            scale.get() * marker.getYsize(),
            scale.get() * marker.getZsize()
        );

        // Render the marker based on the color setting
        event.renderer.box(marker, color.get(), color.get(), ShapeMode.Both, 0);
    }
}
