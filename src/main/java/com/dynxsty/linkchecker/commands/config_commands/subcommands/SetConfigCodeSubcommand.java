package com.dynxsty.linkchecker.commands.config_commands.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.commands.config_commands.ConfigCommand;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SetConfigCodeSubcommand extends SlashCommand.Subcommand {
	public SetConfigCodeSubcommand() {
		setSubcommandData(new SubcommandData("invite-code", "Allows to change the Invite Code which should be checked.")
				.addOption(STRING, "code", "What Invite Code should be checked? (e.g 'python' for discord.gg/python)", true)
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

