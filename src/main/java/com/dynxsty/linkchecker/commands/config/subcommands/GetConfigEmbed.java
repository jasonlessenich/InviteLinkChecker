package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.config.ConfigCommandHandler;
import com.dynxsty.linkchecker.properties.ConfigInt;
import com.dynxsty.linkchecker.properties.ConfigString;
import com.dynxsty.linkchecker.properties.ConfigTimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GetConfigEmbed implements ConfigCommandHandler {
    @Override
    public void execute(SlashCommandEvent event) {

        var e = new EmbedBuilder()
                .setColor(Constants.EMBED_GRAY)
                .setAuthor("Bot Configuration", null, Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                .addField("Interval", "```" + new ConfigInt("interval", 5).getValue() + " "
                        + new ConfigTimeUnit("timeunit", TimeUnit.MINUTES).getValue().name().toLowerCase() + "```", true)

                .addField("Invite Code", "```discord.gg/" + new ConfigString("code", "java").getValue() + "```", true)
                .addField("Total Check Count", "```" + new ConfigInt("totalCheckCount", 0).getValue() + "```", true)
                .addField("Token", "```" + new ConfigString("token", "null").getValue() + "```", false)
                .setTimestamp(new Date().toInstant())
                .build();

        event.getHook().sendMessageEmbeds(e).setEphemeral(true).queue();
    }
}
