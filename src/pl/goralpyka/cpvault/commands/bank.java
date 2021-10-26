package pl.goralpyka.cpvault.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.goralpyka.cpvault.Main;
import pl.goralpyka.cpvault.db.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class bank implements CommandExecutor {

    private Main plugin;
    mysql db = new mysql();

    public bank(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("bank").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (args.length != 0 && p.hasPermission("bank.use")){
            String PlayerName = args[0];
            try {
                String query = "SELECT accBalance FROM edvault.users WHERE login = ?";
                PreparedStatement statement = db.connection.prepareStatement(query);
                statement.setString(1, PlayerName);
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    int balance = rs.getInt(1);
                    p.sendMessage(ChatColor.GREEN + PlayerName + "'s balance: "
                            + ChatColor.RED + String.valueOf(balance) + " E$");
                } else
                    p.sendMessage(ChatColor.RED + "There is no player like " + ChatColor.BOLD + PlayerName);

            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (args.length == 0) {

            String PlayerUUID = p.getUniqueId().toString();

            try {
                String query = "SELECT accBalance, login FROM edvault.users WHERE uuid = ?";
                PreparedStatement stmt = db.connection.prepareStatement(query);
                stmt.setString(1, PlayerUUID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()){
                    int balance = rs.getInt(1);
                    String UserName = rs.getString(2);
                    p.sendMessage(ChatColor.GREEN + UserName + "'s balance: "
                            + ChatColor.RED + String.valueOf(balance) + " E$");
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Check usage of this command by typing "
                    + ChatColor.RESET + ChatColor.BOLD + "/help!");
            return true;
        }
        return false;
    }
}
