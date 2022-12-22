package schauweg.timetolive.config;

import net.fabricmc.loader.api.FabricLoader;
import schauweg.timetolive.Main;

import java.io.*;

public class TTLConfigManger {

    private static File file;
    private static TTLConfig config;

    private static void prepareConfigFile() {
        if (file != null) {
            return;
        }
        file = FabricLoader.getInstance().getConfigDir().resolve(Main.MOD_ID+".json").toFile();
    }

    public static TTLConfig initializeConfig() {
        if (config != null) {
            return config;
        }

        config = new TTLConfig();
        load();

        return config;
    }

    private static void load() {
        prepareConfigFile();

        try {
            if (!file.exists()) {
                save();
            }
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));

                TTLConfig parsed = Main.GSON.fromJson(br, TTLConfig.class);
                if (parsed != null) {
                    config = parsed;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load Login Toast configuration file; reverting to defaults");
            e.printStackTrace();
        }
    }

    public static void save() {
        prepareConfigFile();

        String jsonString = Main.GSON.toJson(config);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            System.err.println("Couldn't save Login Toast configuration file");
            e.printStackTrace();
        }
    }

    public static TTLConfig getConfig() {
        if (config == null) {
            config = new TTLConfig();
        }
        return config;
    }
}
