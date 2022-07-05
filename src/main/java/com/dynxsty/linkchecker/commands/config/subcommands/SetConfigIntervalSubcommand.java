package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.commands.config.ConfigCommand;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;

public class SetConfigIntervalSubcommand extends SlashCommand.Subcommand {

	public SetConfigIntervalSubcommand() {
		setSubcommandData(new SubcommandData("interval", "changes the interval (requires restart)")
				.addOption(INTEGER, "interval", "the new check-interval", true)
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
