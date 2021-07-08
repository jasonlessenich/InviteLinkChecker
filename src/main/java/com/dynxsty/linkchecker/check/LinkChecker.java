package com.dynxsty.linkchecker.check;

import com.dynxsty.linkchecker.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkChecker extends ListenerAdapter {

    private static ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onReady(ReadyEvent event) {

        AtomicInteger i = new AtomicInteger();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        threadPool.scheduleWithFixedDelay(() -> {

            i.getAndIncrement();
            event.getJDA().getPresence().setActivity(Activity.watching("Check #" + i));
            System.out.println("[" + dateFormat.format(new Date()) + "] Checking... (#" + i + ")");

            try {

                Invite.resolve(event.getJDA(), Constants.code).complete();
                System.out.println("[" + dateFormat.format(new Date()) + "] Link (discord.gg/" +  Constants.code + ") is taken. Trying again in " + Constants.value + " " + Constants.timeUnit.name().toLowerCase());

            } catch (ErrorResponseException e) {

                System.out.println("[" + dateFormat.format(new Date()) + "] Exception triggered! Link (discord.gg/" +  Constants.code + ") might be available!");

                var embed = new EmbedBuilder()
                        .setAuthor("ErrorResponseException: " + e.getMeaning(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setColor(Constants.GRAY)
                        .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setDescription("```[" + dateFormat.format(new Date()) + "] Exception triggered!\nLink (discord.gg/" +  Constants.code + ") might be available!```")
                        .setTimestamp(new Date().toInstant())
                        .build();

                event.getJDA().getGuilds().get(0).getTextChannelById("711245550271594556")
                        .sendMessage("<@810481402390118400> <@299555811804315648>").setEmbeds(embed).queue();
            }
        }, 0, Constants.value, Constants.timeUnit);
    }
}
