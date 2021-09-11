package com.dynxsty.linkchecker.commands.dao;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public abstract class GuildSlashSubCommand {

    protected SubcommandData subcommandData = null;

    public SubcommandData getSubCommandData () { return this.subcommandData; }
}
