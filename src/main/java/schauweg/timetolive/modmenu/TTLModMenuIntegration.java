package schauweg.timetolive.modmenu;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import schauweg.timetolive.config.TTLConfig;
import schauweg.timetolive.config.TTLConfigManger;
import schauweg.timetolive.config.TTLConfigScreen;


public class TTLModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            return TTLConfigScreen.getScreen(parent);
        };
    }
}
