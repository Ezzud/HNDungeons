package gg.horizonnetwork.HNDungeons.commands;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.commands.admin.dDelete;
import gg.horizonnetwork.HNDungeons.commands.admin.dReload;
import gg.horizonnetwork.HNDungeons.commands.player.*;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler implements CommandExecutor {
    public HNDungeons plugin = HNDungeons.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        if(player == null)
            return false;
        if(args.length == 0) {
            new dHelp(player, args);
        }

        List<String> disabledCommands = plugin.getConfig().getStringList("disabled_commands");
        if(disabledCommands.contains(args[0])) {
            ChatUtil.sendPathMessage(player, "command.disabled");
            return false;
        }

        switch(args[0]) {
            case "help":
                new dHelp(player, args);
                break;
            case "join":
                new dJoin(player, args);
                break;
            case "reload":
                new dReload(player, args);
                break;
            case "end":
                new dEnd(player, args);
                break;
            case "tp":
                new dTp(player, args);
                break;
            case "info":
                new dInfo(player, args);
                break;
            case "invite":
                new dInvite(player, args);
                break;
            case "leave":
                new dLeave(player, args);
                break;
            case "joinparty":
                new dJoinParty(player, args);
                break;
            case "kick":
                new dKick(player, args);
                break;
            case "delete":
                new dDelete(player, args);
                break;
            default:
                ChatUtil.sendPathMessage(player, "command.not-found");
        }


        return true;
    }
}
