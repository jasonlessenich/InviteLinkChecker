package com.dynxsty.linkchecker.listener;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.check.LinkChecker;
import com.dynxsty.linkchecker.commands.CheckLink;
import com.dynxsty.linkchecker.commands.config.Config;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SlashCommand extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SlashCommand.class);

    void registerSlashCommands(Guild guild) {

        CommandListUpdateAction updateAction = guild.updateCommands();

        updateAction.addCommands(
                new CommandData("config", "the config")
                        .addSubcommands(
                                new SubcommandData("list", "shows the current bot configuration"),
                                new SubcommandData("token", "changes the current bot-token (requires restart)").addOption(STRING, "token", "the new bot token", true),
                                new SubcommandData("time-unit", "changes the time unit (requires restart)").addOption(STRING, "unit", "the new time unit", true),
                                new SubcommandData("interval", "changes the interval (requires restart)").addOption(INTEGER, "int", "the new check-interval", true),
                                new SubcommandData("invite-code", "changes the invite-code").addOption(STRING, "code", "the new invite code", true),
                                new SubcommandData("reset-tcc", "resets the total check count")),
                new CommandData("check-link", "manually check if the invite link is taken").addOption(STRING, "code", "the invite code", false));

        for (var cmd : guild.retrieveCommands().complete()) {

            logger.info("{}[{}]{} Updating Privileges for Command {}",
                    Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET, cmd.getName());
            if (cmd.getName().equals("check-link")) continue;

            cmd.updatePrivileges(guild, CommandPrivilege.enableUser(
                    new ConfigString("owner_id").getValue()),
                    CommandPrivilege.disableRole(guild.getId())).complete();
        }

        updateAction.queue();
    }

    @Override
    public void onReady(ReadyEvent event) {

        new Bot().listConfig();
        for (var guild : event.getJDA().getGuilds()) registerSlashCommands(guild);
        new LinkChecker().startCheck(event.getJDA());
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        event.deferReply().setEphemeral(true).queue();

        try {
            if (event.getSubcommandName() == null) logger.info("{} used /{}", event.getUser().getAsTag(), event.getName());
            else logger.info("{} used /{} {}", event.getUser().getAsTag(), event.getName(), event.getSubcommandName());

        switch (event.getName()) {

            case "config": new Config().execute(event); break;
            case "check-link": new CheckLink().onCheckLink(event); break;
            default: logger.error("Unknown Command! {} used /{}", event.getUser().getAsTag(), event.getName());
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
