package me.ponktacology.simpleconfig.config;

import me.ponktacology.simpleconfig.adapter.ConfigAdapter;
import me.ponktacology.simpleconfig.adapter.impl.BukkitConfigAdapter;
import me.ponktacology.simpleconfig.adapter.impl.BungeeConfigAdapter;
import me.ponktacology.simpleconfig.config.annotation.Configurable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ConfigFactory {

    private final ConfigAdapter configAdapter;
    private final List<Field> cachedFields = new ArrayList<>();

    public ConfigFactory(Class<?> mainClass) {
        boolean bukkit = false;

        try {
            Class.forName("org.bukkit.plugin.java.JavaPlugin");
            bukkit = true;
        } catch (ClassNotFoundException ignored) { }

        if (bukkit)
            configAdapter = new BukkitConfigAdapter(JavaPlugin.getPlugin((Class<? extends JavaPlugin>) mainClass));
        else configAdapter = new BungeeConfigAdapter();

        try {
            getClasses(new File(mainClass.getProtectionDomain().getCodeSource().getLocation().toURI()), mainClass.getPackage().getName()).forEach(aClass -> {
                Arrays.stream(aClass.getFields()).filter(field -> field.isAnnotationPresent(Configurable.class)).forEach(field -> {
                    final Configurable configurable = field.getAnnotation(Configurable.class);
                    final String fileName = configurable.fileName();
                    String path = configurable.path();

                    if (path.length() == 0)
                        path = field.getName();

                    try {
                        if (!field.isAccessible())
                            field.setAccessible(true);
                        if (configAdapter.isSet(fileName, path))
                            field.set(field.getClass(), configAdapter.get(fileName, path));
                        else configAdapter.set(fileName, path, field.get(field.getClass()));
                        if (configurable.save())
                            cachedFields.add(field);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            });
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public void save() {
        cachedFields.forEach(field -> {
            final Configurable configurable = field.getAnnotation(Configurable.class);
            final String fileName = configurable.fileName();
            String path = configurable.path();

            if (path.length() == 0)
                path = field.getName();

            try {
                configAdapter.set(fileName, path, field.get(field.getClass()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static Set<Class<?>> getClasses(File jarFile, String packageName) {
        final Set<Class<?>> classes = new HashSet<>();

        try (final JarFile file = new JarFile(jarFile)) {
            for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements(); ) {
                final JarEntry jarEntry = entry.nextElement();
                final String name = jarEntry.getName().replace("/", ".");
                if (name.startsWith(packageName) && name.endsWith(".class"))
                    classes.add(Class.forName(name.substring(0, name.length() - 6)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
