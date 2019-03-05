package cn.weekdragon.greeneclipse;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;

import cn.weekdragon.greeneclipse.handler.util.CSSUtil;

@SuppressWarnings("restriction")
public class GreenToolProcessor implements IStartup{

	@Inject
	private static IThemeManager themeManager;
	
	@Execute
	public void execute(IThemeManager themeManager) {
		GreenToolProcessor.themeManager = themeManager;
	}


	@Override
	public void earlyStartup() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				CSSUtil.resolveTheme(themeManager.getEngineForDisplay(Display.getDefault()));
			}
		});
	}
}
