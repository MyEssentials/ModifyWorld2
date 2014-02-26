package modifyworld2;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import forgeperms.api.*;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import modifyworld2.handlers.BlockListener;
import modifyworld2.handlers.EntityListener;
import modifyworld2.handlers.PlayerListener;
import modifyworld2.handlers.VehicleListener;

@Mod(
		modid="Modifyworld2", 
		name="Modifyworld2", 
		version="1.5.0.0"
	)
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class Modifyworld2 
{
    public IPermissionManager permManager;
    public IChatManager chatManager;
    public IEconomyManager economyManager;
	@Instance("Modifyworld2")
	public static Modifyworld2 instance;


    public void initPermManager() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> forgePerms = Class.forName("forgeperms.ForgePerms");
        permManager = (IPermissionManager) forgePerms.getMethod("getPermissionManager").invoke(null);
    }

    public void initChatManager() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> forgePerms = Class.forName("forgeperms.ForgePerms");
        chatManager = (IChatManager) forgePerms.getMethod("getChatManager").invoke(null);
    }

    public void initEcoManager() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> forgePerms = Class.forName("forgeperms.ForgePerms");
        economyManager = (IEconomyManager) forgePerms.getMethod("getEconomyManager").invoke(null);
    }

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
        try {
            initPermManager();
            initChatManager();
            initEcoManager();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
