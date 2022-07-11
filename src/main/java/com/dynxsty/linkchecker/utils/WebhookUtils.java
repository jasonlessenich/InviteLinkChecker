package com.dynxsty.linkchecker.utils;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.dynxsty.linkchecker.Bot;
import com.dynxsty.linkchecker.data.SystemsConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebhookUtils {
	private WebhookUtils() {
	}

	public static void sendToAllWebhooks(String message, Object... args) {
		for (SystemsConfig.WebhookConfig config : Bot.config.getWebhookConfigs()) {
			if (config.isUseWebhook() && checkWebhookUrl(config.getWebhookUrl())) {
				WebhookUtils.sendMessageToWebhook(config.getWebhookUrl(), message, args);
			}
		}
	}

	public static void sendMessageToWebhook(String url, String message, Object... args) {
		try (WebhookClient client = new WebhookClientBuilder(url).build()) {
			WebhookMessageBuilder webhookMessage = new WebhookMessageBuilder()
					.setContent(String.format(message, args));
			client.send(webhookMessage.build());
		}
	}

	public static boolean checkWebhookUrls() {
		for (SystemsConfig.WebhookConfig config : Bot.config.getWebhookConfigs()) {
			if (!config.isUseWebhook()) return false;
			if (checkWebhookUrl(config.getWebhookUrl())) {
				return true;
			} else {
				log.error("{} is not a valid Webhook Url! It will be ignored.", config.getWebhookUrl());
				return false;
			}
		}
		return true;
	}

	public static boolean checkWebhookUrl(String url) {
		return WebhookClientBuilder.WEBHOOK_PATTERN.matcher(url).matches();
	}
}
