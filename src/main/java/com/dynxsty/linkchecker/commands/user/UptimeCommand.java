package com.dynxsty.linkchecker.commands.user;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.utils.StringUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class UptimeCommand extends SlashCommand {
	public UptimeCommand() {
		setSlashCommandData(Commands.slash("uptime", "Checks the Bot's Uptime"));
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		event.replyEmbeds(new EmbedBuilder()
				.setColor(Constants.EMBED_GRAY)
				.setAuthor(StringUtils.formatUptime(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
				.build()
		).queue();
	}
}
