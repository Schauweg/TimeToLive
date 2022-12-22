package schauweg.timetolive.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import schauweg.timetolive.config.TTLConfigScreen;


public class TTLModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TTLConfigScreen::getScreen;
    }
}
