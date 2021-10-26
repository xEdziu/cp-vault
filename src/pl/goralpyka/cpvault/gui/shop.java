package pl.goralpyka.cpvault.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.goralpyka.cpvault.Main;

import java.util.Arrays;

public class shop implements CommandExecutor, InventoryHolder {

    private Main plugin;
    private int count = 0,gigmax = 45;
    int tmp =0;
    boolean bedBack= false , bedNext = false;
    public Inventory shop;

    public shop(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("shop").setExecutor(this);
        shop = Bukkit.createInventory(this, 36, "Sklepik");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;

        }
        initializeItems();
        openInventory(p);
        return true;
    }

    public void shops(CommandSender sender){
        Player p = (Player) sender;
        initializeItems();
        openInventory(p);
        tmp = 0 ;
    }

    public void initializeItems() {
        tmp = 0;
        clearInv();
        shop.setItem(31, createGuiItem(Material.BARRIER,"EXIT","TO GUI","DO GUI","DO 'Menu'"));
        count=0;
        if (gigmax > 18){
            bedBack=false;bedNext=true;
            shop.setItem(35, createGuiItem(Material.RED_BED,"NEXT","Lore","Lore","Lore"));
                for (; count < 18;count++){
                    shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp+count+1),"itm","itm"));
                }
        }else{
            for (;count <gigmax ;count++){
                shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp+count+1),"itm","itm"));
            }
        bedBack=false;bedNext=false;
            }
        checkOnExit(bedBack,bedNext);
    }

    public void initializeItems(int tmp) {
        clearInv();
        shop.setItem(31, createGuiItem(Material.BARRIER,"EXIT","TO GUI","DO GUI","DO 'Menu'"));
        count=0;
            if (gigmax > 18){
                bedBack=false;bedNext=true;
                shop.setItem(35, createGuiItem(Material.RED_BED,"NEXT","Lore","Lore","Lore"));
            for (; count < 18;count++){
                shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp+count+1),"itm","itm"));
            }
        }else{
            for (;count <gigmax ;count++){
                shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp+count+1),"itm","itm"));
            }
            bedBack=false;bedNext=false;
        }
        checkOnExit(bedBack,bedNext);
    }

    public void clearInv(){
        for (count=0;count < 36;count++){
            shop.setItem(count,new ItemStack(Material.AIR));
        }
    }

    public void previousPage(){
        count = 0;
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + String.valueOf(tmp)+" tmp");
        if (tmp-18 <= 0){
            initializeItems(tmp-18);
            bedBack = false;
        }else {
            for (; count < 18; count++){
                shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp - 17 + count),"itm","itm"));
            }
            bedBack =true;bedNext=true;
        }if (tmp - count > 0){
            tmp -= count;
            bedBack = false;
        }
        count = 0;
        checkOnExit(bedBack,bedNext);
    }

    public void nextPage(){
        tmp += count;
        count = 0;
        if (tmp+17>gigmax){
            bedNext=false;bedBack=true;
            for (;count<(gigmax-tmp);count++){

                shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp+count+1),"itm","itm"));
            }
            if (tmp+count>=gigmax){
                for (;count<18;count++){
                    shop.setItem(count,new ItemStack(Material.AIR));
                }
            }
            shop.setItem(35,new ItemStack(Material.AIR));
        }else{
            bedBack =true;bedNext=true;
            for (;count<18;count++){
                shop.setItem(count, createGuiItem(Material.RAIL,String.valueOf(tmp+count+1),"itm","itm"));
            }
        }
        checkOnExit(bedBack,bedNext);
    }
    public void checkOnExit(boolean b, boolean n){
        if(b){
            shop.setItem(27, createGuiItem(Material.YELLOW_BED,"BACK","",""));
        }
        if (n){
            shop.setItem(35, createGuiItem(Material.GREEN_BED,"NEXT","Lore","Lore","Lore"));
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(shop);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
