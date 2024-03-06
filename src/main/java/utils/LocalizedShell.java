package utils;

import org.eclipse.swt.widgets.Shell;

import lombok.Getter;

public class LocalizedShell extends Shell {
	@Getter public I18n i18n = new I18n();
}
