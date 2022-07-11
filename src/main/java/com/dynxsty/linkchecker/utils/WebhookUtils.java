package com.dynxsty.linkchecker.utils;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.data.SystemsConfig;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class WebhookUtils {
	private WebhookUtils() {}

	public static void sendToAllWebhooks(String message, Object... args) {
		for (SystemsConfig.WebhookConfig config : Bot.config.getWebhookConfigs()) {
			if (config.isUseWebhook()) {
				WebhookUtils.sendMessageToWebhook(config.getWebhookUrl(), message, args);
			}
		}
	}

	public static @NotNull CompletableFuture<Void> sendMessageToWebhook(String url, String message, Object... args) {
		try (WebhookClient client = new WebhookClientBuilder(url).build()) {
			WebhookMessageBuilder webhookMessage = new WebhookMessageBuilder()
					.setContent(String.format(message, args));
			return CompletableFuture.allOf(client.send(webhookMessage.build()));
		}
	}
}
