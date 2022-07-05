package com.dynxsty.linkchecker.commands.config_commands.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ListConfigSubcommand extends SlashCommand.Subcommand {
	public ListConfigSubcommand() {
		setSubcommandData(new SubcommandData("list", "Shows the current Bot Configuration."));
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		MessageEmbed embed = new EmbedBuilder()
				.setColor(Constants.EMBED_GRAY)
				.setAuthor("Bot Configuration", null, Bot.dih4jda.getConfig().getJDA().getSelfUser().getEffectiveAvatarUrl())
				.addField("Interval", "```" + new ConfigElement<>("interval", Integer.class).getValue() + " "
						+ new ConfigElement<>("timeunit", TimeUnit.class).getValue().name().toLowerCase() + "```", true)
				.addField("Invite Code", "```discord.gg/" + new ConfigElement<>("code", String.class).getValue() + "```", true)
				.addField("Total Check Count", "```" + new ConfigElement<>("totalCheckCount", Integer.class).getValue() + "```", true)
				.setTimestamp(new Date().toInstant())
				.build();
		event.replyEmbeds(embed).setEphemeral(true).queue();
	}
}
