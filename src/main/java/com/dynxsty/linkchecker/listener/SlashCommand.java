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

        this.slashCommands = new HashMap<>();
        this.slashPrivileges = new HashMap<>();

        CommandListUpdateAction updateAction = guild.updateCommands();

        Reflections cmds = new Reflections(Constants.COMMANDS_PACKAGE);
        Set<Class<? extends GuildSlashCommand>> classes = cmds.getSubTypesOf(GuildSlashCommand.class);

        for (var clazz : classes) {
            CommandData cmdData = null;

            try {
                GuildSlashCommand instance;
                try { instance = clazz.getDeclaredConstructor(Guild.class).newInstance(guild);
                } catch (NoSuchMethodException nsm) { instance = clazz.getConstructor().newInstance(); }

                if (instance.getCommandData() == null) {
                    logger.warn("Class {} is missing CommandData. It will be ignored.", clazz.getName());
                    continue;
                }
                if (instance.getCommandPrivileges() != null) {
                    slashPrivileges.put(instance.getCommandData().getName(),
                            instance.getCommandPrivileges());
                }
                cmdData = instance.getCommandData();
                logger.info("{}[{}]{} Added CommandData from Class {}",
                        Constants.TEXT_WHITE, guild.getName(),
                        Constants.TEXT_RESET, clazz.getSimpleName());

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
                            logger.warn("Class {} is missing SubCommandGroupData. It will be ignored.", subGroupClazz.getName());
                            continue;
                        }
                        logger.info("\t{}[{}]{} Adding SubCommandGroupData from Class {}",
                                Constants.TEXT_WHITE, clazz.getSimpleName(),
                                Constants.TEXT_RESET, subGroupClazz.getSimpleName());

                        if (subGroupInstance.getSubCommandClasses() == null) {
                            logger.warn("Class {} is missing SubCommandClasses. It will be ignored.", subGroupClazz.getName());
                            continue;
                        }
                        for (var subClazz : subGroupInstance.getSubCommandClasses()) {
                            GuildSlashSubCommand subInstance = (GuildSlashSubCommand) subClazz.getDeclaredConstructor().newInstance();
                            if (subInstance.getSubCommandData() == null) {
                                logger.warn("Class {} is missing SubCommandData. It will be ignored.", subClazz.getName());
                                continue;
                            }
                            cmdData.addSubcommandGroups(subGroupInstance.getSubCommandGroupData()
                                    .addSubcommands(subInstance.getSubCommandData()));

                            slashCommands.put(instance.getCommandData().getName() + " " +
                                    subGroupInstance.getSubCommandGroupData().getName() + " " +
                                    subInstance.getSubCommandData().getName(), (SlashCommandHandler) subInstance);

                            logger.info("\t\t{}[{}]{} Added SubCommandData from Class {}",
                                    Constants.TEXT_WHITE, subGroupClazz.getSimpleName(), Constants.TEXT_RESET,
                                    subClazz.getSimpleName());
                        }
                    }
                }

                if (instance.getSubCommandClasses() != null) {
                    for (var subClazz : instance.getSubCommandClasses()) {
                        GuildSlashSubCommand subInstance = (GuildSlashSubCommand) subClazz.getDeclaredConstructor().newInstance();
                        if (subInstance.getSubCommandData() == null) {
                            logger.warn("Class {} is missing SubCommandData. It will be ignored.", subClazz.getName());
                        } else {
                            cmdData.addSubcommands(subInstance.getSubCommandData());

                            slashCommands.put(instance.getCommandData().getName() + " " +
                                    null + " " + subInstance.getSubCommandData().getName(),
                                    (SlashCommandHandler) subInstance);

                            logger.info("\t{}[{}]{} Added SubCommandData from Class {}",
                                    Constants.TEXT_WHITE, clazz.getSimpleName(),
                                    Constants.TEXT_RESET, subClazz.getSimpleName());
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
        for (var cmd : guild.retrieveCommands().complete()) {
            if (slashPrivileges.containsKey(cmd.getName())) {
                cmd.updatePrivileges(guild, slashPrivileges.get(cmd.getName())).complete();
                logger.info("{}[{}]{} Updated Privileges for Command {}",
                        Constants.TEXT_WHITE, guild.getName(), Constants.TEXT_RESET, cmd.getName());
            }
        }
        if (!slashPrivileges.isEmpty()) logger.info("{}[{}]{} Successfully updated Privileges",
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
