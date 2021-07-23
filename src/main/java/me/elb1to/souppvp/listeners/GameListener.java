package me.elb1to.souppvp.listeners;

import me.elb1to.souppvp.SoupPvP;
import me.elb1to.souppvp.user.User;
import me.elb1to.souppvp.user.ui.kit.KitSelectionMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.elb1to.souppvp.utils.PlayerUtil.*;

/**
 * Created by Elb1to
 * Project: SoupPvP
 * Date: 5/7/2021 @ 1:28 PM
 */
public class GameListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        resetPlayer(player);
        resetHotbar(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) {
            return;
        }

        User user = SoupPvP.getInstance().getUserManager().getByUuid(player.getUniqueId());
        if (user == null) return;

        if (event.getItem().equals(KIT_SELECTOR)) {
            new KitSelectionMenu().openMenu(player);
        } else if (event.getItem().equals(EVENT_HOSTING)) {
            player.sendMessage("Open Events Menu");
        } else if (event.getItem().equals(PLAYER_PERKS)) {
            player.sendMessage("Open Perks Menu");
        } else if (event.getItem().equals(PREVIOUS_KIT)) {
            SoupPvP.getInstance().getKitManager().getKitByName(user.getCurrentKitName()).equipKit(player);
        } else if (event.getItem().getType().equals(Material.SKULL_ITEM)) {
            sendStats(player, user);
        }
    }

    @EventHandler
    public void onSoupConsumption(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getItemInHand().getType() == Material.MUSHROOM_SOUP && player.getHealth() < 19.5) {
            player.setHealth(Math.min(player.getHealth() + 7.0, 20.0));
            player.getItemInHand().setType(Material.BOWL);
            player.updateInventory();
        }
    }

    @EventHandler
    public void onBowlDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.BOWL) {
            event.getItemDrop().remove();
        }
    }
}
