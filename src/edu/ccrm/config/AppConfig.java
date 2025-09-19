package edu.ccrm.config;

/**
 * Simple Singleton configuration holder.
 */
public class AppConfig {

    private static AppConfig instance;
    private String dataFolder = "data/";

    private AppConfig() {
        // private constructor
    }

    public static synchronized AppConfig getInstance() { // thread-safeish
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(String dataFolder) {
        this.dataFolder = dataFolder;
    }
}
