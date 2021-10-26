package pl.goralpyka.cpvault.gui;

import org.bukkit.Bukkit;
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

public class mainGui implements CommandExecutor, InventoryHolder {

    private Main plugin;
    int X =10;
    String sklep = "Db info";
    String offerts = "X";
    public Inventory inv;
    public mainGui(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("gui").setExecutor(this);
        inv = Bukkit.createInventory(this, 27, "Gui Picker");
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
    public void Mainguireturn(CommandSender sender){
        Player p = (Player) sender;
        initializeItems();
        openInventory(p);
    }
        public void initializeItems() {
            inv.setItem(10, createGuiItem(Material.BEDROCK,"Gig","Mamy dla Ciebie "+X+" zleceń"," DO ROBOTY!"));
            inv.setItem(13, createGuiItem(Material.GLASS_PANE,"&4Tmp","Lore","Lore","Lore"));
            inv.setItem(17, createGuiItem(Material.ENCHANTED_GOLDEN_APPLE,"Skelp",sklep,offerts));

        }

        // Nice little method to create a gui item with a custom name, and description
        protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
            final ItemStack item = new ItemStack(material, 1);
            final ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(name);

            // Set the lore of the item
            meta.setLore(Arrays.asList(lore));

            item.setItemMeta(meta);

            return item;
        }

        public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }


    @Override
    public Inventory getInventory() {
        return null;
    }
}
