package com.dynxsty.linkchecker;

import com.dynxsty.linkchecker.listener.StateListener;
import com.dynxsty.linkchecker.properties.ConfigElement;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.time.ZoneOffset;
import java.util.TimeZone;

@Slf4j
public class Bot {
	public static void main(String[] args) throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
		JDA jda = JDABuilder.createLight(new ConfigElement<>("token", String.class).getValue()).build();
		jda.addEventListener(new StateListener());
	}
}

