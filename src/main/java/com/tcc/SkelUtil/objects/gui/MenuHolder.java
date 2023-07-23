package com.tcc.SkelUtil.objects.gui;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

/**
 * Represents a menu holder.
 *
 * @author Negative
 * @version 2.0.0
 */
public interface MenuHolder<T extends MenuBase> extends InventoryHolder {

    void onOpen(@Nonnull Player player, @Nonnull InventoryOpenEvent event);

    void onClose(@Nonnull Player player, @Nonnull InventoryCloseEvent event);

    void onClick(InventoryClickEvent event);

    @Nonnull
    T getMenu();

}
