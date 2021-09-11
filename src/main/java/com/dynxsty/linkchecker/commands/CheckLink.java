package com.dynxsty.linkchecker.commands;

import com.dynxsty.linkchecker.Constants;
import com.dynxsty.linkchecker.commands.dao.GuildSlashCommand;
import com.dynxsty.linkchecker.properties.ConfigString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

import java.util.Date;

public class CheckLink extends GuildSlashCommand implements SlashCommandHandler {

    @Override
    public ReplyAction execute(SlashCommandEvent event) {

        OptionMapping option = event.getOption("code");
        String code = option == null ? new ConfigString("code").getValue() : option.getAsString();

        try {

            Invite invite = Invite.resolve(event.getJDA(), code).complete();

            var embed = new EmbedBuilder()
                    .setColor(Constants.EMBED_GRAY)
                    .setThumbnail(invite.getGuild().getIconUrl())
                    .setTitle(invite.getGuild().getName())
                    .addField("Name", "```" + invite.getGuild().getName() + "```", true)
                    .addField("ID", "```" + invite.getGuild().getId() + "```", true)
                    .setTimestamp(new Date().toInstant())
                    .build();

            return event.replyEmbeds(embed);

        } catch (ErrorResponseException e) {

            if (!e.getMessage().equals("10006: Unknown Invite")) return null;

            var embed = new EmbedBuilder()
                    .setColor(Constants.EMBED_GRAY)
                    .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setAuthor(e.getClass().getSimpleName(), null, event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setDescription("```" + e.getMessage() + "\n⋅ discord.gg/" + code + " might be available!```")
                    .setTimestamp(new Date().toInstant())
                    .build();

            return event.replyEmbeds(embed);
        }
    }
}
