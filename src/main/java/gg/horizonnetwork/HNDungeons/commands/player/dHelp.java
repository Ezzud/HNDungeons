package gg.horizonnetwork.HNDungeons.commands.player;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.techtide.tidelib.revamped.abysslibrary.command.TideCommand;
import gg.techtide.tidelib.revamped.abysslibrary.command.context.CommandContext;
import org.bukkit.entity.Player;

public class dHelp extends TideCommand<HNDungeons, Player> {

    private final String permission;

    public dHelp(final HNDungeons plugin) {
        super(plugin, plugin.getSettingsConfig().getCommandSettings("command"), Player.class);

        this.permission = plugin.getSettingsConfig().getString("command.permission");
    }

    @Override
    public void execute(CommandContext<Player> context) {

        final Player player = context.getSender();

        if (!player.hasPermission(this.permission)) {
            this.plugin.getMessageCache().sendMessage(player, "messages.no-permission");
            return;
        }

        this.plugin.getMessageCache().sendMessage(player, "messages.help");
    }
}
