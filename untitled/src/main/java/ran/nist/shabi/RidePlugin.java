package ran.nist.shabi;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class RidePlugin extends JavaPlugin implements CommandExecutor, Listener {

    private Set<Player> mountedPlayers;

    @Override
    public void onEnable() {
        getCommand("ride").setExecutor(this);
        new HatPlugin(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        mountedPlayers = new HashSet<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ride")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // 创建特殊鞍的物品
                ItemStack saddle = new ItemStack(Material.SADDLE);
                // 设置鞍的显示名称
                saddle.getItemMeta().setDisplayName("§a特殊鞍");

                // 将特殊鞍给予玩家
                PlayerInventory inventory = player.getInventory();
                inventory.addItem(saddle);

                player.sendMessage("§a你获得了特殊鞍！");
                return true;
            } else {
                sender.sendMessage("§c该命令只能由玩家执行！");
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (player.getItemInHand().getType() == Material.SADDLE) {
            if (entity instanceof Player) {
                Player target = (Player) entity;
                if (mountedPlayers.contains(player)) {
                    player.sendMessage("§c你已经坐在其他玩家的头上了！");
                    return;
                }
                mountedPlayers.add(player);
                target.addPassenger(player);
                player.sendMessage("§a你坐在玩家 " + target.getName() + " 的头上！");
            }
        }
    }

    @EventHandler
    public void onPlayerDismount(org.bukkit.event.player.PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (mountedPlayers.contains(player) && player.getVehicle() == null) {
            mountedPlayers.remove(player);
        }
    }
}
