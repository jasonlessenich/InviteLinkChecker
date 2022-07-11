package com.dynxsty.linkchecker;

import com.dynxsty.linkchecker.data.BotConfigManager;
import com.dynxsty.linkchecker.data.BotConfiguration;
import com.dynxsty.linkchecker.listener.StateListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.nio.file.Path;
import java.time.ZoneOffset;
import java.util.TimeZone;

@Slf4j
public class Bot {
	/**
	 * A static reference to the bot's {@link BotConfiguration} which allows access
	 * to the bot's .yaml configuration.
	 */
	public static BotConfiguration config;

	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
		config = new BotConfigManager(Path.of("")).getConfig();
		JDA jda = JDABuilder.createLight(config.getBotToken()).build();
		jda.addEventListener(new StateListener());
	}
}

