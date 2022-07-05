package com.dynxsty.linkchecker.commands.config_commands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.config_commands.subcommands.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class ConfigCommand extends SlashCommand {
	public ConfigCommand() {
		setSlashCommandData(Commands.slash("config", "the config"));
		addSubcommands(new ListConfigSubcommand(), new ResetTotalCheckCountSubcommand(), new SetConfigCodeSubcommand(),
				new SetConfigIntervalSubcommand(), new SetConfigTimeUnitSubcommand()
		);
	}

	public static @NotNull MessageEmbed buildConfigEmbed(String configName, String newValue) {
		return new EmbedBuilder()
				.setColor(Constants.EMBED_GRAY)
				.setAuthor("Config: " + configName, null, Bot.dih4jda.getConfig().getJDA().getSelfUser().getEffectiveAvatarUrl())
				.setDescription("Successfully set ``" + configName + "`` to ``" + newValue + "``")
				.setTimestamp(Instant.now())
				.build();
	}
}
