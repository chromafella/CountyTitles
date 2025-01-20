package CountyTitles.countyTitles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class CountyTitles extends JavaPlugin {
    private RegionManager regionManager;
    private Map<Player, Region> playerRegions = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        regionManager = new RegionManager(getConfig());
        Bukkit.getConsoleSender().sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(
                "&aCountyTitles &bv1.2.1 &ahas started up!\n               &fMy very first plugin! \n&dCreated with love, by Brennan Cheatwood"));
        this.getCommand("countytitles").setExecutor(new CountyTitlesCommand(this, regionManager));

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location loc = player.getLocation();
                    Region currentRegion = regionManager.getRegionByLocation(loc);
                    Region previousRegion = playerRegions.get(player);

                    if (currentRegion != null && !currentRegion.equals(previousRegion)) {
                        String titleText = getConfig().getString("regions." + currentRegion.getName() + ".title", "&oAwaiting Title");
                        String subtitleText = getConfig().getString("regions." + currentRegion.getName() + ".subtitle", "&a&o/countytitles subtitle <regionname> <subtitle>");
                        Component titleComponent = titleText.isEmpty() ? Component.empty() : LegacyComponentSerializer.legacyAmpersand().deserialize(titleText);
                        Component subtitleComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(subtitleText);
                        Title title = Title.title(
                                titleComponent,
                                subtitleComponent,
                                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
                        );
                        player.showTitle(title);
                        playerRegions.put(player, currentRegion);
                    } else if (currentRegion == null && previousRegion != null) {
                        playerRegions.remove(player);
                    }
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        getLogger().info("CountyTitles has been disabled!");
    }
}