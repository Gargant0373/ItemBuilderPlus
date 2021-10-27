package gargant.itembuilder.containers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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

	private Map<UUID, Integer> loreIndexes = new HashMap<>();

	public void setBuilding(Player p, ItemStack s) {
		this.building.put(p.getUniqueId(), s);
		this.loreIndexes.put(p.getUniqueId(), 0);
	}

	public ItemStack getBuilding(Player p) {
		return this.building.getOrDefault(p.getUniqueId(), null);
	}

	public ItemBuildContainer(MLib lib) {
		super(lib);
	}

	@Override
	public void onTopClick(InventoryClickEvent ev) {
		ev.setCancelled(true);
		Player p = (Player) ev.getWhoClicked();

		if (this.building.getOrDefault(p.getUniqueId(), null) == null)
			return;

		// Display Name Handling
		if (ev.getSlot() == 20) {
			new AnvilGUI.Builder().plugin(lib.getPlugin()).text("Enter new Display Name!")
					.title(ChatColor.YELLOW + "ItemBuilder+ " + ChatColor.GRAY + "| " + ChatColor.WHITE
							+ "Display Name")
					.itemLeft(new ItemStack(Material.PAPER))
					.onClose(c -> lib.getContainerAPI().openFor(c, ItemBuildContainer.class)).onComplete((c, r) -> {
						ItemStack s = this.building.get(c.getUniqueId());
						ItemMeta m = s.getItemMeta();
						m.setDisplayName(ChatColor.translateAlternateColorCodes('&', r));
						s.setItemMeta(m);
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

						if (amount > 64 || amount < 1) {
							return AnvilGUI.Response.text("Amount should be > 0 and < 64.");
						}

						s.setAmount(amount);
						return AnvilGUI.Response.close();
					}).open(p);
			return;
		}

		// Material handling
		if (ev.getSlot() == 24) {
			lib.getContainerAPI().openFor(p, MaterialEditContainer.class);
			return;
		}

		// Lore handling
		if (ev.getSlot() == 31) {
			int maxIndex = (this.building.get(p.getUniqueId()).getItemMeta().getLore() == null ? 0
					: this.building.get(p.getUniqueId()).getItemMeta().getLore().size());
			int currentIndex = this.loreIndexes.get(p.getUniqueId());

			if (ev.getClick().equals(ClickType.LEFT)) {
				this.loreIndexes.put(p.getUniqueId(), (currentIndex - 1 >= 0 ? currentIndex - 1 : maxIndex));
				return;
			}
			if (ev.getClick().equals(ClickType.RIGHT)) {
				this.loreIndexes.put(p.getUniqueId(), (currentIndex + 1 <= maxIndex ? currentIndex + 1 : 0));
				return;
			}
			if (ev.getClick().equals(ClickType.MIDDLE)) {
				String operation = "";
				if (currentIndex == maxIndex)
					operation = "New Lore Line";
				else
					operation = "Edit Lore Line";
				new AnvilGUI.Builder().plugin(lib.getPlugin()).text(operation)
						.title(ChatColor.YELLOW + "ItemBuilder+ " + ChatColor.GRAY + "| " + ChatColor.WHITE + "Lore")
						.itemLeft(new ItemStack(Material.PAPER))
						.onClose(c -> lib.getContainerAPI().openFor(c, ItemBuildContainer.class)).onComplete((c, r) -> {
							ItemStack s = this.building.get(c.getUniqueId());
							List<String> lore = s.getItemMeta().getLore();
							if (lore == null)
								lore = new ArrayList<>();
							if (currentIndex == maxIndex)
								lore.add(ChatColor.translateAlternateColorCodes('&', r));
							else
								lore.set(currentIndex, ChatColor.translateAlternateColorCodes('&', r));
							ItemMeta m = s.getItemMeta();
							m.setLore(lore);
							s.setItemMeta(m);
							return AnvilGUI.Response.close();
						}).open(p);
				return;
			}
		}

		// Finish handling
		if (ev.getSlot() == 49) {
			lib.getContainerAPI().closeFor(p);
			p.getInventory().addItem(this.building.get(p.getUniqueId()));
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

		// Display name setting
		inv.setItem(20, this.getDisplayName(p));

		// Amount
		inv.setItem(13, this.getAmount(p));

		// Change material
		inv.setItem(24, this.getMaterial(p));

		// Lore shit
		inv.setItem(31, this.getLore(p));

		// Add nbt

		// Finish button
		inv.setItem(49, this.getFinish(p));

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

	private ItemStack getLore(Player p) {
		int index = this.loreIndexes.getOrDefault(p.getUniqueId(), 0);

		ItemStack s = new ItemStack(Material.BOOK);
		List<String> actualLore = this.building.get(p.getUniqueId()).getItemMeta().getLore();
		if (actualLore == null)
			actualLore = new ArrayList<>();

		ItemMeta m = s.getItemMeta();

		m.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&eLore"));

		List<String> lore = new ArrayList<>();

		if (actualLore.size() == 0) {
			lore.add(ChatColor.GRAY + "No Lore");
			lore.add("");
		} else if (actualLore.size() != index) {
			for (int i = 0; i < actualLore.size(); i++) {
				if (i == index)
					lore.add(ChatColor.YELLOW + (this.toggleLine() ? "> " : "  ") + ChatColor.GOLD + actualLore.get(i));
				else
					lore.add(ChatColor.WHITE + actualLore.get(i));
			}
			lore.add(ChatColor.WHITE + "+ New Line");
		} else {
			for (int i = 0; i < actualLore.size(); i++) {
				lore.add(ChatColor.WHITE + actualLore.get(i));
			}
			lore.add(ChatColor.YELLOW + (this.toggleLine() ? "> " : "  ") + ChatColor.GOLD + "+ New Line");
		}
		lore.add("");
		lore.add(ChatColor.GRAY + "Left click to go up!");
		lore.add(ChatColor.GRAY + "Right click to go down!");
		lore.add(ChatColor.GRAY + "Middle click to edit!");

		m.setLore(lore);
		s.setItemMeta(m);
		return s;
	}
	
	private boolean toggleLine() {
		if(Instant.now().getEpochSecond() % 2 == 0)
			return true;
		return false;
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

	private ItemStack getFinish(Player p) {
		ItemStack s = this.building.get(p.getUniqueId()).clone();
		ItemMeta m = s.getItemMeta();

		m.setLore(Arrays.asList("", ChatColor.WHITE + "Click to get item!"));

		s.setItemMeta(m);
		return s;
	}
}
