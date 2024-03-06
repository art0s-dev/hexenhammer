package utils;

import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Setter;

public final class I18n {
	private ResourceBundle translations;
	
	public I18n() {
		setLanguage(english());
	}
	
	public void setLanguage(Locale language) {
		translations = ResourceBundle.getBundle("translations", language);
	}
	
	public String get(String translationOf) {
		return translations.getString(translationOf);
	}
	
	public static Locale english() {
		return new Locale.Builder()
				.setLanguage("en")
				.setRegion("US")
				.build();
	}
	
	public static Locale german() {
		return new Locale.Builder()
				.setLanguage("de")
				.setRegion("DE")
				.build();
	}
}
