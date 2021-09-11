package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import com.dynxsty.linkchecker.properties.ConfigInt;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class SetConfigInterval extends GuildSlashSubCommand implements SlashCommandHandler {

    public SetConfigInterval () {
        this.subcommandData = new SubcommandData("interval", "changes the interval (requires restart)")
                .addOption(INTEGER, "int", "the new check-interval", true);
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        new ConfigInt("interval").setValue((int) event.getOption("int").getAsLong());
        return event.replyEmbeds(new Config(event.getGuild()).configEmbed(
                "Interval",
                event.getOption("int").getAsString()
        )).setEphemeral(true);
    }
}
