package com.dynxsty.linkchecker;

import com.dynxsty.dih4jda.DIH4JDA;
import com.dynxsty.dih4jda.DIH4JDABuilder;
import com.dynxsty.linkchecker.listener.StateListener;
import com.dynxsty.linkchecker.properties.ConfigElement;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.time.ZoneOffset;
import java.util.TimeZone;

@Slf4j
public class Bot {
	public static DIH4JDA dih4jda;

	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
		JDA jda = JDABuilder.createDefault(new ConfigElement<>("token", String.class).getValue())
				.setMemberCachePolicy(MemberCachePolicy.ALL)
				.enableCache(CacheFlag.ACTIVITY)
				.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
				.build();
		dih4jda = DIH4JDABuilder
				.setJDA(jda)
				.setCommandsPackage("com.dynxsty.linkchecker.commands")
				.build();
		jda.addEventListener(new StateListener());
	}
}

