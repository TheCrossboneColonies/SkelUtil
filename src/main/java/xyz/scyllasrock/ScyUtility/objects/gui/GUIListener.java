package xyz.scyllasrock.ScyUtility.objects.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class GUIListener implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUIHolder) {
            GUIHolder base = (GUIHolder) holder;
            base.onClick(event);
        }

        if (holder instanceof HopperGUIHolder) {
            HopperGUIHolder base = (HopperGUIHolder) holder;
            base.onClick(event);
        }

        if (holder instanceof DropperGUIHolder) {
            DropperGUIHolder base = (DropperGUIHolder) holder;
            base.onClick(event);
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUIHolder) {
            GUIHolder base = (GUIHolder) holder;
            base.onClose((Player) event.getPlayer(), event);
        }

        if (holder instanceof HopperGUIHolder) {
            HopperGUIHolder base = (HopperGUIHolder) holder;
            base.onClose((Player) event.getPlayer(), event);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof GUIHolder) {
            GUIHolder base = (GUIHolder) holder;
            base.onOpen((Player) event.getPlayer(), event);
        }

        if (holder instanceof HopperGUIHolder) {
            HopperGUIHolder base = (HopperGUIHolder) holder;
            base.onOpen((Player) event.getPlayer(), event);
        }
    }
}