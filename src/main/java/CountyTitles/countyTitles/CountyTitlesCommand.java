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
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&b[CountyTitles] &fv1.2.0\n&bAvailable commands: &a/countytitles <reload | addregion | setpos1 | setpos2 | title | subtitle | info | list | finish | removeregion>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&bCountyTitles Plugin version 1.2.0\n&dHighly experimental release!\n&aCreated by Brennan Cheatwood"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("countytitles.reload")) {
                plugin.reloadConfig();
                regionManager.reloadRegions(plugin.getConfig());
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aCountyTitles configuration reloaded."));
                return true;
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou do not have permission to use this command."));
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("addregion")) {
            if (args.length == 2) {
                regionManager.addRegion(args[1]);
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aRegion: &f" + args[1] + " &asuccessfully added."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /countytitles addregion <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setpos1")) {
            if (args.length == 2 && sender instanceof Player) {
                Player player = (Player) sender;
                regionManager.setRegionPos1(args[1], player.getLocation());
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aPosition 1 for region: &f" + args[1] + " &aset."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /countytitles setpos1 <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setpos2")) {
            if (args.length == 2 && sender instanceof Player) {
                Player player = (Player) sender;
                regionManager.setRegionPos2(args[1], player.getLocation());
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aPosition 2 for region: &f" + args[1] + " &aset."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /countytitles setpos2 <regionname>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("title")) {
            if (args.length >= 3) {
                String regionName = args[1];
                String title = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                regionManager.setRegionTitle(regionName, title);
                plugin.saveConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aTitle for region: &f" + regionName + " &aset to &f" + title + "&a."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /countytitles title <regionname> <title>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("subtitle")) {
            if (args.length >= 3) {
                String regionName = args[1];
                String subtitle = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                regionManager.setRegionSubtitle(regionName, subtitle);
                plugin.saveConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aSubtitle for region: &f" + regionName + " &aset to &f" + subtitle + "&a."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /countytitles subtitle <regionname> <subtitle>"));
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

        if (args[0].equalsIgnoreCase("finish")) {
            if (regionManager.hasIncompleteRegions()) {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cYou have incomplete regions. Please set both positions for all regions before finishing."));
            } else {
                plugin.saveConfig();
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aRegion configuration saved."));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("removeregion")) {
            if (args.length == 2) {
                regionManager.removeRegion(args[1]);
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&aRegion: &f" + args[1] + " &asuccessfully removed."));
            } else {
                sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&cUsage: /countytitles removeregion <regionname>"));
            }
            return true;
        }

        sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize("&c&lHey! &7Sorry, that's an &cUnknown command.\n&bAvailable commands: &a/countytitles <reload | addregion | setpos1 | setpos2 | title | subtitle | info | list | finish | removeregion>"));
        return true;
    }
}