package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.config.ConfigCommandHandler;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class SetConfigCode implements ConfigCommandHandler {
    @Override
    public void execute(SlashCommandEvent event) {

        new ConfigString("code", "java").setValue(event.getOption("code").getAsString().toLowerCase());
        event.getHook().sendMessageEmbeds(new Config().configEmbed(
                "Invite Code",
                event.getOption("code").getAsString().toLowerCase()
        )).setEphemeral(true).queue();
    }
}

