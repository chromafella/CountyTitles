package CountyTitles.countyTitles;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;

public class RegionEnterListener implements Listener {
    private final CountyTitles plugin;
    private final RegionManager regionManager;
    private final Map<Player, BukkitRunnable> playerTasks = new HashMap<>();

    public RegionEnterListener(CountyTitles plugin, RegionManager regionManager) {
        this.plugin = plugin;
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Region region = regionManager.getRegionByLocation(player.getLocation());

        if (region != null) {
            String actionBarMessage = region.getActionbarMessage();
            sendActionBarMessage(player, actionBarMessage.replace("{region}", region.getName()));
        } else {
            cancelTask(player);
        }
    }

    private void sendActionBarMessage(Player player, String message) {
        cancelTask(player);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendActionBar(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
            }
        };
        task.runTaskTimer(plugin, 0, 20);
        playerTasks.put(player, task);
    }

    private void cancelTask(Player player) {
        BukkitRunnable task = playerTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }
}