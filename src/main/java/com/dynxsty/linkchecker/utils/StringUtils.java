package com.dynxsty.linkchecker.utils;

import com.dynxsty.linkchecker.properties.ConfigElement;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StringUtils {

	public static void listConfig(@NotNull JDA jda) {
		log.info("""
						 
						                
						[*] Logged in as {}
						Owner: {}
						Invite Link: discord.gg/{}
						Interval: {} {} ({} checks total)           
						""", jda.getSelfUser().getAsTag(), new ConfigElement<>("owner_id", String.class).getValue(),
				new ConfigElement<>("code", String.class).getValue(), new ConfigElement<>("interval", String.class).getValue(),
				new ConfigElement<>("timeunit", TimeUnit.class).getValue(), new ConfigElement<>("totalCheckCount", Integer.class).getValue());
	}

	public static String formatUptime() {
		RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
		long uptimeMS = rb.getUptime();
		long uptimeDAYS = TimeUnit.MILLISECONDS.toDays(uptimeMS);
		uptimeMS -= TimeUnit.DAYS.toMillis(uptimeDAYS);
		long uptimeHRS = TimeUnit.MILLISECONDS.toHours(uptimeMS);
		uptimeMS -= TimeUnit.HOURS.toMillis(uptimeHRS);
		long uptimeMIN = TimeUnit.MILLISECONDS.toMinutes(uptimeMS);
		uptimeMS -= TimeUnit.MINUTES.toMillis(uptimeMIN);
		long uptimeSEC = TimeUnit.MILLISECONDS.toSeconds(uptimeMS);
		return String.format("%sd %sh %smin %ss",
				uptimeDAYS, uptimeHRS, uptimeMIN, uptimeSEC);
	}
}
