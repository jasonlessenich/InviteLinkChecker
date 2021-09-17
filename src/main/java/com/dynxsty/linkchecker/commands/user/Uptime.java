package com.dynxsty.linkchecker.commands.user;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.dao.GuildSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class Uptime extends GuildSlashCommand implements SlashCommandHandler {

    public Uptime () {
        this.commandData = new CommandData("uptime", "Checks the Bot's Uptime");
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        var e = new EmbedBuilder()
                .setColor(Constants.EMBED_GRAY)
                .setAuthor(new Bot().getUptime(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setDescription(String.format("<t:%s:d>, <t:%s:R>",
                        Bot.unixTime, Bot.unixTime))
                .build();

        return event.replyEmbeds(e);
    }
}
