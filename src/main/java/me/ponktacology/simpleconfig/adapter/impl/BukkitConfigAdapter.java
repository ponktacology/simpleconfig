package me.ponktacology.simpleconfig.adapter.impl;

import me.ponktacology.simpleconfig.adapter.ConfigAdapter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BukkitConfigAdapter extends ConfigAdapter {

    public JavaPlugin javaPlugin;

    public BukkitConfigAdapter(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    @Override
    public void set(String fileName, String path, Object object) throws IOException, InvalidConfigurationException {
        final YamlConfiguration yamlConfiguration = getYamlConfiguration(fileName);
        yamlConfiguration.set(path, object);
        yamlConfiguration.save(getFile(fileName));
    }

    @Override
    public Object get(String fileName, String path) throws IOException, InvalidConfigurationException {
        return getYamlConfiguration(fileName).get(path);
    }

    @Override
    public boolean isSet(String fileName, String path) throws IOException, InvalidConfigurationException {
        return getYamlConfiguration(fileName).isSet(path);
    }

    @Override
    protected File getFile(String fileName) throws IOException {
        System.out.println(javaPlugin.getDataFolder().toPath().toString() + " " + fileName);
        final File file = new File(javaPlugin.getDataFolder(), fileName);
        if(!javaPlugin.getDataFolder().exists()) javaPlugin.getDataFolder().mkdirs();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    private YamlConfiguration getYamlConfiguration(String fileName) throws IOException, InvalidConfigurationException {
        final YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(getFile(fileName));
        return yamlConfiguration;
    }

}
