package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.config.ConfigCommandHandler;
import com.dynxsty.linkchecker.properties.ConfigTimeUnit;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.TimeUnit;

public class SetConfigTimeUnit implements ConfigCommandHandler {
    @Override
    public void execute(SlashCommandEvent event) {

        new ConfigTimeUnit("timeunit").setValue(TimeUnit.valueOf(event.getOption("unit").getAsString().toUpperCase()));
        event.getHook().sendMessageEmbeds(new Config().configEmbed(
                "Time Unit",
                event.getOption("unit").getAsString().toUpperCase()
        )).setEphemeral(true).queue();
    }
}
