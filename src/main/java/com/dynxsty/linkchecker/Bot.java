package com.dynxsty.linkchecker;

import com.dynxsty.linkchecker.listener.SlashCommand;
import com.dynxsty.linkchecker.properties.ConfigInt;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {

    public static JDA jda;

    public static void main(String[] args) throws Exception {

        jda = JDABuilder.createDefault(new ConfigString("token").getValue())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .addEventListeners(new SlashCommand())
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

}

