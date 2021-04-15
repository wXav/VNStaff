package net.vicnix.staff.listener;

import net.vicnix.staff.session.Session;
import net.vicnix.staff.session.SessionManager;
import net.vicnix.staff.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler (priority = EventPriority.NORMAL)
    public void onInventoryClick(PlayerInteractEvent ev) {
        Player player = ev.getPlayer();

        if (ev.getAction().equals(Action.LEFT_CLICK_AIR) || ev.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;

        ItemStack itemStack = ev.getItem();

        if(itemStack == null) return;

        Session session = SessionManager.getInstance().getSession(player.getUniqueId());

        if (session == null || !session.getSessionStorage().isStaff()) return;

        for (ItemStack item : ItemUtils.getStaffContents(session.getSessionStorage().isVanished()).values()) {
            if (!item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) continue;

            session.sendMessage("Action for " + item.getItemMeta().getDisplayName());
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerDropItemEvent(PlayerDropItemEvent ev) {
        Player player = ev.getPlayer();

        Session session = SessionManager.getInstance().getSession(player.getUniqueId());

        if (session == null || !session.getSessionStorage().isStaff()) return;

        ItemStack itemStack = ev.getItemDrop().getItemStack();

        for (ItemStack item : ItemUtils.getStaffContents(session.getSessionStorage().isVanished()).values()) {
            if (!item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) continue;

            ev.setCancelled(true);

            return;
        }
    }

    @EventHandler (priority = EventPriority.NORMAL)
    public void onInventoryClickEvent(InventoryClickEvent ev) {
        if (!(ev.getWhoClicked() instanceof Player)) return;

        Player player = (Player) ev.getWhoClicked();

        Session session = SessionManager.getInstance().getSession(player.getUniqueId());

        if (session == null || !session.getSessionStorage().isStaff()) return;

        ItemStack itemStack = ev.getCurrentItem();

        if (itemStack == null) return;

        for (ItemStack item : ItemUtils.getStaffContents(session.getSessionStorage().isVanished()).values()) {
            if (itemStack.getItemMeta() == null) continue;

            if (!item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) continue;

            ev.setCancelled(true);

            return;
        }
    }
}