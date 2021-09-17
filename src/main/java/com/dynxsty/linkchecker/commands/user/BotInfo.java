package com.dynxsty.linkchecker.commands.user;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.dao.GuildSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public class BotInfo extends GuildSlashCommand implements SlashCommandHandler {

    public BotInfo() {
        this.commandData = new CommandData("botinfo", "Displays some information about the Bot");
    }

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        User usr = event.getJDA().getSelfUser();

        var e = new EmbedBuilder()
                .setAuthor(usr.getAsTag(), Constants.GITHUB_LINK, usr.getEffectiveAvatarUrl())
                .setThumbnail(usr.getEffectiveAvatarUrl())
                .setColor(Constants.EMBED_GRAY)
                .addField("Name", "```" + usr.getAsTag() + "```", false)
                .addField("Library", "```JDA 4.3.0_324```",  true)
                .addField("JDK", "```" + System.getProperty("java.version") + "```", true)
                .addField("Uptime", "```" + new Bot().getUptime() + "```", true)
                .build();

        return event.replyEmbeds(e).addActionRow(
                Button.link(Constants.GITHUB_LINK, "View on GitHub"),
                Button.link("https://github.com/DV8FromTheWorld/JDA/", "JDA")
                );
    }
}
