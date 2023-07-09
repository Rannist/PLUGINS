package ran.nist.first;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Second extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getCommand("snstick").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("snstick")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                ItemStack stick = new ItemStack(Material.STICK);
                ItemMeta meta = stick.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "傻鸟木棍");
                stick.setItemMeta(meta);

                player.getInventory().addItem(stick);
                player.sendMessage(ChatColor.YELLOW + "你获得了一个傻鸟木棍！");
            } else {
                sender.sendMessage(ChatColor.RED + "只有玩家可以执行此命令！");
            }
            return true;
        }
        return false;
    }
}
