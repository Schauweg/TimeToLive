package schauweg.timetolive.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.Function;

public class TTLConfigScreen {

    public static Screen getScreen(Screen parent){
        TTLConfig config = TTLConfigManger.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableText("timetolive.config.menu"));

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("timetolive.config.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("timetolive.config.option.enable"), config.isOverlayActive())
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.setOverlayActive(newValue))
                .setYesNoTextSupplier(getYesNoSupplier("timetolive.config.option.enable.enabled", "timetolive.config.option.enable.disabled"))
                .build());


        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("timetolive.config.option.displayticks"), config.isDisplayInTicks())
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.setDisplayInTicks(newValue))
                .setYesNoTextSupplier(getYesNoSupplier("timetolive.config.option.displayticks.ticks", "timetolive.config.option.displayticks.seconds"))
                .build());

        builder.setSavingRunnable(() -> {
            TTLConfigManger.save();
        });

        return builder.build();
    }


    private static Function<Boolean, Text> getYesNoSupplier(String keyYes, String keyNo){
        return x ->{
            if (x)
                return new TranslatableText(keyYes);
            else
                return new TranslatableText(keyNo);
        };
    }

}
