package ran.nist.first;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class First extends JavaPlugin implements Listener {

    private Map<UUID, Location> lockedLocations;

    @Override
    public void onEnable() {
        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);

        // 初始化锁定位置的映射
        lockedLocations = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("weapon") && sender instanceof Player) {
            Player player = (Player) sender;

            // 给予特殊武器
            ItemStack weapon = new ItemStack(Material.STICK);
            ItemMeta meta = weapon.getItemMeta();
            meta.setDisplayName("§6炫酷木棍");
            meta.setLore(Collections.singletonList("§7一个炫酷的武器！"));
            weapon.setItemMeta(meta);
            player.getInventory().addItem(weapon);
            player.sendMessage("§a你获得了炫酷木棍！");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon.getType() == Material.STICK && weapon.getItemMeta().getDisplayName().equals("§6炫酷木棍")) {
            Location lockedLocation = entity.getLocation();

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;

                // 创建一个计时器，在锁定持续时间结束后取消锁定
                BukkitRunnable lockTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        lockedLocations.remove(entity.getUniqueId());
                        player.sendMessage("§a锁定时间已结束，实体已解除锁定！");
                    }
                };
                lockTask.runTaskLater(this, 100); // 持续5秒钟

                player.sendMessage("§a你锁定了实体，它将保持在原地，持续5秒钟！");

                // 创建环状粒子特效，显示剩余的锁定时间
                createParticleRing(lockedLocation, 5);

                // 将被锁定的实体添加到锁定位置的映射中
                lockedLocations.put(entity.getUniqueId(), lockedLocation);

                // 监听玩家移动事件，如果被锁定的玩家移动，则将其传送回被锁定时的位置
                Bukkit.getPluginManager().registerEvents(new Listener() {
                    @EventHandler
                    public void onPlayerMove(PlayerMoveEvent e) {
                        Player movedPlayer = e.getPlayer();
                        if (movedPlayer.equals(player) && lockedLocations.containsKey(movedPlayer.getUniqueId())) {
                            Location originalLocation = lockedLocations.get(movedPlayer.getUniqueId());
                            if (!originalLocation.equals(movedPlayer.getLocation())) {
                                movedPlayer.teleport(originalLocation);
                            }
                        }
                    }
                }, this);
            }
        }
    }

    private void createParticleRing(Location location, int duration) {
        double radius = 1.5; // 环的半径
        int particles = 100; // 粒子数量

        BukkitRunnable particleTask = new BukkitRunnable() {
            double angle = 0;
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= duration * 20) {
                    cancel();
                    return;
                }

                angle += Math.PI / particles;

                for (int i = 0; i < particles; i++) {
                    double x = location.getX() + radius * Math.cos(angle * i);
                    double z = location.getZ() + radius * Math.sin(angle * i);
                    Location particleLoc = new Location(location.getWorld(), x, location.getY(), z);

                    particleLoc.getWorld().spawnParticle(Particle.REDSTONE, particleLoc, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 0, 0), 1));
                }

                ticks++;
            }
        };
        particleTask.runTaskTimer(this, 0, 1);
    }
}
