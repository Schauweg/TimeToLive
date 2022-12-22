package schauweg.timetolive.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Function;

public class TTLConfigScreen {

    public static Screen getScreen(Screen parent){
        TTLConfig config = TTLConfigManger.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("timetolive.config.menu"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("timetolive.config.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("timetolive.config.option.enable"), config.isOverlayActive())
                .setDefaultValue(true)
                .setSaveConsumer(config::setOverlayActive)
                .setYesNoTextSupplier(getYesNoSupplier("timetolive.config.option.enable.enabled", "timetolive.config.option.enable.disabled"))
                .build());


        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("timetolive.config.option.displayticks"), config.isDisplayInTicks())
                .setDefaultValue(false)
                .setSaveConsumer(config::setDisplayInTicks)
                .setYesNoTextSupplier(getYesNoSupplier("timetolive.config.option.displayticks.ticks", "timetolive.config.option.displayticks.seconds"))
                .build());

        builder.setSavingRunnable(TTLConfigManger::save);

        return builder.build();
    }


    private static Function<Boolean, Text> getYesNoSupplier(String keyYes, String keyNo){
        return x ->{
            if (x)
                return Text.translatable(keyYes);
            else
                return Text.translatable(keyNo);
        };
    }

}
