package com.dynxsty.linkchecker.commands.config.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.commands.config.ConfigCommand;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SetConfigTimeUnitSubcommand extends SlashCommand.Subcommand {

	public SetConfigTimeUnitSubcommand() {
		setSubcommandData(new SubcommandData("time-unit", "changes the time unit (requires restart)")
				.addOptions(new OptionData(STRING, "unit", "the new time unit", true)
						.addChoice("Seconds", "SECONDS")
						.addChoice("Minutes", "MINUTES")
						.addChoice("Hours", "HOURS")
						.addChoice("Days", "DAYS"))
		);
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		OptionMapping timeUnitMapping = event.getOption("time-unit");
		if (timeUnitMapping == null) {
			event.reply("Missing required arguments.").queue();
			return;
		}
		TimeUnit timeUnit = TimeUnit.valueOf(event.getOption("time-unit").getAsString());
		new ConfigElement<>("timeunit", TimeUnit.class).setValue(timeUnit);
		event.replyEmbeds(ConfigCommand.buildConfigEmbed("Time Unit", timeUnit.toString()))
				.setEphemeral(true)
				.queue();
	}
}
