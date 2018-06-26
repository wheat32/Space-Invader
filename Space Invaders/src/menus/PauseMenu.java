package menus;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import utils.EntityManagement;
import utils.ObjectCollection;

public class PauseMenu extends JFrame
{
	private static final long serialVersionUID = -8507190066912804299L;
	
	public void displayPauseMenu()
	{
		ObjectCollection.getMainLoop().stop();
		
		String[] options = {"Resume",
		                    "Options",
		                    "Exit to Menu",
		                    "Exit to Desktop"};
	
		int selection = JOptionPane.showOptionDialog(this,
		    null,
		    "Paused",
		    JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.PLAIN_MESSAGE,
		    null,
		    options,
		    options[0]);
		
		switch(selection)
		{
			case 1:
				System.out.println("to options");
				break;
			case 2:
				ObjectCollection.getMainMenu().setInMainMenu(true);
				ObjectCollection.getGameManagement().stopGame();
				EntityManagement.removeAllEntities(true);
				break;
			case 3:
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.OK_CANCEL_OPTION))
				{
					System.exit(0);
				}
				break;
		}
		
		ObjectCollection.getMainLoop().start();
	}
}
