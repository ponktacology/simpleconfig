package me.ponktacology.simpleconfig.adapter.impl;

import me.ponktacology.simpleconfig.adapter.ConfigAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class BungeeConfigAdapter extends ConfigAdapter {

    @Override
    public void set(String fileName, String path, Object object) throws IOException {
        final Configuration configuration = getConfiguration(fileName);
        configuration.set(path, object);
        getConfigurationProvider().save(configuration, getFile(fileName));
    }

    @Override
    public Object get(String fileName, String path) throws IOException {
        return getConfiguration(fileName).get(path);
    }

    @Override
    public boolean isSet(String fileName, String path) throws IOException {
        return getConfiguration(fileName).contains(path);
    }

    @Override
    protected File getFile(String fileName) throws IOException {
        final File file = new File(ProxyServer.getInstance().getPluginsFolder(), fileName);
        if (!file.exists()) file.createNewFile();
        return file;
    }

    private ConfigurationProvider getConfigurationProvider() {
        return ConfigurationProvider.getProvider(YamlConfiguration.class);
    }

    private Configuration getConfiguration(String fileName) throws IOException {
        return getConfigurationProvider().load(getFile(fileName));
    }

}
