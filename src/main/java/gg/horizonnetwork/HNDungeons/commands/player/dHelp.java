package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import org.bukkit.entity.Player;

public class dHelp extends TideCommand {

    public dHelp(final HNDungeons plugin) {
        super(plugin, plugin.getSettingsConfig().getCommandSettings("command"), Player.class);

    }

    @Override
    public void execute(CommandContext commandContext) {

    }
}
