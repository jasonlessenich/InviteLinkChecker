package com.dynxsty.linkchecker.data;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The base container class for all the bot's configuration.
 */
@Slf4j
public final class BotConfig {
	private static final String CONFIG_FILE = "config.yaml";

	private final SystemsConfig config;

	/**
	 * The path from which the config was loaded.
	 */
	private final Path dir;

	/**
	 * Constructs a new empty configuration.
	 *
	 * @param dir The path to the directory containing the guild configuration
	 *            files.
	 */
	public BotConfig(Path dir) {
		this.dir = dir;
		if (!(Files.exists(dir) && Files.isDirectory(dir))) {
			if (!Files.exists(dir)) {
				try {
					Files.createDirectories(dir);
				} catch (IOException e) {
					log.error("Could not create config directory " + dir, e);
				}
			} else {
				log.error("File exists at config directory path {}", dir);
			}
		}
		Yaml yaml = new Yaml();
		Path systemsFile = dir.resolve(CONFIG_FILE);
		if (Files.exists(systemsFile)) {
			try (BufferedReader reader = Files.newBufferedReader(systemsFile)) {
				this.config = yaml.loadAs(reader, SystemsConfig.class);
				log.info("Loaded systems config from {}", systemsFile);
			} catch (YAMLException e) {
				log.error("Invalid YAML found! Please fix or remove config file " + systemsFile + " and restart.", e);
				throw e;
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		} else {
			log.info("No systems config file found. Creating an empty one at {}", systemsFile);
			this.config = new SystemsConfig();
			this.flush();
		}
	}

	public SystemsConfig getConfig() {
		return this.config;
	}

	/**
	 * Flushes all configuration to the disk.
	 */
	public void flush() {
		Yaml yaml = new Yaml();
		Path systemsFile = this.dir.resolve(CONFIG_FILE);
		try (BufferedWriter writer = Files.newBufferedWriter(systemsFile)) {
			yaml.dump(this.config, writer);
			writer.flush();
		} catch (IOException e) {
			log.error("Could not save systems config.", e);
		}
	}
}
