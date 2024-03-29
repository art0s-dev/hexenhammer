package unittests.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import utils.I18n;

class I18NTest {
	
	@Test
	void testGettingALanguageConstantThatIsNotThere() {
		I18n i18n = new I18n();
		assertThrows(Exception.class, () -> i18n.get("message.welcome"));
	}

	@Test
	void testGettingALanguageConstant() {
		I18n i18n = new I18n();
		assertEquals("Hexenhammer", i18n.get("general.applicationName"));
	}
	
	@Test
	void testSwitchingTheLanguage() {
		I18n i18n = new I18n();
		assertEquals("Template", i18n.get("arch.BaseView.tabName"));
		i18n.setLanguage(I18n.german());
		assertEquals("Vorlage", i18n.get("arch.BaseView.tabName"));
	}

}
