package xyz.scyllasrock.ScyUtility.objects.gui;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Data;

@Data
public class MenuItem {

    private final int slot;
    private final Function<Player, ItemStack> item;
    @Nullable
    private final BiConsumer<Player, InventoryClickEvent> clickEvent;


}
