package CountyTitles.countyTitles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import java.util.Arrays;

public class CountyTitlesCommand implements CommandExecutor {
    private final CountyTitles plugin;
    private final RegionManager regionManager;

    public CountyTitlesCommand(CountyTitles plugin, RegionManager regionManager) {
        this.plugin = plugin;
        this.regionManager = regionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &fv1.2.1-SNAPSHOT\n&bAvailable commands: &a/countytitles reload " +
                    "\n&a/countytitles addregion " +
                    "\n&a/countytitles setpos1 " +
                    "\n&a/countytitles setpos2 " +
                    "\n&a/countytitles title " +
                    "\n&a/countytitles subtitle" +
                    "\n&a/countytitles info " +
                    "\n&a/countytitles list" +
                    "\n&a/countytitles save" +
                    "\n&a/countytitles removeregion" +
                    "\n&a/countytitles regioninfo"));
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &fPlugin version 1.2.1-SNAPSHOT\n&dHighly experimental release!\n&aCreated by Brennan Cheatwood"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("countytitles.reload")) {
                plugin.reloadConfig();
                regionManager.reloadRegions(plugin.getConfig());
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aConfiguration successfully reloaded."));
                return true;
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cYou do not have permission to use this command."));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("addregion")) {
            if (args.length == 2) {
                regionManager.addRegion(args[1]);
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aRegion: &f" + args[1] + " &asuccessfully added."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cUsage: /countytitles addregion <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setpos1")) {
            if (args.length == 2 && sender instanceof Player) {
                Player player = (Player) sender;
                regionManager.setRegionPos1(args[1], player.getLocation());
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aPosition 1 for region: &f" + args[1] + " &aset."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cUsage: /countytitles setpos1 <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setpos2")) {
            if (args.length == 2 && sender instanceof Player) {
                Player player = (Player) sender;
                regionManager.setRegionPos2(args[1], player.getLocation());
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aPosition 2 for region: &f" + args[1] + " &aset."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cUsage: /countytitles setpos2 <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("title")) {
            if (args.length >= 3) {
                String regionName = args[1];
                String title = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                if (title.equals("''")) {
                    title = "";
                }
                regionManager.setRegionTitle(regionName, title);
                plugin.saveConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aTitle for region: &f" + regionName + " &aset to &f" + title + "&a."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cUsage: /countytitles title <regionname> <title>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("subtitle")) {
            if (args.length >= 3) {
                String regionName = args[1];
                String subtitle = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                regionManager.setRegionSubtitle(regionName, subtitle);
                plugin.saveConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aSubtitle for region: &f" + regionName + " &aset to &f" + subtitle + "&a."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cUsage: /countytitles subtitle <regionname> <subtitle>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            StringBuilder regionList = new StringBuilder("&bRegions:\n");
            for (String regionName : regionManager.getRegionNames()) {
                regionList.append("&a- ").append(regionName).append("\n");
            }
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(regionList.toString()));
            return true;
        }

        if (args[0].equalsIgnoreCase("save")) {
            if (regionManager.hasIncompleteRegions()) {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cYou have incomplete regions. Please set both positions for all regions before saving."));
            } else {
                plugin.saveConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aAll regions & titles saved! &f(config.yml updated)"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("removeregion")) {
            if (args.length == 2) {
                regionManager.removeRegion(args[1]);
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aRegion: &f" + args[1] + " &asuccessfully removed."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cUsage: /countytitles removeregion <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("regioninfo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Region region = regionManager.getRegionByLocation(player.getLocation());
                if (region != null) {
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &aYou are currently in region: &f" + region.getName() + "&a."));
                } else {
                    sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cYou are not in any region."));
                }
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &cThis command can only be used by players."));
            }
            return true;
        }

        sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&f&lCounty&b&lTitles &7&l>> &c&lHey! &7Sorry, that's an &cUnknown command.\n&bAvailable commands: &a/countytitles <reload | addregion | setpos1 | setpos2 | title | subtitle | info | list | save | removeregion | regioninfo>"));
        return true;
    }
}