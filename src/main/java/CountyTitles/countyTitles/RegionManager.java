package CountyTitles.countyTitles;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {
    private final Map<String, Region> regions = new HashMap<>();
    private final FileConfiguration config;

    public RegionManager(FileConfiguration config) {
        this.config = config;
        loadRegions(config);
    }

    public void addRegion(String name) {
        regions.put(name, new Region(name));
    }

    public void setRegionPos1(String name, Location pos1) {
        Region region = regions.get(name);
        if (region != null) {
            region.setPos1(pos1);
            config.set("regions." + name + ".pos1", pos1);
        }
    }

    public void setRegionPos2(String name, Location pos2) {
        Region region = regions.get(name);
        if (region != null) {
            region.setPos2(pos2);
            config.set("regions." + name + ".pos2", pos2);
        }
    }

    public void setRegionTitle(String name, String title) {
        Region region = regions.get(name);
        if (region != null) {
            region.setTitle(title);
            config.set("regions." + name + ".title", title);
        }
    }

    public void setRegionSubtitle(String name, String subtitle) {
        Region region = regions.get(name);
        if (region != null) {
            region.setSubtitle(subtitle);
            config.set("regions." + name + ".subtitle", subtitle);
        }
    }

    public Region getRegionByLocation(Location location) {
        for (Region region : regions.values()) {
            if (region.contains(location)) {
                return region;
            }
        }
        return null;
    }

    public Iterable<String> getRegionNames() {
        return regions.keySet();
    }

    public boolean hasIncompleteRegions() {
        for (Region region : regions.values()) {
            if (region.getPos1() == null || region.getPos2() == null) {
                return true;
            }
        }
        return false;
    }

    public void reloadRegions(Configuration config) {
        regions.clear();
        loadRegions(config);
    }

    public void removeRegion(String name) {
        regions.remove(name);
        config.set("regions." + name, null);
    }

    private void loadRegions(Configuration config) {
        ConfigurationSection regionsSection = config.getConfigurationSection("regions");
        if (regionsSection != null) {
            for (String regionName : regionsSection.getKeys(false)) {
                String title = regionsSection.getString(regionName + ".title", "&oAwaiting Title");
                String subtitle = regionsSection.getString(regionName + ".subtitle", "&a&o/countytitles subtitle <regionname> <subtitle>");
                Location pos1 = regionsSection.getLocation(regionName + ".pos1");
                Location pos2 = regionsSection.getLocation(regionName + ".pos2");

                Region region = new Region(regionName, title, subtitle);
                region.setPos1(pos1);
                region.setPos2(pos2);
                regions.put(regionName, region);
            }
        }
    }
}