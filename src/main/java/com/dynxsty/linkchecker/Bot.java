package com.dynxsty.linkchecker;

import com.dynxsty.linkchecker.listener.InteractionListener;
import com.dynxsty.linkchecker.listener.SlashCommand;
import com.dynxsty.linkchecker.properties.ConfigInt;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.ZoneOffset;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Bot {

    public static JDA jda;

    public static long unixTime;

    public static void main(String[] args) throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
        unixTime = System.currentTimeMillis() / 1000L;

        jda = JDABuilder.createDefault(new ConfigString("token").getValue())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(new SlashCommand(), new InteractionListener())
                .build();
    }

    public void listConfig() {

        System.out.printf(
                "\n     [*] Logged in as " + Constants.TEXT_WHITE + jda.getSelfUser().getAsTag() + Constants.TEXT_RESET +
                "\n     Token: " + new ConfigString("token").getValue() +
                "\n     Owner: " +  jda.retrieveUserById(new ConfigString("owner_id").getValue()).complete().getAsTag() +
                "\n     Invite Link: discord.gg/" + new ConfigString("code").getValue() +
                "\n     Interval: %s %s (%d checks total)" +
                "\n\n",
                new ConfigString("interval").getValue(), new ConfigString("timeunit").getValue(),
                new ConfigInt("totalCheckCount").getValue()
        );
    }

    public void updatePresence(JDA jda, AtomicInteger checkCount, int interval, String unit) {
        jda.getPresence().setActivity(Activity.watching("discord.gg/" + new ConfigString("code").getValue()
                + " every " + interval + " " + unit + " | Check #" + checkCount
                + " (" + new ConfigInt("totalCheckCount").getValue() + " total)" + " | " + getUptime()));
    }

    public String getUptime() {

        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        long uptimeMS = rb.getUptime();

        long uptimeDAYS = TimeUnit.MILLISECONDS.toDays(uptimeMS);
        uptimeMS -= TimeUnit.DAYS.toMillis(uptimeDAYS);
        long uptimeHRS = TimeUnit.MILLISECONDS.toHours(uptimeMS);
        uptimeMS -= TimeUnit.HOURS.toMillis(uptimeHRS);
        long uptimeMIN = TimeUnit.MILLISECONDS.toMinutes(uptimeMS);
        uptimeMS -= TimeUnit.MINUTES.toMillis(uptimeMIN);
        long uptimeSEC = TimeUnit.MILLISECONDS.toSeconds(uptimeMS);

        return String.format("%sd %sh %smin %ss",
                uptimeDAYS, uptimeHRS, uptimeMIN, uptimeSEC);
    }

}

