package com.dynxsty.linkchecker.commands.dao;

import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

public abstract class GuildSlashSubCommandGroup {

    protected SubcommandGroupData subcommandGroupData = null;
    protected Class[] subCommandClasses = null;

    public SubcommandGroupData getSubCommandGroupData () { return this.subcommandGroupData; }
    public Class[] getSubCommandClasses() { return this.subCommandClasses; }
}
