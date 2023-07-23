package com.tcc.SkelUtil.objects.gui;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents the base functions of a menu.
 *
 * @author Negative
 * @since 2.0.0
 */
public interface MenuBase {

    void setItem(int index, @Nonnull Function<Player, ItemStack> function);

    void setItemClickEvent(int index, @Nonnull Function<Player, ItemStack> function, @Nullable BiConsumer<Player, InventoryClickEvent> clickFunction);

    void addItem(@Nonnull Function<Player, ItemStack> function);

    void addItemClickEvent(@Nonnull Function<Player, ItemStack> function, @Nullable BiConsumer<Player, InventoryClickEvent> clickFunction);

    void clearSlot(int index);

    void refresh(@Nonnull Player player);

    void open(@Nonnull Player player);

    void onOpen(BiConsumer<Player, InventoryOpenEvent> function);

    void onClose(BiConsumer<Player, InventoryCloseEvent> function);

    void onInventoryClick(BiConsumer<Player, InventoryClickEvent> function);
}
