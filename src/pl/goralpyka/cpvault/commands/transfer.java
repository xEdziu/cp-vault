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

public class transfer implements CommandExecutor {

    private Main plugin;
    mysql db = new mysql();

    public transfer(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("transfer").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("Only players can execute this command!");
            return true;
        }


        Player p = (Player) sender;
        String userUUID = p.getUniqueId().toString();
        String userName = p.getName();


        if (args.length == 2){

            if (userName.equals(args[1])){
                p.sendMessage(ChatColor.RED + "You can't transfer money to yourself!");
                return true;
            }

            if (Main.isNumeric(args[0])){

                int amount = Integer.parseInt(args[0]);

                if (amount <= 0){
                    p.sendMessage(ChatColor.RED + "You can't transfer 0 or negative number of money!");
                    return true;
                }

                String queryCollect = "SELECT accBalance FROM edvault.users WHERE uuid = ?";
                try {

                    PreparedStatement preparedStatementCollect = db.connection.prepareStatement(queryCollect);
                    preparedStatementCollect.setString(1, userUUID);
                    ResultSet rs = preparedStatementCollect.executeQuery();
                    if (rs.next()){
                        int userBalance = rs.getInt(1);

                        if (userBalance >= Integer.parseInt(args[0])){

                            String query = "SELECT id FROM edvault.users WHERE login = ?";
                            try {

                                PreparedStatement stmt = db.connection.prepareStatement(query);
                                stmt.setString(1, args[1]);
                                ResultSet resultSet = stmt.executeQuery();

                                if (resultSet.next()){

                                    try {

                                        String queryTransferRemove = "UPDATE users SET accBalance = accBalance - ? WHERE" +
                                                " uuid = ?";

                                        PreparedStatement psRm = db.connection.prepareStatement(queryTransferRemove);
                                        psRm.setInt(1, amount);
                                        psRm.setString(2, userUUID);
                                        int rsRm = psRm.executeUpdate();

                                        if (rsRm == 1){

                                            try {

                                                String queryTransferAdd = "UPDATE users SET accBalance = " +
                                                        "accBalance + ? WHERE login = ?";
                                                PreparedStatement psAd = db.connection.prepareStatement(queryTransferAdd);
                                                psAd.setInt(1, amount);
                                                psAd.setString(2, args[1]);

                                                int rsAd = psAd.executeUpdate();

                                                if (rsAd == 1){
                                                    p.sendMessage(ChatColor.RED + String.valueOf(amount) + "E$ " +
                                                            ChatColor.GREEN + "transferred successfully to user " +
                                                            ChatColor.BOLD + args[1]);
                                                }

                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }

                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }

                                } else {

                                    p.sendMessage(ChatColor.RED + "There is no player like " + ChatColor.BOLD + args[1]);
                                    return true;

                                }

                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            sender.sendMessage(ChatColor.RED + "You don't have enough money!");
                            return true;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Amount is not a numeric or integer value!");
                return true;
            }


        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Check usage of this command by typing "
                    + ChatColor.RESET + ChatColor.BOLD +"/help!");
            return true;
        }

        return false;
    }
}
