package pl.goralpyka.cpvault;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pl.goralpyka.cpvault.commands.bank;
import pl.goralpyka.cpvault.commands.hello;
import pl.goralpyka.cpvault.commands.transfer;
import pl.goralpyka.cpvault.db.mysql;
import pl.goralpyka.cpvault.gui.gig;
import pl.goralpyka.cpvault.gui.mainGui;
import pl.goralpyka.cpvault.gui.shop;

public class Main extends JavaPlugin implements Listener {

    mysql db;
    mainGui gui;
    gig guiGig;
    shop shop;
    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        db = new mysql();
        new hello(this);
        new bank(this);
        new transfer(this);
        gui = new mainGui(this);
        guiGig = new gig(this);
        shop = new shop(this);
    }

    @Override
    public void onDisable(){

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player user = e.getPlayer();
        db.checkIfUserExists(user.getName(), user.getUniqueId().toString() ,db.connection);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        try{
        if(e.getInventory().getHolder() instanceof mainGui || e.getInventory().getHolder() instanceof gig || e.getInventory().getHolder() instanceof shop){
            if(e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                e.setCancelled(true);
            }
            if (!e.getClickedInventory().equals(gui.inv)  || !e.getClickedInventory().equals(guiGig.gig) || !e.getClickedInventory().equals(shop.shop)) e.setCancelled(true);
            final ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
            final Player p = (Player) e.getWhoClicked();
            if (clickedItem.getType() == Material.BARRIER && e.getRawSlot() == 31) gui.Mainguireturn(p);
            if (clickedItem.getType() == Material.BEDROCK && e.getRawSlot() == 10)  guiGig.gigs(p);
            if (clickedItem.getType() == Material.BLACK_BED && e.getRawSlot() == 27) guiGig.previousPage();
            if (clickedItem.getType() == Material.RED_BED && e.getRawSlot() == 35) guiGig.nextPage();
            if (clickedItem.getType() == Material.ENCHANTED_GOLDEN_APPLE && e.getRawSlot() == 17) shop.shops(p);
            if (clickedItem.getType() == Material.YELLOW_BED && e.getRawSlot() == 27) shop.previousPage();
            if (clickedItem.getType() == Material.GREEN_BED && e.getRawSlot() == 35) shop.nextPage();
        }
        }catch (Exception ex){
            return;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
