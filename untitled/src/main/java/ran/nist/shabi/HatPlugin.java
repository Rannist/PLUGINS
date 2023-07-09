package ran.nist.shabi;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class HatPlugin implements CommandExecutor {

    private final JavaPlugin plugin;

    public HatPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("hat").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hat")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                PlayerInventory inventory = player.getInventory();

                // 将物品放在玩家头上的代码
                ItemStack handItem = inventory.getItemInMainHand();
                ItemStack helmet = inventory.getHelmet();

                if (handItem != null && !handItem.getType().equals(Material.AIR)) {
                    inventory.setHelmet(handItem);
                    inventory.setItemInMainHand(helmet);

                    player.sendMessage("§a你的物品已移动到头部装备栏！");
                } else {
                    player.sendMessage("§c你手上没有物品！");
                }

                return true;
            } else {
                sender.sendMessage("§c该命令只能由玩家执行！");
            }
        }
        return false;
    }
}
