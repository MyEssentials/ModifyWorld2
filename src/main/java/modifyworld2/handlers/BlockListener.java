package modifyworld2.handlers;

import modifyworld2.BaseListener;
import modifyworld2.Modifyworld2;
import modifyworld2.entities.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class BlockListener extends BaseListener {
	@ForgeSubscribe(priority = EventPriority.LOW)
	public void onInteract(PlayerInteractEvent ev) {
		if (!enabled)
			return;

		if (ev.action == Action.LEFT_CLICK_BLOCK) {
			int id = ev.entityPlayer.worldObj.getBlockId(ev.x, ev.y, ev.z);
			int type = ev.entityPlayer.worldObj.getBlockMetadata(ev.x, ev.y, ev.z);
			if (permissionDenied(ev.entityPlayer, "modifyworld.blocks.destroy", new Block(id, type)))
				ev.setCanceled(true);
		} else if (ev.action == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = ev.entityPlayer.getHeldItem();
			if (item != null) {
				if (checkItemUse && permissionDenied(ev.entityPlayer, "modifyworld.items.use", item, "on.block", ev.entityPlayer.worldObj.getBlockId(ev.x, ev.y, ev.z))) {
					ev.entityPlayer.stopUsingItem();
					ev.setCanceled(true);
				}
			}
			int id = ev.entityPlayer.worldObj.getBlockId(ev.x, ev.y, ev.z);
			int type = ev.entityPlayer.worldObj.getBlockMetadata(ev.x, ev.y, ev.z);
			if (permissionDenied(ev.entityPlayer, "modifyworld.blocks.interact", new Block(id, type)))
				ev.setCanceled(true);
			
			if (item == null)
				return;
		
			id = item.itemID;
			type = item.getMaxDamage() < 1 ? item.getItemDamage() : 0;
			if (permissionDenied(ev.entityPlayer, "modifyworld.blocks.place", new Block(id, type)))
				ev.setCanceled(true);
		} else if (ev.action == Action.RIGHT_CLICK_AIR) {
			ItemStack item = ev.entityPlayer.getHeldItem();
			if (item == null)
				return;
			if (checkItemUse && permissionDenied(ev.entityPlayer, "modifyworld.items.use", item, "on.block", ev.entityPlayer.worldObj.getBlockId(ev.x, ev.y, ev.z))) {
				ev.entityPlayer.stopUsingItem();
				ev.setCanceled(true);
			}
		}
	}

	@Override
	protected void registerEvents(Modifyworld2 plugin) {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
