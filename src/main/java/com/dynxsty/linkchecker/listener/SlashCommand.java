package com.dynxsty.linkchecker.listener;

import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.check.LinkChecker;
import com.dynxsty.linkchecker.commands.SlashCommandHandler;
import com.dynxsty.linkchecker.commands.dao.GuildSlashCommand;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommand;
import com.dynxsty.linkchecker.commands.dao.GuildSlashSubCommandGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class SlashCommand extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SlashCommand.class);
    private HashMap<String, SlashCommandHandler> slashCommands;
    private HashMap<String, CommandPrivilege[]> slashPrivileges;

    void registerSlashCommands(@NotNull Guild guild) {

        this.slashCommands = new HashMap<>();
        this.slashPrivileges = new HashMap<>();

        CommandListUpdateAction updateAction = guild.updateCommands();

        Reflections cmds = new Reflections(Constants.COMMANDS_PACKAGE);
        Set<Class<? extends GuildSlashCommand>> classes = cmds.getSubTypesOf(GuildSlashCommand.class);

        for (var clazz : classes) {
            CommandData cmdData = null;

            try {
                logger.info("{}[{}]{} Adding CommandData from Class {}",
                        Constants.TEXT_WHITE, guild.getName(),
                        Constants.TEXT_RESET, clazz.getSimpleName());
                GuildSlashCommand instance;
                try { instance = clazz.getDeclaredConstructor(Guild.class).newInstance(guild);
                } catch (NoSuchMethodException nsm) { instance = clazz.getConstructor().newInstance(); }

                if (instance.getCommandPrivileges() != null) {
                    slashPrivileges.put(instance.getCommandData().getName(),
                            instance.getCommandPrivileges());
                }
                if (instance.getCommandData() == null) {
                    logger.warn("Class {} is missing CommandData. It will be ignored.", clazz.getName());
                    continue;
                }
                cmdData = instance.getCommandData();
                if (instance.getSubCommandClasses() == null
                    && instance.getSubCommandGroupClasses() == null) {
                    slashCommands.put(instance.getCommandData().getName() + " " +
                            null + " " +
                            null, (SlashCommandHandler) instance);
                }
                if (instance.getSubCommandGroupClasses() != null) {
                    for (var subGroupClazz : instance.getSubCommandGroupClasses()) {
                        GuildSlashSubCommandGroup subGroupInstance = (GuildSlashSubCommandGroup) subGroupClazz.getDeclaredConstructor().newInstance();
                        if (subGroupInstance.getSubCommandGroupData() == null) {
                            logger.warn("Class {} is missing SubCommandGroupData. It will be ignored.", clazz.getName());
                        } else {
                            logger.info("\t{}[{}]{} Adding SubCommandGroupData from Class {}",
                                    Constants.TEXT_WHITE, clazz.getSimpleName(),
                                    Constants.TEXT_RESET, subGroupClazz.getSimpleName());

                            if (subGroupInstance.getSubCommandClasses() != null) {
                                for (var subClazz : subGroupInstance.getSubCommandClasses()) {
                                    GuildSlashSubCommand subInstance = (GuildSlashSubCommand) subClazz.getDeclaredConstructor().newInstance();
                                    if (subInstance.getSubCommandData() == null) {
                                        logger.warn("Class {} is missing SubCommandData. It will be ignored.", clazz.getName());
                                    } else {
                                        logger.info("\t\t{}[{}]{} Adding SubCommandData from Class {}",
                                                Constants.TEXT_WHITE, clazz.getSimpleName(), Constants.TEXT_RESET,
                                                subClazz.getSimpleName());
                                        cmdData.addSubcommandGroups(subGroupInstance.getSubCommandGroupData()
                                                .addSubcommands(subInstance.getSubCommandData()));

                                        slashCommands.put(instance.getCommandData().getName() + " " +
                                                subGroupInstance.getSubCommandGroupData().getName() + " " +
                                                subInstance.getSubCommandData().getName(), (SlashCommandHandler) subInstance);
                                    }
                                }
                            }
                        }
                    }
                }
                if (instance.getSubCommandClasses() != null) {
                    for (var subClazz : instance.getSubCommandClasses()) {
                        GuildSlashSubCommand subInstance = (GuildSlashSubCommand) subClazz.getDeclaredConstructor().newInstance();
                        if (subInstance.getSubCommandData() == null) {
                            logger.warn("Class {} is missing SubCommandData. It will be ignored.", clazz.getName());
                        } else {
                            logger.info("\t{}[{}]{} Adding SubCommandData from Class {}",
                                    Constants.TEXT_WHITE, clazz.getSimpleName(),
                                    Constants.TEXT_RESET, subClazz.getSimpleName());
                            cmdData.addSubcommands(subInstance.getSubCommandData());

                            slashCommands.put(instance.getCommandData().getName() + " " +
                                    null + " " + subInstance.getSubCommandData().getName(),
                                    (SlashCommandHandler) subInstance);
                        }
                    }
                }
            } catch (Exception e) { e.printStackTrace(); }
            updateAction.addCommands(cmdData);
        }
        logger.info("{}[{}]{} Queuing SlashCommands",
                Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET);
        updateAction.queue();
    }

    void updatePrivileges (@NotNull Guild guild) {
        int i = 0;
        for (var cmd : guild.retrieveCommands().complete()) {

            if (slashPrivileges.containsKey(cmd.getName())) {
                i++;
                logger.info("{}[{}] {} Updating Privileges for Command {}",
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
        logger.info("{}[*]{} Command update completed\n",
                Constants.TEXT_WHITE, Constants.TEXT_RESET);

        new LinkChecker().startCheck(event.getJDA());
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        try {
            var command = slashCommands.get(event.getName() + " " + event.getSubcommandGroup() + " " + event.getSubcommandName());
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
