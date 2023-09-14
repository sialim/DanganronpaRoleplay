package me.sialim.danganronparoleplay.commands;

import dev.majek.hexnicks.HexNicks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender.hasPermission("danganrp.me"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player) sender;
                if(args.length==0){p.sendMessage(ChatColor.RED + "[DRP] Incorrect usage, please do: /me <action>");}
                else
                {
                    String message = String.join(" ", args);
                    p.getServer().broadcastMessage(p.getDisplayName() + " " + ChatColor.GRAY + message);
                }
            }
        }else{sender.sendMessage(ChatColor.RED + "[DRP] Sorry, you do not have permission to perform this command.");}
        return true;
    }
}
