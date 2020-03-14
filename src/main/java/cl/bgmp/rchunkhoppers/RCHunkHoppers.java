package cl.bgmp.rchunkhoppers;


import cl.bgmp.rchoppers.RCHopper;
import cl.bgmp.rchoppers.RCHoppers;
import cl.bgmp.rchunkhoppers.Commands.CommandTab;
import cl.bgmp.rchunkhoppers.Commands.GetChunkHopperCommand;
import cl.bgmp.rchunkhoppers.Commands.GiveUpgradeCommand;
import cl.bgmp.rchunkhoppers.Commands.UpgradeCommandTab;
import cl.bgmp.rchunkhoppers.Listeners.BreakChunkHopperListener;
import cl.bgmp.rchunkhoppers.Listeners.FilterGUI.FilterGuiClickListener;
import cl.bgmp.rchunkhoppers.Listeners.FilterGUI.SellFilterGuiClickListener;
import cl.bgmp.rchunkhoppers.Listeners.PickupItemListenerNormal;
import cl.bgmp.rchunkhoppers.Listeners.PickupItemListenerShopGUIPlus;
import cl.bgmp.rchunkhoppers.Listeners.PlaceChunkHopperListener;
import cl.bgmp.rchunkhoppers.Listeners.SettingsGUI.CloseSettingsGuiListener;
import cl.bgmp.rchunkhoppers.Listeners.SettingsGUI.SettingsGuiClickListener;
import cl.bgmp.rchunkhoppers.Listeners.SettingsGUI.SettingsGuiCloseListener;
import cl.bgmp.rchunkhoppers.Listeners.ShiftClickChunkHopperListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class RCHunkHoppers extends JavaPlugin {
    private static RCHunkHoppers plugin;

    private static boolean shopGuiPlusInstalled = true;
    private PluginManager m;

    private static File worthFile;
    private static FileConfiguration worth;
    private static File offlineSoldFile;
    private static FileConfiguration offlineSold;

    private static Economy econ = null;



    @Override
    public void onEnable() {
        m = Bukkit.getPluginManager();
        checkDependencies();

        plugin = this;
        createConfig();
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run() {
                registerChunkHoppers();

            }
        }, 5);


        getCommand("gch").setExecutor(new GetChunkHopperCommand());
        getCommand("gch").setTabCompleter(new CommandTab());
        getCommand("gu").setExecutor(new GiveUpgradeCommand());
        getCommand("gu").setTabCompleter(new UpgradeCommandTab());

        m.registerEvents(new SettingsGuiClickListener(), this);
        m.registerEvents(new SettingsGuiCloseListener(), this);
        m.registerEvents(new BreakChunkHopperListener(), this);
        m.registerEvents(new CloseSettingsGuiListener(), this);
        m.registerEvents(new PlaceChunkHopperListener(), this);
        m.registerEvents(new ShiftClickChunkHopperListener(), this);
        m.registerEvents(new FilterGuiClickListener(), this);
        m.registerEvents(new SellFilterGuiClickListener(), this);
        if(shopGuiPlusInstalled)
            m.registerEvents(new PickupItemListenerShopGUIPlus(), this);
        else
            m.registerEvents(new PickupItemListenerNormal(), this);


    }

    private void checkDependencies() {
        if(!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(RCHoppers.prefix + "§4Vault is required for this version!");
            Bukkit.getConsoleSender().sendMessage(RCHoppers.prefix + "§4Try the version without autosell option:");
            Bukkit.getConsoleSender().sendMessage(RCHoppers.prefix + "§4lolimi.github.io");
            m.disablePlugin(this);
        }
        if(m.getPlugin("ShopGUIPlus") == null) {
            shopGuiPlusInstalled = false;
            Bukkit.getConsoleSender().sendMessage(RCHoppers.prefix+ "§cShopGUIPlus is not installed, using worth.yml!");
        }
    }


    private void createConfig() {
        this.getDataFolder().mkdir();
        new File(getDataFolder().getPath()+ File.separator + "Data").mkdir();
        File f = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
                OutputStream writer = new FileOutputStream(f);
                InputStream out = this.getResource("config.yml");
                byte[] linebuffer = new byte[out.available()];
                out.read(linebuffer);
                writer.write(linebuffer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        f = new File(this.getDataFolder()+ File.separator + "worth.yml");
        worthFile = f;
        if (!f.exists()) {
            try {
                f.createNewFile();
                OutputStream writer = new FileOutputStream(f);
                InputStream out = this.getResource("worth.yml");
                byte[] linebuffer = new byte[out.available()];
                out.read(linebuffer);
                writer.write(linebuffer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        worth = YamlConfiguration.loadConfiguration(worthFile);

        offlineSoldFile = new File(this.getDataFolder()+ File.separator + "worth.yml");
        if(!offlineSoldFile.exists())
            try {
                offlineSoldFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        offlineSold = YamlConfiguration.loadConfiguration(offlineSoldFile);
    }

    private void registerChunkHoppers() {
        File[] files = new File(getDataFolder().getPath()+ File.separator + "Data").listFiles();
        if(files == null) return;
        ChunkHopper[] hoppers = new ChunkHopper[files.length];
        RCHopper.registerType(ChunkHopper.class);
        for(int i = 0; i < files.length; i++) {
            if(files.length == 0) break;
            Location loc = RCHopper.getLocation(files[i]);
            hoppers[i] = new ChunkHopper(loc, null, null);
        }
        RCHopper.registerHoppers(hoppers, this);
    }

    public static RCHunkHoppers getPlugin() {
        return plugin;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static FileConfiguration getWorth() {
        return worth;
    }

    public static FileConfiguration getOfflineSold() {
        return offlineSold;
    }
}
