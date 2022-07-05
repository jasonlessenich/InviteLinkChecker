package com.dynxsty.linkchecker.properties;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class ConfigElement<T> extends BotProperties {
	private static final Map<Class<?>, Function<String, Object>> propertyTypeParsers = new HashMap<>();

	static {
		propertyTypeParsers.put(Integer.class, Integer::parseInt);
		propertyTypeParsers.put(int.class, Integer::parseInt);
		propertyTypeParsers.put(Long.class, Long::parseLong);
		propertyTypeParsers.put(long.class, Long::parseLong);
		propertyTypeParsers.put(Float.class, Float::parseFloat);
		propertyTypeParsers.put(float.class, Float::parseFloat);
		propertyTypeParsers.put(Double.class, Double::parseDouble);
		propertyTypeParsers.put(double.class, Double::parseDouble);
		propertyTypeParsers.put(Boolean.class, Boolean::parseBoolean);
		propertyTypeParsers.put(boolean.class, Boolean::parseBoolean);
		propertyTypeParsers.put(String.class, s -> s);
		propertyTypeParsers.put(TimeUnit.class, TimeUnit::valueOf);
	}

	private T value;

	public ConfigElement(String entryname, Class<T> targetClass) {
		super(entryname);
		if (this.isRegisteredInConfig()) {
			try {
				if (propertyTypeParsers.containsKey(targetClass)) {
					this.value = (T) propertyTypeParsers.get(targetClass).apply(this.load());
				} else {
					throw new IllegalArgumentException("Unknown Property Type: " + targetClass.getSimpleName());
				}
			} catch (Exception e) {
				log.error("Failed whilst loading: " + entryname);
				e.printStackTrace();
			}
		} else {
			try {
				this.save(String.valueOf(0));
			} catch (Exception e) {
				log.error("Failed whilst saving: " + entryname);
				e.printStackTrace();
			}
		}
	}

	public T getValue() {
		return this.value;
	}

	public void setValue(T value) {
		this.value = value;
		try {
			this.save(String.valueOf(value));
		} catch (Exception e) {
			log.error("Failed whilst saving: " + entryname);
			e.printStackTrace();
		}
	}

}
