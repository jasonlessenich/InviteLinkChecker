package com.dynxsty.linkchecker.listener;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.check.LinkChecker;
import com.dynxsty.linkchecker.data.SystemsConfig;
import com.dynxsty.linkchecker.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StateListener extends ListenerAdapter {
	@Override
	public void onReady(@NotNull ReadyEvent event) {
		StringUtils.listConfig(event.getJDA());
		SystemsConfig.TimerConfig timer = Bot.config.getTimerConfig();
		if (Arrays.asList(TimeUnit.values()).stream().noneMatch(t -> t.toString().equals(timer.getTimeUnit()))) {
			log.error("\"{}\" is not a valid enum of java.util.concurrent.TimeUnit!", timer.getTimeUnit());
			System.exit(0);
		}
		LinkChecker.startCheck(event.getJDA(), timer.getInterval(), TimeUnit.valueOf(timer.getTimeUnit()));
	}
}
