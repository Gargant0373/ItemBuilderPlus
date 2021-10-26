package gargant.itembuilder.commands;

import gargant.itembuilder.containers.ItemBuildContainer;
import masecla.mlib.annotations.RegisterableInfo;
import masecla.mlib.annotations.RequiresPlayer;
import masecla.mlib.classes.Registerable;
import masecla.mlib.main.MLib;

@RegisterableInfo(command = "item")
@RequiresPlayer
public class ItemBuildCommand extends Registerable{

	private ItemBuildContainer itemContainer;
	
	public ItemBuildCommand(MLib lib,ItemBuildContainer itemContainer) {
		super(lib);
		this.itemContainer = itemContainer;
	}

}
