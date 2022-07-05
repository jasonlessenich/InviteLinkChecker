package com.dynxsty.linkchecker.commands.user_commands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class BotInfoCommand extends SlashCommand {
	public BotInfoCommand() {
		setSlashCommandData(Commands.slash("botinfo", "Displays some information about the Bot."));
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		SelfUser self = event.getJDA().getSelfUser();
		event.replyEmbeds(new EmbedBuilder()
				.setAuthor(self.getAsTag(), Constants.GITHUB_LINK, self.getEffectiveAvatarUrl())
				.setThumbnail(self.getEffectiveAvatarUrl())
				.setColor(Constants.EMBED_GRAY)
				.addField("Name", "```" + self.getAsTag() + "```", false)
				.addField("Library", "```net.dv8tion:JDA:5.0.0-alpha.13```", true)
				.addField("JDK", "```" + System.getProperty("java.version") + "```", true)
				.addField("Uptime", "```" + StringUtils.formatUptime() + "```", true)
				.build()
		).addActionRow(
				Button.link(Constants.GITHUB_LINK, "View on GitHub"),
				Button.link("https://github.com/DV8FromTheWorld/JDA/", "JDA")
		).queue();
	}
}
