package com.dynxsty.linkchecker.check;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.properties.ConfigInt;
import com.dynxsty.linkchecker.properties.ConfigString;
import com.dynxsty.linkchecker.properties.ConfigTimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkChecker extends ListenerAdapter {

    private static ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
    private static final Logger logger = LoggerFactory.getLogger(LinkChecker.class);

    void incrementTotalCount() {

        int i = new ConfigInt("totalCheckCount").getValue();
        new ConfigInt("totalCheckCount").setValue(i + 1);
    }

    public void startCheck(JDA jda) {

        AtomicInteger i = new AtomicInteger();
        int interval = new ConfigInt("interval").getValue();
        String timeUnit = new ConfigTimeUnit("timeunit").getValue().name().toLowerCase();

        threadPool.scheduleWithFixedDelay(() -> {

            i.getAndIncrement();
            incrementTotalCount();

            new Bot().updatePresence(jda, i, interval, timeUnit);

            String code = new ConfigString("code").getValue();

            try {
                Invite invite = Invite.resolve(jda, code).complete();
                logger.info("{}[{}/{}]{} discord.gg/{} is taken ({}, {})",
                        Constants.TEXT_WHITE, i, new ConfigInt("totalCheckCount").getValue(), Constants.TEXT_RESET,
                        code, invite.getGuild().getName(), invite.getGuild().getId());

            } catch (ErrorResponseException e) {

                if (!e.getMessage().equals("10006: Unknown Invite")) {
                    logger.warn(e.getClass().getSimpleName() + ": \"" + e.getMessage() + "\"");
                    return;
                }

                logger.warn(e.getClass().getSimpleName() + ": \"" + e.getMessage() + "\": discord.gg/{} is available!", code);

                var embed = new EmbedBuilder()
                        .setColor(Constants.EMBED_GRAY)
                        .setThumbnail(jda.getSelfUser().getEffectiveAvatarUrl())
                        .setAuthor(e.getClass().getSimpleName(), null, jda.getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("```" + e.getMessage() + "\n⋅ discord.gg/" +  code + " is available!```")
                        .setTimestamp(new Date().toInstant())
                        .build();

                jda.getGuildById(new ConfigString("guild_id").getValue())
                        .getTextChannelById(new ConfigString("text_id").getValue())
                        .sendMessage("<@" + new ConfigString("owner_id").getValue() + ">")
                        .setEmbeds(embed).queue();
            }
        }, 5, new ConfigInt("interval").getValue(), new ConfigTimeUnit("timeunit").getValue());
    }
}
