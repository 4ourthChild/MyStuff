package edu.byu.isys413.fbrooke;

import org.eclipse.swt.widgets.Display;

public class WindowController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			SaleWin window = new SaleWin();
			window.open();
			//window.isVisible(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			Display display = Display.getDefault();
//			LoginShell shell = new LoginShell(display);
//			shell.open();
//			shell.layout();
//			
//			while (!shell.isDisposed()) {
//				if (!display.readAndDispatch()) {
//					display.sleep();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		

		
	}

}
