package com.dynxsty.linkchecker.listener;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.check.LinkChecker;
import com.dynxsty.linkchecker.config.Config;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SlashCommand extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SlashCommand.class);

    @Override
    public void onReady(ReadyEvent event) {

        CommandListUpdateAction updateAction = event.getJDA().getGuildById(new ConfigString("guild_id", "null").getValue()).updateCommands();

        updateAction.addCommands(
                new CommandData("config", "the config")
                        .addSubcommands(
                                new SubcommandData("list", "shows the current bot configuration"),
                                new SubcommandData("token", "changes the current bot-token (requires restart)").addOption(STRING, "token", "the new bot token", true),
                                new SubcommandData("time-unit", "changes the time unit (requires restart)").addOption(STRING, "unit", "the new time unit", true),
                                new SubcommandData("interval", "changes the interval (requires restart)").addOption(INTEGER, "int", "the new check-interval", true),
                                new SubcommandData("invite-code", "changes the invite-code").addOption(STRING, "code", "the new invite code", true),
                                new SubcommandData("reset-tcc", "resets the total check count")));
        updateAction.queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {



        if (!event.getMember().getId().equals(new ConfigString("owner_id", "null").getValue())) {

            logger.info(event.getUser().getAsTag() + " tried to use /" + event.getName() + " " + event.getSubcommandName());
            event.reply("Only ``" + event.getGuild().getMemberById(new ConfigString("owner_id", "null").getValue()).getUser().getAsTag() + "`` can execute this command").setEphemeral(true).queue();
            return;
        }

        event.deferReply().setEphemeral(true).queue();
        Config config = new Config();

        try {

            logger.info(event.getUser().getAsTag() + " used /" + event.getName() + " " + event.getSubcommandName());

        switch (event.getName()) {

            case "config":
                switch (event.getSubcommandName()) {
                    case "list": config.onConfigList(event); break;
                    case "token": config.onConfigToken(event); break;
                    case "time-unit": config.onConfigTimeUnit(event); break;
                    case "interval": config.onConfigInterval(event); break;
                    case "invite-code": config.onConfigCode(event); break;
                    case "reset-tcc": config.onConfigResetTCC(event); break;
                }
            }

        } catch (Exception e) {

            var embed = new EmbedBuilder()
                    .setColor(Constants.EMBED_GRAY)
                    .setAuthor(e.getClass().getSimpleName(), null, Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                    .setDescription("```" + e.getMessage() + "```")
                    .setTimestamp(new Date().toInstant())
                    .build();

            event.getHook().sendMessageEmbeds(embed).setEphemeral(true).queue();
        }
    }
}
