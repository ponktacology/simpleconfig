# simpleconfig
Simple config API for Bungee and Bukkit/Spigot

## Instalation

Add this to your pom.xml

```
	<repository>
		<id>jitpack.io</id>
		url>https://jitpack.io</url>
	</repository>
```

```
	<dependency>
	    <groupId>com.github.ponktacology</groupId>
	    <artifactId>simpleconfig</artifactId>
	    <version>aa6822f866</version>
	</dependency>
```


## Usage
```java
public class ExamplePlugin extends JavaPlugin {

	private ConfigFactory configFactory;
	
    @Override
    public void onEnable(){
        configFactory = new ConfigFactory(this.getClass());
    }
	
	@Override
	public void onDisable() {
		configFactory.save();
	}
}

public class ConfigClass {

	
	@Configurable //This will be saved/loaded from config.yml with "test" path
    private static String test;

    @Configurable(path = "different.path") //This will be saved/loaded from config.yml with "different.path" path
    private static String anotherTest;

    @Configurable(fileName = "customFile.yml") //This will be saved/loaded from customFile.yml with "anotherAnotherTest" path
    private static String anotherAnotherTest;
	
	@Configurable(save = false) //This will be only loaded but not saved (if you change it pragramatically it won't be saved to file)
	private static String anotherAnotherAnotherString;
}
```