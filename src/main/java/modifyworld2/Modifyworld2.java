package modifyworld2;

import java.io.File;

import modifyworld2.handlers.BlockListener;
import modifyworld2.handlers.EntityListener;
import modifyworld2.handlers.PlayerListener;
import modifyworld2.handlers.VehicleListener;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(
		modid="Modifyworld2", 
		name="Modifyworld2", 
		version="1.5.0.0"
	)
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class Modifyworld2 
{
	@Instance("Modifyworld2")
	public static Modifyworld2 instance;

	public static String MOD_NAME = "Modifyworld2";
	protected BaseListener[] listeners = new BaseListener[]
	{
		new PlayerListener(),
		new EntityListener(),
		new BlockListener(),
		new VehicleListener()
	};
	public PlayerInformer informer;
	public File configFile;
	protected Configuration config;

	@EventHandler
	public void onLoad(FMLPreInitializationEvent ev)
	{
		configFile = ev.getSuggestedConfigurationFile();
		config = new Configuration(configFile);
		informer = new PlayerInformer(config);
	}

    @EventHandler
	public void modsLoaded(FMLServerStartedEvent var1)
	{
		for (BaseListener l : listeners)
			l.load();
		
		onEnable();
	}
	
	public void reload()
	{
		onDisable();
		onEnable();
	}

	public void onEnable() 
	{
		reloadConfig();

		for (BaseListener l : listeners)
			l.enabled = true;
		
		Log.info("§2enabled");

		this.saveConfig();
	}

	public void onDisable()
	{
		for (BaseListener l : listeners)
			l.enabled = false;

		Log.info("§2 disabled");
	}

	public Configuration getConfig()
	{
		if (this.config == null) {
			this.reloadConfig();
		}

		return this.config;
	}

	public void saveConfig() 
	{
		this.config.save();
	}

	public void reloadConfig() 
	{
		this.config.load();
	}
}
