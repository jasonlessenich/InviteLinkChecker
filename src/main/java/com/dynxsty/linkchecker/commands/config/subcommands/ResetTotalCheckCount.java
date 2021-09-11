package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import com.dynxsty.linkchecker.properties.ConfigInt;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class ResetTotalCheckCount extends GuildSlashSubCommand implements SlashCommandHandler {

    public ResetTotalCheckCount () {
        this.subcommandData = new SubcommandData("reset-tcc", "resets the total check count");
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        new ConfigInt("totalCheckCount").setValue(0);
        return event.replyEmbeds(new Config(event.getGuild()).configEmbed(
                "Total Check Count",
                "0"
        )).setEphemeral(true);
    }
}
