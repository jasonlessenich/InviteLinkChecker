package com.dynxsty.linkchecker.commands.user;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class CheckLinkCommand extends SlashCommand {
	public CheckLinkCommand() {
		setSlashCommandData(Commands.slash("check-link", "Manually checks if an Invite Link is taken")
				.addOption(STRING, "code", "The Invite Code", false)
		);
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		String code = event.getOption("code", new ConfigElement<>("code", String.class).getValue(), OptionMapping::getAsString);
		try {
			Invite invite = Invite.resolve(event.getJDA(), code).complete();
			event.replyEmbeds(new EmbedBuilder()
					.setTitle(invite.getGuild().getName())
					.setColor(Constants.EMBED_GRAY)
					.setThumbnail(invite.getGuild().getIconUrl())
					.addField("Name", "```" + invite.getGuild().getName() + "```", true)
					.addField("ID", "```" + invite.getGuild().getId() + "```", true)
					.setTimestamp(new Date().toInstant())
					.build()
			).queue();
		} catch (ErrorResponseException e) {
			if (e.getMessage().equals("10006: Unknown Invite")) {
				event.replyEmbeds(
						new EmbedBuilder()
								.setColor(Constants.EMBED_GRAY)
								.setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
								.setAuthor(e.getClass().getSimpleName(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
								.setDescription("```" + e.getMessage() + "\n⋅ discord.gg/" + code + " might be available!```")
								.setTimestamp(new Date().toInstant())
								.build()
				).queue();
			}
		}
	}
}
