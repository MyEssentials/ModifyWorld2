package modifyworld2.handlers;

import modifyworld2.BaseListener;
import modifyworld2.Modifyworld2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;

public class PlayerListener extends BaseListener implements IPlayerTracker {
	@Override
	protected void registerEvents(Modifyworld2 plugin) {
		MinecraftForge.EVENT_BUS.register(this);
		GameRegistry.registerPlayerTracker(this);
	}

	@Override
	public void onPlayerLogin(EntityPlayer player) {
		if (!enabled)
			return;

		EntityPlayerMP pl = (EntityPlayerMP) player;

		if (permissionDenied(pl, "modifyworld.login") || (pl.dimension == 0 && permissionDenied(pl, "modifyworld.enter", 0))) {
			pl.playerNetServerHandler.kickPlayerFromServer(informer.getMessage(pl, "modifyworld.login"));
		}

		if (pl.dimension != 0 && permissionDenied(pl, "modifyworld.enter", pl.dimension)) {
			sendToServerSpawn(pl);
		}
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
		if (!enabled)
			return;

		EntityPlayerMP pl = (EntityPlayerMP) player;

		if (pl.dimension != 0 && permissionDenied(pl, "modifyworld.enter", pl.dimension)) {
			respawnPlayer(pl, 0);
		}
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}
}
