package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SetConfigToken extends GuildSlashSubCommand implements SlashCommandHandler {

    public SetConfigToken () {
        this.subcommandData = new SubcommandData("token", "changes the current bot-token (requires restart)")
                .addOption(STRING, "token", "the new bot token", true);
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        new ConfigString("token").setValue(event.getOption("token").getAsString());
        return event.replyEmbeds(new Config(event.getGuild()).configEmbed(
                "Token",
                event.getOption("token").getAsString()
        )).setEphemeral(true);
    }
}
