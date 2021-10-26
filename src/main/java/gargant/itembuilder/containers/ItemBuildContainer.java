package gargant.itembuilder.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;

public class ItemBuildContainer extends ImmutableContainer{

	private Map<UUID, ItemStack> building = new HashMap<>();
	
	public void setBuilding(Player p, ItemStack s) {
		this.building.put(p.getUniqueId(), s);
	}
	
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
