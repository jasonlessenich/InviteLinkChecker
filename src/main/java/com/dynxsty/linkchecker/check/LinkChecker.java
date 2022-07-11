package com.dynxsty.linkchecker.check;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.data.SystemsConfig;
import com.dynxsty.linkchecker.utils.WebhookUtils;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LinkChecker extends ListenerAdapter {
	private static final ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
	private static int checks = 0;

	private final JDA jda;

	public LinkChecker(JDA jda, int interval, TimeUnit unit) {
		this.jda = jda;
		threadPool.scheduleWithFixedDelay(this::checkInviteLink, 0, interval, unit);
	}

	public static boolean isLinkTaken(JDA jda, String code) {
		try {
			Invite invite = Invite.resolve(jda, code).complete();
			return invite != null;
		} catch (ErrorResponseException e) {
			return !e.getMessage().equals("10006: Unknown Invite");
		}
	}

	private void checkInviteLink() {
		checks++;
		String code = Bot.config.getInviteCode().trim().toLowerCase();
		if (isLinkTaken(jda, code)) {
			Invite.resolve(jda, code, true).queue(invite ->
					log.info("(#{}) discord.gg/{} is taken by: {} ({}, {} members, {} online)",
							checks, code, invite.getGuild().getName(), invite.getGuild().getId(),
							invite.getGuild().getMemberCount(), invite.getGuild().getOnlineCount()));
		} else {
			log.warn("discord.gg/{} is available!", code);
			WebhookUtils.sendToAllWebhooks("discord.gg/%s is not taken!", code);
		}
	}
}
