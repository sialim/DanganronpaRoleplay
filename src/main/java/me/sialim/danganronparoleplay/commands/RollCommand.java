package me.sialim.danganronparoleplay.commands;

import dev.majek.hexnicks.HexNicks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.UUID;

public class RollCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender.hasPermission("danganrp.roll"))
        {
            if(sender instanceof Player)
            {
                Player p = (Player) sender;
                if(args.length==0){p.sendMessage(ChatColor.RED + "[DRP] Incorrect usage, please do: /roll <integer>");}
                else
                {
                    try
                    {
                        int rollMax = Integer.parseInt(args[0]);
                        if(rollMax<=1)
                        {
                            p.sendMessage(ChatColor.RED + "[DRP] Number must be greater than zero.");
                        }

                        int rollResult = new Random().nextInt(rollMax) + 1;
                        p.sendMessage( p.getDisplayName() + " " + ChatColor.GRAY + " rolled a " + ChatColor.WHITE + rollResult + ChatColor.GRAY + "[1-" + rollMax + "]");
                    }
                    catch (NumberFormatException e){p.sendMessage(ChatColor.RED + "[DRP] Invalid integer.");}
                }
            }
        }else{sender.sendMessage(ChatColor.RED + "[DRP] Sorry, you do not have permission to perform this command.");}
        return true;
    }
}
