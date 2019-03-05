package cn.weekdragon.greeneclipse.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weekdragon.greeneclipse.handler.util.CSSUtil;
import cn.weekdragon.greeneclipse.handler.util.IConstants;

@SuppressWarnings("restriction")
public class GreenDialog extends Dialog {

	private static Logger logger = LoggerFactory.getLogger(GreenDialog.class);
	private IThemeEngine engine;

	public GreenDialog(Shell parentShell, IThemeEngine themeEngine) {
		super(parentShell);
		this.engine = themeEngine;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Group gpTheme = new Group(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(gpTheme);
		gpTheme.setText("Theme");
		gpTheme.setLayout(new GridLayout(3, false));

		Button btnGreen = new Button(gpTheme, SWT.PUSH);
		btnGreen.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		btnGreen.setText("Green");
		btnGreen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnGreen.forceFocus();
					String defalutCSS = getDefaultCSS();
					String res = CSSUtil.applyCSS(engine, defalutCSS.toString().replaceAll("bg-color", IConstants.BG_GREEN));
					info("Greening Success\n" + res);
				} catch (Exception e2) {
					MessageDialog.openError(getShell(), "错误", e2.toString());
				}
			}
		});

		Button btnColor = new Button(gpTheme, SWT.PUSH);
		btnColor.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		btnColor.setText("Customize");
		btnColor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnColor.forceFocus();
					ColorDialog cd = new ColorDialog(getShell());
					RGB rgb = cd.open();
					if (rgb == null) {
						return;
					}
					String defalutCSS = getDefaultCSS();
					String res = CSSUtil.applyCSS(engine,
							defalutCSS.toString().replaceAll("bg-color", CSSUtil.convertRGB2HEX(rgb)));
					info("Customize Success\n" + res);
				} catch (Exception e2) {
					logger.error("stack trace: ", e2);
					error(e2.toString());
				}
			}
		});

		Button btnReset = new Button(gpTheme, SWT.PUSH);
		btnReset.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		btnReset.setText("Reset");
		btnReset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					btnReset.forceFocus();
					String res = CSSUtil.resetCSS(engine);
					info("Reset Success\n" + res);
				} catch (Exception e2) {
					logger.error("stack trace: ", e2);
					error(e2.toString());
				}
			}
		});
		return parent;
	}

	private void info(String msg) {
		MessageDialog.openInformation(getShell(), "Info", msg);
	}

	private void error(String msg) {
		MessageDialog.openError(getShell(), "Error", msg);
	}

	private String getDefaultCSS() {
		InputStream resourceAsStream = getClass().getResourceAsStream("greenCSS.css");
		if (resourceAsStream == null) {
			error("can not find the default css");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			logger.error("stack trace: ", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (resourceAsStream != null) {
					resourceAsStream.close();
				}
			} catch (IOException e) {
				logger.error("stack trace: ", e);
			}
		}
		return sb.toString();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, Dialog.OK, "Close", true);
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		new GreenDialog(shell, null).open();
		shell.dispose();
		display.dispose();
	}
}
