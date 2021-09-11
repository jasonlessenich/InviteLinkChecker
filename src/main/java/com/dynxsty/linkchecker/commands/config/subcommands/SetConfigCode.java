package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SetConfigCode extends GuildSlashSubCommand implements SlashCommandHandler {

    public SetConfigCode () {
        this.subcommandData = new SubcommandData("invite-code", "changes the invite-code")
                .addOption(STRING, "code", "the new invite code", true);
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        new ConfigString("code").setValue(event.getOption("code").getAsString().toLowerCase());
        return event.replyEmbeds(new Config(event.getGuild()).configEmbed(
                "Invite Code",
                event.getOption("code").getAsString().toLowerCase()
        )).setEphemeral(true);
    }
}

