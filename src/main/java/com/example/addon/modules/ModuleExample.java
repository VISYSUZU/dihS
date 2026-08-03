package com.example.addon;

import com.example.addon.modules.CartPvPModule;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AddonTemplate extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Cart PvP");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Cart PvP Addon");

        // Registra o nosso módulo dentro do Meteor Client
        Modules.get().add(new CartPvPModule());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }
}                    executeCrossbowCart();
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
