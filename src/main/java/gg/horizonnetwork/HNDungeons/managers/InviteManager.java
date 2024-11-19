package gg.horizonnetwork.HNDungeons.managers;

import gg.horizonnetwork.HNDungeons.HNDungeons;
import gg.horizonnetwork.HNDungeons.api.DungeonInstance;
import gg.horizonnetwork.HNDungeons.api.DungeonPlayer;
import gg.horizonnetwork.HNDungeons.utils.ChatUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InviteManager {

    private HNDungeons plugin;

    public InviteManager(HNDungeons dungeon){
        this.plugin = dungeon;
    }

    public boolean invited(DungeonPlayer invitedPlayer, DungeonPlayer host) {
        return false;
    }

    public void send(DungeonPlayer host, DungeonPlayer invitedPlayer) {
        DungeonInstance i = plugin.getInstanceManager().getPlayerInstance(host);
        if(i == null) {
            ChatUtil.sendMessage(host.getPlayer(), "&cInstance not found");
            return;
        }
        List<String> l = Objects.requireNonNull(ConfigManager.getInvites()).getStringList(i.getId().toString());
        l.add(invitedPlayer.getOfflinePlayer().getUniqueId().toString());
        ConfigManager.getInvites().set(i.getId().toString(), l);
        try {
            File f = new File("plugins/HN-Dungeons/invites.yml");
            ConfigManager.getInvites().save(f);
            ConfigManager.saveInvites();
        } catch(IOException e){
            e.printStackTrace();
        }
        if(invitedPlayer.getPlayer() != null) {
            this.sendClickableMessage(invitedPlayer.getPlayer(), "&e&l" + host.getPlayer().getName() + " &6sent you an invite to his instance!" + " &a&l[ACCEPT]&r", "dungeons joinparty " + i.getId().toString());
        }
        ChatUtil.sendMessage(host.getPlayer(), "&aYou sent an invite to " + invitedPlayer.getPlayer().getName());
    }

    public void sendClickableMessage(Player player, String message, String command) {
        // Make a new component (Bungee API).
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message)));
        // Add a click event to the component.
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        // Send it!
        player.spigot().sendMessage(component);
    }

    public void cancel(DungeonPlayer host, DungeonPlayer invitedPlayer) {
        DungeonInstance i = host.getInstance();
        if(i == null) {
            ChatUtil.sendMessage(host.getPlayer(), "&cInstance not found");
            return;
        }
        List<String> l = Objects.requireNonNull(ConfigManager.getInvites()).getStringList(i.getId().toString());
        l.remove(invitedPlayer.getOfflinePlayer().getUniqueId().toString());
        ConfigManager.getInvites().set(i.getId().toString(), l);
        try {
            File f = new File("plugins/HN-Dungeons/invites.yml");
            ConfigManager.getInvites().save(f);
            ConfigManager.saveInvites();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public int accept(DungeonPlayer host, DungeonPlayer invitedPlayer) {
        DungeonInstance i = host.getInstance();
        if(i == null) {
            ChatUtil.sendMessage(invitedPlayer.getPlayer(), "&cInstance not found");
            return -1;
        }
        /*
        List<String> l = Objects.requireNonNull(ConfigManager.getInvites()).getStringList(i.getId().toString());
        if(!l.contains(invitedPlayer.getOfflinePlayer().getUniqueId().toString())){
            ChatUtil.sendMessage(invitedPlayer.getPlayer(), "&cInvite not found");
            return -2;
        }
        */
        InstanceManager m = plugin.getInstanceManager();
        if(m.isOnInstance(invitedPlayer)) {
            ChatUtil.sendMessage(host.getPlayer(), "&cYou are already on a party");
            return -3;
        }
        i.getParty().add(invitedPlayer);
        plugin.getInstanceManager().getStorage().save(i);
        i.teleportPlayerIn(invitedPlayer);
        this.cancel(host, invitedPlayer);
        if(invitedPlayer.getPlayer() != null) {
            ChatUtil.sendMessage(invitedPlayer.getPlayer(), "&aYou joined the party of &e" + host.getOfflinePlayer().getName());
        }
        ChatUtil.sendPartyMessage(i, "&e" + invitedPlayer.getPlayer().getName() + " &ajoined the party");
        return 1;
    }
}
