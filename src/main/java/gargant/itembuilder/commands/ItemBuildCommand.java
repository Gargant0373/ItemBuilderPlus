package gargant.itembuilder.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import gargant.itembuilder.containers.ItemBuildContainer;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.annotations.RequiresPlayer;
import masecla.mlib.annotations.SubcommandInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.classes.Replaceable;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

@RegisterableInfo(command = "item")
@RequiresPlayer
public class ItemBuildCommand extends Registerable {

	private ItemBuildContainer itemContainer;

	public ItemBuildCommand(MLib lib, ItemBuildContainer itemContainer) {
		super(lib);
		this.itemContainer = itemContainer;
	}
	
	@Override
	public void fallbackCommand(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&o&lItemBuilder+ &7- &f&oEasiest way to build items!"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fRun /item &chelp &ffor help!"));
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&oWritten with love by Gargant!"));
	}
	
	@SubcommandInfo(subcommand = "help", permission = "itembuilder.help")
	public void onHelp(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&o&lItemBuilder+ &7- &fEasiest way to build items!"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/item &chand &7- &fBuild the item in your hand!"));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/item &copen <Material> &7- &fBuild an item with the provided material."));
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f/item &creturn &7- &fReturn to building the previous item."));
	}

	@SubcommandInfo(subcommand = "open", permission = "itembuilder.build")
	public void onOpen(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (item == null || item.getType().equals(Material.AIR)) {
			lib.getMessagesAPI().sendMessage("exception.no-item", p);
			return;
		}
		this.itemContainer.setBuilding(p, item.clone());
		lib.getContainerAPI().openFor(p, ItemBuildContainer.class);
	}
	
	@SubcommandInfo(subcommand = "open", permission = "itembuilder.build")
	public void onOpenMaterial(Player p, String mat) {
		Material m = Material.matchMaterial(mat);
		if(m == null) {
			lib.getMessagesAPI().sendMessage("exception.invalid-material", p, new Replaceable("%material%", mat));
			return;
		}
		this.itemContainer.setBuilding(p, new ItemStack(m));
		lib.getContainerAPI().openFor(p, ItemBuildContainer.class);
	}
	
	@SubcommandInfo(subcommand = "hand", permission = "itembuilder.build")
	public void onHand(Player p) {
		this.onOpen(p);
	}
	
	@SubcommandInfo(subcommand = "return", permission = "itembuilder.build")
	public void onReturn(Player p) {
		ItemStack item = this.itemContainer.getBuilding(p);
		if (item == null || item.getType().equals(Material.AIR)) {
			lib.getMessagesAPI().sendMessage("exception.not-building", p);
			return;
		}
		lib.getContainerAPI().openFor(p, ItemBuildContainer.class);
	}
	
	@SubcommandInfo(subcommand = "previous", permission = "itembuilder.build")
	public void onPrevious(Player p) {
		this.onReturn(p);
	}

}
