//package ran.nist.first;
//
//import io.papermc.paper.event.entity.EntityMoveEvent;
//import org.bukkit.Bukkit;
//import org.bukkit.Particle;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.plugin.java.JavaPlugin;
//import org.bukkit.scheduler.BukkitRunnable;
//
//public class MyEventListener implements Listener {
//
//    private JavaPlugin plugin;
//
//    public MyEventListener(JavaPlugin plugin) {
//        this.plugin = plugin;
//    }
//
//    @EventHandler
//    public void onPlayerInteract(PlayerInteractEvent event) {
//        Entity entity = event.getPlayer();
//
//        // 创建一个彩色圈的粒子效果
//        new BukkitRunnable() {
//            int count = 0;
//
//            @Override
//            public void run() {
//                if (count >= 5 * 20) {
//                    cancel();
//                    return;
//                }
//
//                entity.getWorld().spawnParticle(Particle.REDSTONE, entity.getLocation().add(0, 1, 0), 1, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 255, 0), 1));
//
//                count++;
//            }
//        }.runTaskTimer(plugin, 0, 1);
//    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    public void onEntityMove(EntityMoveEvent event) {
//        LivingEntity entity = event.getEntity();
//        if (First.lockedEntities.containsValue(entity)) {
//            event.setCancelled(true);
//        }
//    }
//}
