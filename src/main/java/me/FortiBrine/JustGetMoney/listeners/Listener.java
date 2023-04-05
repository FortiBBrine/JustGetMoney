package me.FortiBrine.JustGetMoney.listeners;

import me.FortiBrine.JustGetMoney.Main;
import me.fortibrine.justmoney.utils.BalanceManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;

import java.math.BigInteger;
import java.util.Set;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        FileConfiguration config = Main.getMain().getConfig();

        Player player = event.getPlayer();

        for (String key : config.getConfigurationSection("break").getKeys(false)) {
            BigInteger count = new BigInteger( config.getString("break."+key, "0") );

            if (event.getBlock().getType().name().equalsIgnoreCase(key)) {
                BalanceManager.pay(player.getName(), count);

                String message = config.getString("messages.break");

                message = message.replace("%block", key);
                message = message.replace("%money", count.toString());

                player.sendMessage(message);

            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();

        if (!(entity instanceof Player)) {
            return;
        }

        FileConfiguration config = Main.getMain().getConfig();

        Player player = (Player) entity;
        Set<String> canDamageBy = config.getConfigurationSection("can_damage_by").getKeys(false);
        Material type = player.getInventory().getItemInMainHand().getType();

        boolean x = false;

        for (String key : canDamageBy) {
            if (key.equalsIgnoreCase(type.name())) {
                x = true;
                break;
            }
        }

        if (!x) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onKill(EntityDeathEvent event) {
        FileConfiguration config = Main.getMain().getConfig();

        Player player = event.getEntity().getKiller();

        if (player == null) {
            return;
        }

        if (player.getName() == null) {
            return;
        }

        for (String key : config.getConfigurationSection("kill").getKeys(false)) {
            BigInteger count = new BigInteger( config.getString("kill."+key, "0") );

            if (event.getEntityType().name().equalsIgnoreCase(key)) {
                BalanceManager.pay(player.getName(), count);

                String message = config.getString("messages.kill");

                message = message.replace("%mob", key);
                message = message.replace("%money", count.toString());

                player.sendMessage(message);
            }

        }
    }

}
