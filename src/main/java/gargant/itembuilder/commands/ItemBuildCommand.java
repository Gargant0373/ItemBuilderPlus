package gargant.itembuilder.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import gargant.itembuilder.containers.ItemBuildContainer;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.annotations.RequiresPlayer;
import masecla.mlib.annotations.SubcommandInfo;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

@RegisterableInfo(command = "item")
@RequiresPlayer
public class ItemBuildCommand extends Registerable {

	private ItemBuildContainer itemContainer;

	public ItemBuildCommand(MLib lib, ItemBuildContainer itemContainer) {
		super(lib);
		this.itemContainer = itemContainer;
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
