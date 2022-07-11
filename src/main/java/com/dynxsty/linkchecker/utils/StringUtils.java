package com.dynxsty.linkchecker.utils;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import static com.dynxsty.linkchecker.Bot.config;

@Slf4j
public class StringUtils {
	private StringUtils() {
	}

	public static void listConfig(@NotNull JDA jda) {
		log.info("\n\n[*] Logged in as {}" +
						"\nInvite Link: discord.gg/{}" +
						"\nInterval: {} {}\n",
				jda.getSelfUser().getAsTag(), config.getInviteCode(),
				config.getTimerConfig().getInterval(), config.getTimerConfig().getTimeUnit()
		);
	}
}
