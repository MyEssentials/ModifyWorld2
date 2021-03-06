package modifyworld2.handlers;

import java.util.logging.Level;

import modifyworld2.BaseListener;
import modifyworld2.Log;
import modifyworld2.Modifyworld2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class EntityListener extends BaseListener {
	private String[] dropsBlacklist = new String[0];

	@Override
	public void load() {
		super.load();
		this.dropsBlacklist = config.get("General", "mob-drop-blacklist", "").getString().split(",");
		String st = "";
		for (String s : dropsBlacklist) {
			st += s;
		}
		Log.log(Level.INFO, st);
	}

//	@ForgeSubscribe(priority = EventPriority.HIGH)
//	public void onMobDrop(LivingDropsEvent event) {
//		if (!enabled)
//			return;
//
//		for (EntityItem drop : event.drops){
//			Log.log(Level.INFO, "ItemID: " + Integer.toString(drop.getEntityItem().itemID));
//			for (String itemID : dropsBlacklist){
//				int _itemID = Integer.parseInt(itemID);
//				if (drop.getEntityItem().itemID == _itemID){
//					Log.log(Level.INFO, "Blacklisted item " + Integer.toString(_itemID) + " almost dropped in the world!");
//					event.setCanceled(true);
//				}
//			}
//		}
//	}

	@ForgeSubscribe(priority = EventPriority.LOW)
	public void onEntityAttack(AttackEntityEvent event) {
		if (!enabled)
			return;

		if (permissionDenied(event.entityPlayer, "modifyworld.damage.deal", event.entityPlayer))
			event.setCanceled(true);

		if (event.target instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.target;
			if (_permissionDenied(player, "modifyworld.damage.take", event.entityPlayer))
				event.setCanceled(true);
		}

	}

	@ForgeSubscribe(priority = EventPriority.LOW)
	public void onEntityDamaged(LivingAttackEvent event) {
		if (!enabled)
			return;

		if (event.entityLiving instanceof EntityPlayer) { // player are been
															// damaged by
															// environment
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			if (_permissionDenied(player, "modifyworld.damage.take", event.source.damageType.toLowerCase().replace("_", ""))) {
				event.setCanceled(true);

				if (event.source == DamageSource.outOfWorld && player instanceof EntityPlayerMP)
					respawnPlayer((EntityPlayerMP) player);
			}
		}
	}
	 
	@Override
	protected void registerEvents(Modifyworld2 plugin) {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
