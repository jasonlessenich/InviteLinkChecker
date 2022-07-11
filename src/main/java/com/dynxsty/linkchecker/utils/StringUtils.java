package com.dynxsty.linkchecker.utils;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.data.BotConfiguration;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

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
