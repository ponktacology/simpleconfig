package me.ponktacology.simpleconfig.adapter;

import java.io.File;
import java.io.IOException;

public abstract class ConfigAdapter {

    public abstract void set(String fileName, String path, Object object) throws Exception;

    public abstract Object get(String fileName, String path) throws Exception;

    public abstract boolean isSet(String fileName, String path) throws Exception;

    protected abstract File getFile(String fileName) throws IOException;
}
