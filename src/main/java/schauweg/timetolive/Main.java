package schauweg.timetolive;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import schauweg.timetolive.config.TTLConfigManger;

public class Main implements ClientModInitializer {

    public static final String MOD_ID = "timetolive";
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    @Override
    public void onInitializeClient() {

        TTLConfigManger.initializeConfig();
    }
}
