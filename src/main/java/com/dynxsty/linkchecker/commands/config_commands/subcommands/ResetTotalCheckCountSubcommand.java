package com.dynxsty.linkchecker.commands.config_commands.subcommands;

import com.dynxsty.dih4jda.interactions.commands.SlashCommand;
import com.dynxsty.linkchecker.commands.config_commands.ConfigCommand;
import com.dynxsty.linkchecker.properties.ConfigElement;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

public class ResetTotalCheckCountSubcommand extends SlashCommand.Subcommand {
	public ResetTotalCheckCountSubcommand() {
		setSubcommandData(new SubcommandData("reset-count", "Resets the current total Check Count."));
	}

	@Override
	public void execute(@NotNull SlashCommandInteractionEvent event) {
		new ConfigElement<>("totalCheckCount", Integer.class).setValue(0);
		event.replyEmbeds(ConfigCommand.buildConfigEmbed("Total Check Count", "0"))
				.setEphemeral(true)
				.queue();
	}
}