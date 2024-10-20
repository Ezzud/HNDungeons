package gg.horizonnetwork.HNDungeons.commands;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import gg.techtide.tidelib.revamped.abysslibrary.config.wrapper.CommandSettingsWrapper;
import org.bukkit.entity.Player;

public class Dungeons extends TideCommand<HNDungeons, Player> {


    public Dungeons(final HNDungeons plugin) {
        super(plugin, plugin.getSettingsConfig().getCommandSettings("command"), Player.class);

    }

    @Override
    public void execute(CommandContext commandContext) {

    }
}
