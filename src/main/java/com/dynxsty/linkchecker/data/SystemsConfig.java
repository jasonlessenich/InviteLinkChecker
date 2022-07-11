package com.dynxsty.linkchecker.data;

import lombok.Data;

import java.util.List;

@Data
public final class SystemsConfig {
	private String botToken = "YOUR_BOT_TOKEN";
	private String inviteCode = "java";

	private TimerConfig timerConfig = new TimerConfig();

	private List<WebhookConfig> webhookConfigs = List.of(new WebhookConfig());

	@Data
	public static class TimerConfig {
		private int interval = 30;
		private String timeUnit = "SECONDS";
	}

	@Data
	public static class WebhookConfig {
		private boolean useWebhook = false;
		private String webhookUrl = "YOUR_WEBHOOK_URL";
	}
}
