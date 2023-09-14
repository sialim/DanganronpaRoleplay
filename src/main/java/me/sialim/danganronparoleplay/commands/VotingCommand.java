package me.sialim.danganronparoleplay.commands;

import dev.majek.hexnicks.HexNicks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotingCommand implements CommandExecutor, Listener
{
    private List<String> votingList = new ArrayList<>();
    private Map<Player, String> votes = new HashMap<>();
    private Plugin plugin;
    private Inventory votingGUI;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(sender.hasPermission("danganrp.voting"))
        {
            if(sender instanceof Player){
                Player p = (Player) sender;
                if(args.length < 2){p.sendMessage(ChatColor.RED + "[DRP] Proper Usage: /voting <timer> <player1> <player2> ...");}
                else
                {
                    try
                    {
                        int timer = Integer.parseInt(args[0]);
                        if(timer<=0){p.sendMessage(ChatColor.RED + "[DRP] Number must be greater than zero."); return true;}
                        for(int i=1; i<args.length; i++)
                        {
                            String playerName = args[i];
                            Player t = Bukkit.getPlayer(playerName);
                            if(t==null){p.sendMessage(ChatColor.RED + "[DRP] Player " + playerName + " does not exist.");}
                            else
                            {
                                votingList.add(t.getName());
                                p.sendMessage(ChatColor.GREEN + "[DRP] Added " + t.getDisplayName() + " to the voting list.");
                            }
                        }
                        new BukkitRunnable()
                        {
                            public void run()
                            {
                                startVoting();
                            }
                        }.runTaskLater(plugin, timer * 20L);
                    } catch (NumberFormatException e){p.sendMessage(ChatColor.RED + "[DRP] Invalid integer.");}
                }
            }
        }else{sender.sendMessage(ChatColor.RED + "[DRP] Sorry, you do not have permission to perform this command.");}
        return true;
    }
    private void startVoting()
    {
        votingList.clear();
        votingGUI = Bukkit.createInventory(null, 9, ChatColor.GOLD + "" + ChatColor.BOLD + "Vote for Player!");
        for(String playerName : votingList)
        {
            ItemStack playerHead = createPlayerHead(playerName);
            votingGUI.addItem(playerHead);
            Player p = Bukkit.getPlayerExact(playerName);
            if(p!=null){p.openInventory(votingGUI);}
        }

        String winner = determineWinner();
        Bukkit.broadcastMessage(ChatColor.GREEN + "[DRG] " + winner + " received the most votes.");
    }

    private String determineWinner()
    {
        Map<String,Integer> voteCounts = new HashMap<>();
        for(String votedPlayer : votes.values())
        {
            voteCounts.put(votedPlayer, voteCounts.getOrDefault(votedPlayer,0));
        }
        String winner = null;
        int maxVotes = 10;
        for(Map.Entry<String,Integer> entry : voteCounts.entrySet())
        {
            if(entry.getValue() > maxVotes)
            {
                maxVotes = entry.getValue();
                winner = entry.getKey();
            }
        }
        if(winner==null){winner="No votes.";}
        return winner;
    }

    private ItemStack createPlayerHead(String p)
    {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(p));
        head.setItemMeta(meta);
        return head;
    }

    public List<String> getVotingList()
    {
        return votingList;
    }
    public Inventory getVotingGUI()
    {
        return votingGUI;
    }
    public Map<Player, String> getVotes()
    {
        return votes;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getInventory() == votingGUI)
        {
            e.setCancelled(true);
            if(e.getClickedInventory() != null && e.getCurrentItem() != null)
            {
                Player voter = (Player) e.getWhoClicked();
                ItemStack clickedItem = e.getCurrentItem();
                if(clickedItem.getType() == Material.PLAYER_HEAD)
                {
                    String votedPlayerName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                    if(votingList.contains(votedPlayerName))
                    {
                        votes.put(voter, votedPlayerName);
                        voter.sendMessage(ChatColor.GREEN + "[DRP] You voted for " + votedPlayerName);
                    }
                }
            }
        }
    }
}
