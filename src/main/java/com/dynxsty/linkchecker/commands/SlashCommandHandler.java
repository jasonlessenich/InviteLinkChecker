package com.dynxsty.linkchecker.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;

public interface SlashCommandHandler {

    ReplyAction execute(SlashCommandEvent event);
}
