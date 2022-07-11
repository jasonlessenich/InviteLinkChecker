package com.dynxsty.linkchecker.check;

import com.dynxsty.linkchecker.Bot;
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

	public static void startCheck(JDA jda, int interval, TimeUnit unit) {
		threadPool.scheduleWithFixedDelay(() -> {
			checks++;
			String code = Bot.config.getInviteCode();
			if (checkLink(jda, code)) {
				Invite.resolve(jda, code, true).queue(invite ->
						log.info("(#{}) discord.gg/{} is taken by: {} ({}, {} members, {} online)",
								checks, code, invite.getGuild().getName(), invite.getGuild().getId(),
								invite.getGuild().getMemberCount(), invite.getGuild().getOnlineCount()));
			} else {
				log.warn("discord.gg/{} is available!", code);
				//MessageEmbed embed = new EmbedBuilder()
				//		.setColor(Constants.EMBED_GRAY)
				//		.setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
				//		.setDescription("discord.gg/" + code + " is available!")
				//		.setTimestamp(Instant.now())
				//		.build();
				//jda.getGuildById(new ConfigElement<>("guild_id", Long.class).getValue())
				//		.getTextChannelById(new ConfigElement<>("text_id", Long.class).getValue())
				//		.sendMessage(new ConfigElement<>("link_available_msg", String.class).getValue())
				//		.setEmbeds(embed)
				//		.queue();
			}
		}, 0, interval, unit);
	}

	public static boolean checkLink(JDA jda, String code) {
		try {
			Invite invite = Invite.resolve(jda, code).complete();
			return invite != null;
		} catch (ErrorResponseException e) {
			return !e.getMessage().equals("10006: Unknown Invite");
		}
	}
}
