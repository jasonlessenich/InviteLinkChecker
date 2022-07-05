package com.dynxsty.linkchecker.listener;

import com.dynxsty.linkchecker.check.LinkChecker;
import com.dynxsty.linkchecker.properties.ConfigElement;
import com.dynxsty.linkchecker.utils.StringUtils;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class StateListener extends ListenerAdapter {
	@Override
	public void onReady(@NotNull ReadyEvent event) {
		StringUtils.listConfig(event.getJDA());
		LinkChecker.startCheck(event.getJDA(),
				new ConfigElement<>("interval", Integer.class).getValue(),
				new ConfigElement<>("timeunit", TimeUnit.class).getValue());
	}
}
