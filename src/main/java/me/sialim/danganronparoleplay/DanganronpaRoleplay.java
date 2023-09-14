package me.sialim.danganronparoleplay;

import me.sialim.danganronparoleplay.commands.MeCommand;
import me.sialim.danganronparoleplay.commands.NonRpCommand;
import me.sialim.danganronparoleplay.commands.RollCommand;
import me.sialim.danganronparoleplay.commands.VotingCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DanganronpaRoleplay extends JavaPlugin {

    @Override
    public void onEnable()
    {
        getCommand("me").setExecutor(new MeCommand());
        getCommand("roll").setExecutor(new RollCommand());
        getCommand("b").setExecutor(new NonRpCommand());
        getCommand("voting").setExecutor(new VotingCommand());
        getServer().getPluginManager().registerEvents(new VotingCommand(),this);
    }
}
