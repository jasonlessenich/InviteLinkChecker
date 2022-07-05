package com.dynxsty.linkchecker.check;

import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.properties.ConfigElement;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LinkChecker extends ListenerAdapter {
	private static final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

	private static void incrementTotalCount() {
		ConfigElement<Integer> totalCheckCount = new ConfigElement<>("totalCheckCount", Integer.class);
		totalCheckCount.setValue(totalCheckCount.getValue() + 1);
	}

	public static void startCheck(JDA jda, int interval, TimeUnit unit) {
		AtomicInteger i = new AtomicInteger();
		threadPool.scheduleWithFixedDelay(() -> {
			incrementTotalCount();
			i.getAndIncrement();
			String code = new ConfigElement<>("code", String.class).getValue();
			try {
				Invite invite = Invite.resolve(jda, code).complete();
				log.info("{}[{}/{}]{} discord.gg/{} is taken ({}, {})",
						Constants.TEXT_WHITE, i, new ConfigElement<>("totalCheckCount", Integer.class).getValue(), Constants.TEXT_RESET,
						code, invite.getGuild().getName(), invite.getGuild().getId());
			} catch (ErrorResponseException e) {
				if (!e.getMessage().equals("10006: Unknown Invite")) {
					log.warn(e.getClass().getSimpleName() + ": \"" + e.getMessage() + "\"");
					return;
				}
				log.warn(e.getClass().getSimpleName() + ": \"" + e.getMessage() + "\": discord.gg/{} is available!", code);
				MessageEmbed embed = new EmbedBuilder()
						.setColor(Constants.EMBED_GRAY)
						.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
						.setAuthor(e.getClass().getSimpleName(), null, jda.getSelfUser().getEffectiveAvatarUrl())
						.setDescription("```" + e.getMessage() + "\n⋅ discord.gg/" + code + " is available!```")
						.setTimestamp(new Date().toInstant())
						.build();
				jda.getGuildById(new ConfigElement<>("guild_id", Long.class).getValue())
						.getTextChannelById(new ConfigElement<>("text_id", Long.class).getValue())
						.sendMessage("<@" + new ConfigElement<>("owner_id", Long.class).getValue() + ">")
						.setEmbeds(embed)
						.queue();
			}
		}, 0, interval, unit);
	}
}
