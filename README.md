# InviteLinkChecker

A Discord Bot, that checks if an (custom) invite link is taken - written in Java using [JDA](https://github.com/DV8FromTheWorld/JDA/)

# Usage

Simply run ``java -jar LinkChecker-all.jar`` in your CLI or, if on Windows, run the `start.bat` file. 
[→ Java 1.8 required](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)

# Configuration
 
Sample ``config.yaml`` file:
```yaml
# Your bot's token. This can be found at https://discord.com/developers/applications/
botToken: 'YOUR_BOT_TOKEN'
# The invite code you want to check for. 'python' will check for discord.gg/python
inviteCode: 'YOUR_INVITE_CODE'
timerConfig:
    # The amount of 'timeUnit' for which to check. An interval of 30 combined with a TimeUnit of 'SECONDS' will
    # check every 30 seconds.
    interval: 30
    # An enum of java.util.concurrent.TimeUnit. e.g. SECONDS, MINUTES, HOURS, ...
    timeUnit: SECONDS
webhookConfigs:
  # A list of webhooks.
  - {
    # Whether the bot should send a message to a webhook once the invite code is not in use.
    useWebhook: true,
    # Your webhook url.
    # This can be found under YOUR_CHANNEL > Settings > Integrations > Webhooks > (Create Webhook) > Copy Webhook URL
    webhookUrl: 'YOUR_WEBHOOK_URL',
    # The message which will be sent when the link is available. Please note that '%s' is a format specifier which
    # holds the 'inviteCode'.
    linkAvailableMessage: 'discord.gg/%s is available!'
  }
```

