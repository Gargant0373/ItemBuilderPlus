package gargant.itembuilder.main;

import org.bukkit.plugin.java.JavaPlugin;

import gargant.itembuilder.commands.ItemBuildCommand;
import gargant.itembuilder.containers.ItemBuildContainer;
import gargant.itembuilder.containers.MaterialEditContainer;
import masecla.mlib.main.MLib;

public class ItemBuilderPlus extends JavaPlugin{

	private MLib lib;
	
	private ItemBuildContainer itemContainer;
	
	@Override
	public void onEnable() {
		this.lib = new MLib(this);
		
		this.itemContainer = new ItemBuildContainer(lib);
		this.itemContainer.register();
		
		new MaterialEditContainer(lib, itemContainer).register();
		
		new ItemBuildCommand(lib, itemContainer).register();
	}
}
