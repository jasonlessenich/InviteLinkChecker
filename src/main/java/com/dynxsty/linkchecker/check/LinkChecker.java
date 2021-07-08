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

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkChecker extends ListenerAdapter {

    private static ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    String timeStamp () {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return "[" + dateFormat.format(new Date()) + "] ";
    }

    void incrementTotalCount() {

        int i = new ConfigInt("totalCheckCount", 0).getValue();
        new ConfigInt("totalCheckCount", 0).setValue(i + 1);
    }

    void updatePresence(ReadyEvent event, AtomicInteger checkCount) {

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
                + " | Check #" + checkCount + " (" + new ConfigInt("totalCheckCount", 0).getValue() + " total)"
                + " | " + uptimeDAYS + "d " + uptimeHRS + "h " + uptimeMIN + "min " + uptimeSEC + "s"));
    }

    @Override
    public void onReady(ReadyEvent event) {

        AtomicInteger i = new AtomicInteger();

        threadPool.scheduleWithFixedDelay(() -> {

            i.getAndIncrement();

            String code = new ConfigString("code", "java").getValue();
            updatePresence(event, i);
            System.out.println(timeStamp() + "Checking... (#" + i + ")");

            try {

                Invite.resolve(event.getJDA(), code).complete();
                System.out.println(timeStamp() + "Link (discord.gg/" +  code + ") is taken. Trying again in " + new ConfigInt("interval", 5).getValue() + " " + new ConfigTimeUnit("timeunit", TimeUnit.MINUTES).getValue().name().toLowerCase());

            } catch (ErrorResponseException e) {

                System.out.println(timeStamp() + "Exception triggered! Link (discord.gg/" +  code + ") might be available!");

                var embed = new EmbedBuilder()
                        .setAuthor("ErrorResponseException: " + e.getMeaning(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setColor(Constants.GRAY)
                        .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("```" + timeStamp() + "Exception triggered!\nLink (discord.gg/" +  code + ") might be available!```")
                        .setTimestamp(new Date().toInstant())
                        .build();

                event.getJDA().getGuilds().get(0).getTextChannelById("711245550271594556")
                        .sendMessage("<@810481402390118400> <@299555811804315648>").setEmbeds(embed).queue();
            }
        }, 0, new ConfigInt("interval", 5).getValue(), new ConfigTimeUnit("timeunit", TimeUnit.MINUTES).getValue());
    }
}
