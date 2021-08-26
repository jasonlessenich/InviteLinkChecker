package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.config.ConfigCommandHandler;
import com.dynxsty.linkchecker.properties.ConfigInt;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class ResetTotalCheckCount implements ConfigCommandHandler {
    @Override
    public void execute(SlashCommandEvent event) {

        new ConfigInt("totalCheckCount", 0).setValue(0);
        event.getHook().sendMessageEmbeds(new Config().configEmbed(
                "Total Check Count",
                "0"
        )).setEphemeral(true).queue();
    }
}
