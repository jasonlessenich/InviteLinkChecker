package com.dynxsty.linkchecker.commands.config_commands.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.commands.config_commands.ConfigCommand;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class SetConfigIntervalSubcommand extends SlashCommand.Subcommand {
	public SetConfigIntervalSubcommand() {
		setSubcommandData(new SubcommandData("interval", "Allows to change the Interval. (Requires Restart)")
				.addOption(INTEGER, "interval", "What Interval should be used to schedule the LinkChecker?", true)
		);
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		OptionMapping intervalMapping = event.getOption("interval");
		if (intervalMapping == null) {
			event.reply("Missing required arguments.").queue();
			return;
		}
		int interval = intervalMapping.getAsInt();
		new ConfigElement<>("interval", Integer.class).setValue(interval);
		event.replyEmbeds(ConfigCommand.buildConfigEmbed("Interval", String.valueOf(interval)))
				.setEphemeral(true)
				.queue();
	}
}
