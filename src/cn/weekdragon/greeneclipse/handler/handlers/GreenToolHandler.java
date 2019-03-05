package cn.weekdragon.greeneclipse.handler.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import cn.weekdragon.greeneclipse.view.GreenDialog;

public class GreenToolHandler {
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell s,IThemeEngine themeEngine) {
		new GreenDialog(s,themeEngine).open();
	}


}
