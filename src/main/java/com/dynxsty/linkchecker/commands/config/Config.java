package com.dynxsty.linkchecker.commands.config;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.config.subcommands.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Config implements ConfigCommandHandler {

    private final Map<String, ConfigCommandHandler> configIndex;

    public Config() {

        this.configIndex = new HashMap<>();

        configIndex.put("list", new GetConfigEmbed());
        configIndex.put("reset-tcc", new ResetTotalCheckCount());
        configIndex.put("invite-code", new SetConfigCode());
        configIndex.put("interval", new SetConfigInterval());
        configIndex.put("time-unit", new SetConfigTimeUnit());
        configIndex.put("token", new SetConfigToken());
    }

    @Override
    public void execute(SlashCommandEvent event) {
        var command = configIndex.get(event.getSubcommandName());
        if (command != null) command.execute(event);
    }

    public MessageEmbed configEmbed (String configName, String newValue) {

        var embed = new EmbedBuilder()
                .setColor(Constants.EMBED_GRAY)
                .setAuthor("Config: " + configName, null, Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Successfully set ``" + configName + "`` to ``" + newValue + "``")
                .setTimestamp(new Date().toInstant())
                .build();

        return embed;
    }
}
