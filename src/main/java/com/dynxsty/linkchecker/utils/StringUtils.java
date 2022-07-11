package com.dynxsty.linkchecker.utils;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import static com.dynxsty.linkchecker.Bot.config;

@Slf4j
public class StringUtils {
	private StringUtils() {}

	public static void listConfig(@NotNull JDA jda) {
		log.info("""
						 
						                
						[*] Logged in as {}
						Invite Link: discord.gg/{}
						Interval: {} {}           
						""", jda.getSelfUser().getAsTag(), config.getInviteCode(),
				config.getTimerConfig().getInterval(), config.getTimerConfig().getTimeUnit()
		);
	}
}
