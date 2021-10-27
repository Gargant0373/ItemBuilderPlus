package gargant.itembuilder.containers;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;

public class MaterialEditContainer extends ImmutableContainer {

	private ItemBuildContainer itemContainer;

	public MaterialEditContainer(MLib lib, ItemBuildContainer itemContainer) {
		super(lib);
		this.itemContainer = itemContainer;
	}

	@Override
	public void onClose(InventoryCloseEvent ev) {
		if (ev.getInventory().getItem(4) == null || ev.getInventory().getItem(4).getType().equals(Material.AIR)) {
			lib.getContainerAPI().openFor((Player) ev.getPlayer(), ItemBuildContainer.class);
			return;
		}
		Material m = ev.getInventory().getItem(4).getType();
		itemContainer.getBuilding((Player) ev.getPlayer()).setType(m);
		lib.getContainerAPI().openFor((Player) ev.getPlayer(), ItemBuildContainer.class);
	}

	@Override
	public void onTopClick(InventoryClickEvent ev) {

	}

	@Override
	public Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, getSize(),
				ChatColor.translateAlternateColorCodes('&', "&eItemBuilder+ &7| &fMaterial"));
		for (int i = 0; i < 9; i++) {
			if (i != 4)
				inv.setItem(i, this.getPane(i));
		}

		return inv;
	}

	@Override
	public int getSize() {
		return 9;
	}

	@Override
	public int getUpdatingInterval() {
		return 0;
	}

	@Override
	public boolean requiresUpdating() {
		return false;
	}

	@Override
	public boolean updateOnClick() {
		return false;
	}

	private ItemStack getPane(int slot) {
		ItemStack s = new ItemStack(Material.STAINED_GLASS_PANE, (byte) 15);
		ItemMeta m = s.getItemMeta();

		m.setDisplayName(ChatColor.WHITE + this.buildArrows(slot));
		m.setLore(Arrays.asList("", ChatColor.WHITE + "Put the new material into the middle slot!"));

		s.setItemMeta(m);
		return s;
	}

	private String buildArrows(int slot) {
		switch (slot) {
		case 0:
			return "===>";
		case 1:
			return "==>";
		case 2:
			return "=>";
		case 3:
			return ">";
		case 5:
			return "<";
		case 6:
			return "<=";
		case 7:
			return "<==";
		case 8:
			return "<===";
		default:
			return "+";
		}
	}
}
