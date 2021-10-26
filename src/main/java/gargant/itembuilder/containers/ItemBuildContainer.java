package gargant.itembuilder.containers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;

public class ItemBuildContainer extends ImmutableContainer{

	public ItemBuildContainer(MLib lib) {
		super(lib);
	}

	@Override
	public void onTopClick(InventoryClickEvent ev) {
	}

	@Override
	public Inventory getInventory(Player p) {
		return null;
	}

	@Override
	public int getSize() {
		return 54;
	}

	@Override
	public int getUpdatingInterval() {
		return 10;
	}

	@Override
	public boolean requiresUpdating() {
		return true;
	}

}
