package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.config.ConfigCommandHandler;
import com.dynxsty.linkchecker.properties.ConfigInt;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SetConfigInterval implements ConfigCommandHandler {
    @Override
    public void execute(SlashCommandEvent event) {

        new ConfigInt("interval").setValue((int) event.getOption("int").getAsLong());
        event.getHook().sendMessageEmbeds(new Config().configEmbed(
                "Interval",
                event.getOption("int").getAsString()
        )).setEphemeral(true).queue();
    }
}
