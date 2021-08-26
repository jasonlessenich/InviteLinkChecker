package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.config.ConfigCommandHandler;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SetConfigToken implements ConfigCommandHandler {
    @Override
    public void execute(SlashCommandEvent event) {

        new ConfigString("token", "null").setValue(event.getOption("token").getAsString());
        event.getHook().sendMessageEmbeds(new Config().configEmbed(
                "Token",
                event.getOption("token").getAsString()
        )).setEphemeral(true).queue();
    }
}
