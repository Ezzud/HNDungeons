package gg.horizonnetwork.HNDungeons.commands;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.managers.InstanceManager;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TabCompletion implements TabCompleter {
    HNDungeons plugin = HNDungeons.getInstance();

    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("dungeons") && args.length < 2){
            if(sender instanceof Player player){
                List<String> list = new ArrayList<>();
                List<String> disabledCommands = plugin.getConfig().getStringList("disabled_commands");
                String[] commands = new String[]{"help", "join", "leave", "invite", "kick", "end", "joinparty", "tp", "info"};
                for(String command : commands){
                    if(!disabledCommands.contains(command)){
                        list.add(command);
                    }
                }
                if(player.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("command.permission")))) {
                    list.add("reload");
                    list.add("delete");
                }
                return list;
            }
        } else {
            switch (args[0].toLowerCase()){
                case "join":
                    Set<String> maps = Objects.requireNonNull(plugin.getConfig().getConfigurationSection("dungeons")).getKeys(false);
                    return new ArrayList<>(maps);
                case "joinparty":
                case "invite":
                case "kick":
                    List<String> players = new ArrayList<>();
                    for(Player player : plugin.getServer().getOnlinePlayers()){
                        players.add(player.getName());
                    }
                    return players;
                case "delete":
                    List<String> ids = new ArrayList<>();
                    InstanceManager m = plugin.getInstanceManager();
                    List<DungeonInstance> d = m.getInstances();
                    for(DungeonInstance di : d) {
                        ids.add(di.getId().toString());
                    }
                    return ids;
                default:
                    return new ArrayList<>();
            }
        }
        return null;
    }
}
