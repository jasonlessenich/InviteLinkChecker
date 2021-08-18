package com.dynxsty.linkchecker;

import com.dynxsty.linkchecker.listener.SlashCommand;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {

    public static JDA jda;

    public static void main(String[] args) throws Exception {

        jda = JDABuilder.createDefault(new ConfigString("token", "null").getValue())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(new SlashCommand())
                .build();
    }

}

