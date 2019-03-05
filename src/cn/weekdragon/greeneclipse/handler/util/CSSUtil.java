package cn.weekdragon.greeneclipse.handler.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.ui.css.core.dom.ExtendedDocumentCSS;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.swt.graphics.RGB;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.css.sac.CSSParseException;
import org.w3c.dom.stylesheets.StyleSheet;
import org.w3c.dom.stylesheets.StyleSheetList;

@SuppressWarnings({ "restriction" })
public class CSSUtil {
	private static Logger logger = LoggerFactory.getLogger(CSSUtil.class); 
	private static IEclipsePreferences preference = ConfigurationScope.INSTANCE.getNode("GreenEclipse.CSS");
	
	public static void resolveTheme(IThemeEngine themeEngine) {
		String css = preference.get(IConstants.BG_COLOR, "");
		if(!css.isEmpty()) {
			applyCSS(themeEngine, css);
			logger.info("resovle theme");
		}
	}
	
	public static String applyCSS(IThemeEngine themeEngine, String css) {
		if (themeEngine == null) {
			return null;
		}
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		((ThemeEngine) themeEngine).resetCurrentTheme();

		int count = 0;
		for (CSSEngine engine : ((ThemeEngine) themeEngine).getCSSEngines()) {
			if (count++ > 0) {
				sb.append("\n\n");
			}
			ExtendedDocumentCSS doc = (ExtendedDocumentCSS) engine.getDocumentCSS();
			List<StyleSheet> sheets = new ArrayList<StyleSheet>();
			StyleSheetList list = doc.getStyleSheets();
			for (int i = 0; i < list.getLength(); i++) {
				sheets.add(list.item(i));
			}

			try {
				Reader reader = new StringReader(css);
				sheets.add(0, engine.parseStyleSheet(reader));
				doc.removeAllStyleSheets();
				for (StyleSheet sheet : sheets) {
					doc.addStyleSheet(sheet);
				}
				engine.reapply();
				long nanoDiff = System.nanoTime() - start;
				sb.append("\nTime: ").append(nanoDiff / 1000000).append("ms");
				preference.put(IConstants.BG_COLOR, css);
				preference.flush();
			} catch (CSSParseException e) {
				logger.error("stack: ",e);
				sb.append("\nError: line ").append(e.getLineNumber()).append(" col ").append(e.getColumnNumber())
						.append(": ").append(e.getLocalizedMessage());
			} catch (IOException e) {
				logger.error("stack: ",e);
				sb.append("\nError: ").append(e.getLocalizedMessage());
			} catch (BackingStoreException e) {
				logger.error("stack: ",e);
			}
		}
		return sb.toString();
	}

	public static String resetCSS(IThemeEngine themeEngine) {
		if (themeEngine == null) {
			return null;
		}
		long start = System.nanoTime();

		StringBuilder sb = new StringBuilder();
		((ThemeEngine) themeEngine).resetCurrentTheme();

		long nanoDiff = System.nanoTime() - start;
		sb.append("\nTime: ").append(nanoDiff / 1000000).append("ms");
		preference.put(IConstants.BG_COLOR, "");
		try {
			preference.flush();
		} catch (BackingStoreException e) {
			logger.error("stack: ",e);
		}
		return sb.toString();
	}
	
	public static String convertRGB2HEX(RGB rgb) {
		String r = Integer.toHexString(rgb.red);
		if(r.length() == 1) {
			r = 0+r;
		}
		String g = Integer.toHexString(rgb.green);
		if(g.length() == 1) {
			g = 0+g;
		}
		String b = Integer.toHexString(rgb.blue);
		if(b.length() == 1) {
			b = 0+b;
		}
		return "#" + r+g+b;
	}
}
