package gargant.itembuilder.containers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import masecla.mlib.containers.generic.ImmutableContainer;
import masecla.mlib.main.MLib;
import net.md_5.bungee.api.ChatColor;
import net.wesjd.anvilgui.AnvilGUI;

public class ItemBuildContainer extends ImmutableContainer {

	private Map<UUID, ItemStack> building = new HashMap<>();

	public void setBuilding(Player p, ItemStack s) {
		this.building.put(p.getUniqueId(), s);
	}

	public ItemStack getBuilding(Player p) {
		return this.building.getOrDefault(p.getUniqueId(), null);
	}

	public ItemBuildContainer(MLib lib) {
		super(lib);
	}

	@Override
	public void onTopClick(InventoryClickEvent ev) {

		Player p = (Player) ev.getWhoClicked();

		// Display Name Handling
		if (ev.getSlot() == 11) {
			new AnvilGUI.Builder().plugin(lib.getPlugin()).text("Enter new Display Name!")
					.title(ChatColor.YELLOW + "ItemBuilder+ " + ChatColor.GRAY + "| " + ChatColor.WHITE
							+ "Display Name")
					.itemLeft(new ItemStack(Material.PAPER))
					.onClose(c -> lib.getContainerAPI().openFor(c, ItemBuildContainer.class)).onComplete((c, r) -> {
						ItemStack s = this.building.get(c.getUniqueId());
						s.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', r));
						return AnvilGUI.Response.close();
					}).open(p);
			return;
		}

		// Amount handling
		if (ev.getSlot() == 13) {
			new AnvilGUI.Builder().plugin(lib.getPlugin()).text("Enter new item amount!")
					.title(ChatColor.YELLOW + "ItemBuilder+ " + ChatColor.GRAY + "| " + ChatColor.WHITE + "Amount")
					.itemLeft(new ItemStack(Material.PAPER))
					.onClose(c -> lib.getContainerAPI().openFor(c, ItemBuildContainer.class)).onComplete((c, r) -> {
						ItemStack s = this.building.get(c.getUniqueId());

						int amount = -1;
						try {
							amount = Integer.parseInt(r);
						} catch (NumberFormatException e) {
							return AnvilGUI.Response.text("Invalid amount!");
						}

						s.setAmount(amount);
						return AnvilGUI.Response.close();
					}).open(p);
			return;
		}
		
		// Material handling
		if(ev.getSlot() == 15) {
			lib.getContainerAPI().openFor(p, MaterialEditContainer.class);
			return;
		}

	}

	@Override
	public Inventory getInventory(Player p) {
		ItemStack building = this.building.getOrDefault(p.getUniqueId(), null);
		if (building == null) {
			return Bukkit.createInventory(p, getSize(), ChatColor.RED + "NO ITEM BUILDING!");
		}

		Inventory inv = Bukkit.createInventory(p, getSize(), ChatColor.YELLOW + "ItemBuilder+");

		inv.setItem(4, this.building.get(p.getUniqueId()));

		// Display name setting
		inv.setItem(11, this.getDisplayName(p));

		// Lore shit

		// Amount
		inv.setItem(13, this.getAmount(p));

		// Change material
		inv.setItem(15, this.getMaterial(p));

		// Add nbt

		return inv;
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

	private ItemStack getDisplayName(Player p) {
		ItemStack s = new ItemStack(Material.NAME_TAG);
		ItemMeta m = s.getItemMeta();

		String displayName = this.building.get(p.getUniqueId()).getItemMeta().getDisplayName();

		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eDisplay Name: &f" + displayName));
		m.setLore(Arrays.asList("", ChatColor.GRAY + "- " + ChatColor.WHITE + "Click to change!"));

		s.setItemMeta(m);
		return s;
	}

	private ItemStack getAmount(Player p) {
		int amount = this.building.get(p.getUniqueId()).getAmount();

		ItemStack s = new ItemStack(Material.IRON_INGOT, amount);
		ItemMeta m = s.getItemMeta();

		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eAmount: &f" + amount));
		m.setLore(Arrays.asList("", ChatColor.GRAY + "- " + ChatColor.WHITE + "Click to change!"));

		s.setItemMeta(m);
		return s;
	}

	private ItemStack getMaterial(Player p) {
		Material current = this.building.get(p.getUniqueId()).getType();

		ItemStack s = new ItemStack(current);
		ItemMeta m = s.getItemMeta();

		m.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				"&eMaterial: &f" + lib.getStringAPI().upperCaseWords(current.name().replace('_', ' '))));
		m.setLore(Arrays.asList("", ChatColor.GRAY + "- " + ChatColor.WHITE + "Click to change!"));
		
		s.setItemMeta(m);
		return s;
	}

}
