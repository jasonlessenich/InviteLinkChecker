package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import com.dynxsty.linkchecker.properties.ConfigTimeUnit;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SetConfigTimeUnit extends GuildSlashSubCommand implements SlashCommandHandler {

    public SetConfigTimeUnit () {
        this.subcommandData = new SubcommandData("time-unit", "changes the time unit (requires restart)")
                .addOption(STRING, "unit", "the new time unit", true);
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        new ConfigTimeUnit("timeunit").setValue(TimeUnit.valueOf(event.getOption("unit").getAsString().toUpperCase()));
        return event.replyEmbeds(new Config(event.getGuild()).configEmbed(
                "Time Unit",
                event.getOption("unit").getAsString().toUpperCase()
        )).setEphemeral(true);
    }
}
