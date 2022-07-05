package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.commands.config.ConfigCommand;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SetConfigCodeSubcommand extends SlashCommand.Subcommand {

	public SetConfigCodeSubcommand() {
		setSubcommandData(new SubcommandData("invite-code", "changes the invite-code")
				.addOption(STRING, "code", "the new invite code", true)
		);
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		OptionMapping codeMapping = event.getOption("code");
		if (codeMapping == null) {
			event.reply("Missing required arguments.").queue();
			return;
		}
		String code = codeMapping.getAsString().toLowerCase();
		new ConfigElement<>("code", String.class).setValue(code);
		event.replyEmbeds(ConfigCommand.buildConfigEmbed("Invite Code", code))
				.setEphemeral(true)
				.queue();
	}
}

