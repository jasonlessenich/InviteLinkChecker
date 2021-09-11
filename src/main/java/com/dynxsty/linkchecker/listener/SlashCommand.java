package com.dynxsty.linkchecker.listener;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.check.LinkChecker;
import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.dao.GuildSlashCommand;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class SlashCommand extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SlashCommand.class);
    private HashMap<String, SlashCommandHandler> slashCommands;
    private HashMap<String, CommandPrivilege[]> slashPrivileges;

    void registerSlashCommands(@NotNull Guild guild) {

        slashCommands = new HashMap<>();
        slashPrivileges = new HashMap<>();

        CommandListUpdateAction updateAction = guild.updateCommands();

        Reflections cmds = new Reflections(Constants.COMMANDS_PACKAGE);
        Set<Class<? extends GuildSlashCommand>> classes = cmds.getSubTypesOf(GuildSlashCommand.class);

        for (var clazz : classes) {
            try {
                logger.info("{}[{}]{} Adding CommandData from Class {}",
                        Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET, clazz.getSimpleName());

                GuildSlashCommand instance;
                try { instance = clazz.getDeclaredConstructor(Guild.class).newInstance(guild);
                } catch (NoSuchMethodException nsm) { instance = clazz.getConstructor().newInstance(); }

                if (instance.getCommandPrivileges() != null) {
                    slashPrivileges.put(instance.getCommandData().getName(),
                            instance.getCommandPrivileges());
                }

                if (instance.getSubCommandClasses() != null) {
                    var cmdData = instance.getCommandData();
                    for (var subClazz : instance.getSubCommandClasses()) {
                        logger.info("\t{}[{}]{} Adding SubCommandData from Class {}",
                                Constants.TEXT_WHITE, clazz.getSimpleName(), Constants.TEXT_RESET, subClazz.getSimpleName());

                        Class<GuildSlashSubCommand> subClass = subClazz.asSubclass(GuildSlashSubCommand.class);
                        var subInstance = subClass.getDeclaredConstructor().newInstance();

                        if (subInstance.getSubCommandData() == null) logger.warn("\t{}[{}]{} Missing SubCommandData for Class {}",
                                Constants.TEXT_WHITE, clazz.getSimpleName(), Constants.TEXT_RESET, subClazz.getSimpleName());
                        else cmdData = cmdData.addSubcommands(subInstance.getSubCommandData());
                        slashCommands.put(instance.getCommandData().getName() + " " + subInstance.getSubCommandData().getName(), (SlashCommandHandler) subInstance);
                    }
                    updateAction.addCommands(cmdData);
                }
                else {
                    updateAction.addCommands(instance.getCommandData());
                    slashCommands.put(instance.getCommandData().getName() + " " + null, (SlashCommandHandler) instance);
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

        logger.info("{}[{}]{} Queuing SlashCommands",
                Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET);
        updateAction.queue();
    }

    void updatePrivileges (@NotNull Guild guild) {
        for (var cmd : guild.retrieveCommands().complete()) {

            if (slashPrivileges.containsKey(cmd.getName())) {
                logger.info("{}[{}]{} Updating Privileges for Command {}",
                        Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET, cmd.getName());

                cmd.updatePrivileges(guild, slashPrivileges.get(cmd.getName())).complete();
            }
        }
        logger.info("{}[{}]{} Successfully updated Privileges",
                Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        new Bot().listConfig();
        for (var guild : event.getJDA().getGuilds()) {
            registerSlashCommands(guild);
            updatePrivileges(guild);
        }
        new LinkChecker().startCheck(event.getJDA());
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {

        try {
            var command = slashCommands.get(event.getName() + " " + event.getSubcommandName());
            command.execute(event).queue();

        } catch (Exception e) {

            var embed = new EmbedBuilder()
                    .setColor(Constants.EMBED_GRAY)
                    .setAuthor(e.getClass().getSimpleName(), null, Bot.jda.getSelfUser().getEffectiveAvatarUrl())
                    .setDescription("```" + e.getMessage() + "```")
                    .setTimestamp(new Date().toInstant())
                    .build();

            event.replyEmbeds(embed).setEphemeral(true).queue();
        }
    }
}
