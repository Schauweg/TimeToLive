package schauweg.timetolive.modmenu;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import schauweg.timetolive.config.TTLConfigScreen;


public class TTLModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return TTLConfigScreen::getScreen;
    }
}
