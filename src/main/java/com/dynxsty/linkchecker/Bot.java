package com.dynxsty.linkchecker;

import com.dynxsty.linkchecker.data.BotConfig;
import com.dynxsty.linkchecker.data.SystemsConfig;
import com.dynxsty.linkchecker.listener.StateListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Bot {
	/**
	 * A static reference to the bot's {@link SystemsConfig} which allows access
	 * to the bot's .yaml configuration.
	 */
	public static SystemsConfig config;

	public static void main(String[] args) throws Exception {
		config = new BotConfig(Paths.get("")).getConfig();
		JDA jda = JDABuilder.createLight(config.getBotToken()).build();
		jda.addEventListener(new StateListener());
	}
}

