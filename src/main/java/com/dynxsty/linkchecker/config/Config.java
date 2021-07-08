package com.dynxsty.linkchecker.config;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.properties.ConfigInt;
import com.dynxsty.linkchecker.properties.ConfigString;
import com.dynxsty.linkchecker.properties.ConfigTimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Config {

    MessageEmbed configEmbed (String configName, String newValue) {

        var e = new EmbedBuilder()
                .setColor(Constants.EMBED_GRAY)
                .setAuthor("Config: " + configName, null, Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Successfully changed ``" + configName + "`` to ``" + newValue + "``")
                .setTimestamp(new Date().toInstant())
                .build();

        return e;
    }

    public void onConfigList (SlashCommandEvent event) {

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

    public void onConfigToken (SlashCommandEvent event) throws Exception {

        new ConfigString("token", "null").setValue(event.getOption("token").getAsString());
        event.getHook().sendMessageEmbeds(configEmbed("Token", event.getOption("token").getAsString())).setEphemeral(true).queue();
    }

    public void onConfigTimeUnit (SlashCommandEvent event) throws Exception {

        new ConfigTimeUnit("timeunit", TimeUnit.MINUTES).setValue(TimeUnit.valueOf(event.getOption("unit").getAsString().toUpperCase()));
        event.getHook().sendMessageEmbeds(configEmbed("Time Unit", event.getOption("unit").getAsString().toUpperCase())).setEphemeral(true).queue();
    }

    public void onConfigInterval (SlashCommandEvent event) throws Exception {

        new ConfigInt("interval", 5).setValue((int) event.getOption("int").getAsLong());
        event.getHook().sendMessageEmbeds(configEmbed("Interval", event.getOption("int").getAsString())).setEphemeral(true).queue();
    }

    public void onConfigCode (SlashCommandEvent event) throws Exception {

        new ConfigString("code", "java").setValue(event.getOption("code").getAsString().toLowerCase());
        event.getHook().sendMessageEmbeds(configEmbed("Invite Code", event.getOption("code").getAsString().toLowerCase())).setEphemeral(true).queue();
    }

    public void onConfigResetTCC (SlashCommandEvent event) throws Exception {

        new ConfigInt("totalCheckCount", 0).setValue(0);
        event.getHook().sendMessageEmbeds(configEmbed("Total Check Count", "0")).setEphemeral(true).queue();
    }
}
