package com.dynxsty.linkchecker.check;

import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.properties.ConfigInt;
import com.dynxsty.linkchecker.properties.ConfigString;
import com.dynxsty.linkchecker.properties.ConfigTimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkChecker extends ListenerAdapter {

    private static ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();
    private static final Logger logger = LoggerFactory.getLogger(LinkChecker.class);

    void incrementTotalCount() {

        int i = new ConfigInt("totalCheckCount", 0).getValue();
        new ConfigInt("totalCheckCount", 0).setValue(i + 1);
    }

    void updatePresence(ReadyEvent event, AtomicInteger checkCount, int interval, String unit) {

        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptimeMS = rb.getUptime();

        long uptimeDAYS = TimeUnit.MILLISECONDS.toDays(uptimeMS);
        uptimeMS -= TimeUnit.DAYS.toMillis(uptimeDAYS);
        long uptimeHRS = TimeUnit.MILLISECONDS.toHours(uptimeMS);
        uptimeMS -= TimeUnit.HOURS.toMillis(uptimeHRS);
        long uptimeMIN = TimeUnit.MILLISECONDS.toMinutes(uptimeMS);
        uptimeMS -= TimeUnit.MINUTES.toMillis(uptimeMIN);
        long uptimeSEC = TimeUnit.MILLISECONDS.toSeconds(uptimeMS);

        incrementTotalCount();

        event.getJDA().getPresence().setActivity(Activity.watching("discord.gg/" + new ConfigString("code", "java").getValue()
                + " every " + interval + " " + unit + " | Check #" + checkCount + " (" + new ConfigInt("totalCheckCount", 0).getValue() + " total)"
                + " | " + uptimeDAYS + "d " + uptimeHRS + "h " + uptimeMIN + "min " + uptimeSEC + "s"));
    }

    @Override
    public void onReady(ReadyEvent event) {

        AtomicInteger i = new AtomicInteger();
        int interval = new ConfigInt("interval", 5).getValue();
        String timeUnit = new ConfigTimeUnit("timeunit", TimeUnit.MINUTES).getValue().name().toLowerCase();

        threadPool.scheduleWithFixedDelay(() -> {

            i.getAndIncrement();
            updatePresence(event, i, interval, timeUnit);

            String code = new ConfigString("code", "java").getValue();

            try {

                Invite invite = Invite.resolve(event.getJDA(), code).complete();
                logger.info("discord.gg/" +  code + " is taken (" + invite.getGuild().getName() + ", " + invite.getGuild().getId() + "). Next check in " + interval + " "
                        + timeUnit + ".");

            } catch (ErrorResponseException e) {

                if (!e.getMessage().equals("10006: Unknown Invite")) {
                    logger.warn(e.getClass().getSimpleName() + ": \"" + e.getMessage() + "\"");
                    return;
                }

                logger.warn(e.getClass().getSimpleName() + ": \"" + e.getMessage() + "\": discord.gg/" +  code + " might be available!");

                var embed = new EmbedBuilder()
                        .setColor(Constants.EMBED_GRAY)
                        .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setAuthor(e.getClass().getSimpleName(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("```" + e.getMessage() + "\n⋅ discord.gg/" +  code + " might be available!```")
                        .setTimestamp(new Date().toInstant())
                        .build();

                event.getJDA().getGuildById(new ConfigString("guild_id", "null").getValue())
                        .getTextChannelById(new ConfigString("text_id", "null").getValue())
                        .sendMessage(new ConfigString("link_available_msg", "null").getValue())
                        .setEmbeds(embed).queue();
            }
        }, 0, new ConfigInt("interval", 5).getValue(), new ConfigTimeUnit("timeunit", TimeUnit.MINUTES).getValue());
    }
}
