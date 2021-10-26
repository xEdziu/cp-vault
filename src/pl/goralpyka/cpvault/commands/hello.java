package pl.goralpyka.cpvault.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.goralpyka.cpvault.Main;

public class hello implements CommandExecutor {

    private Main plugin;

    public hello(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("hello").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player p = (Player) sender;

        String playerName = p.getName();

        p.sendMessage("Hi "+playerName+"!");

        return false;
    }
}
