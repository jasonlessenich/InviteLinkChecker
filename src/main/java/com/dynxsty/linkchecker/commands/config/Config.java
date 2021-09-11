package com.dynxsty.linkchecker.commands.config;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.config.subcommands.*;
import com.dynxsty.linkchecker.commands.dao.GuildSlashCommand;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

import java.util.Date;

public class Config extends GuildSlashCommand {

    public Config(Guild guild) {

        this.commandData = new CommandData("config", "the config");
        this.subCommandClasses = new Class[]{GetConfigEmbed.class, ResetTotalCheckCount.class, SetConfigCode.class,
                SetConfigInterval.class, SetConfigTimeUnit.class, SetConfigToken.class};

        this.commandPrivileges = new CommandPrivilege[]{
                CommandPrivilege.enableUser(new ConfigString("owner_id").getValue()),
                CommandPrivilege.disableRole(guild.getId())};
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
