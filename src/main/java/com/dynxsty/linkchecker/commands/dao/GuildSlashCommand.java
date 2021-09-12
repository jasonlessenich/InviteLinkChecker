package com.dynxsty.linkchecker.commands.dao;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;

public abstract class GuildSlashCommand {

    protected CommandData commandData = null;
    protected Class[] subCommandClasses = null;
    protected Class[] subCommandGroupClasses = null;
    protected CommandPrivilege[] commandPrivileges = null;

    public CommandData getCommandData () { return this.commandData; }
    public Class[] getSubCommandClasses() { return this.subCommandClasses; }
    public Class[] getSubCommandGroupClasses() { return this.subCommandGroupClasses; }
    public CommandPrivilege[] getCommandPrivileges() { return this.commandPrivileges; }
}
