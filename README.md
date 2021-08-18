# InviteLinkChecker

A (dumb) discord bot, which checks if an (custom) invite link is taken or not - written in java using [JDA](https://github.com/DV8FromTheWorld/JDA/)\
_(totally not made to let us know when the discord.gg/java vanity url is finally available)_

# Usage

Simply run ``java -jar LinkChecker.jar`` in the command line  
[→ Java SE Development Kit 16 required](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html)

# Configuration
To start up, the bot only needs a ``config.properties`` file in the source directory

sample ``config.properties`` file:
```
#
#Thu Jul 08 23:43:24 CEST 2021
interval=5
timeunit=MINUTES
code=python
totalCheckCount=0
owner_id=810481402390118400
link_available_msg=<@810481402390118400>
guild_id=648956210850299986
text_id=856219624516223018
token=ODYyNzg1OqAxDs0Mjg5NzE0.l1nKCA.6WajdFZi5Y5gzqp33RdZRLbWA78
```

