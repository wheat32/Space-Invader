package utils;

import core.MainLoop;
import menus.MainMenu;

public final class ObjectCollection
{
	private static GameManagementUtils gameManagement;
	private static MainMenu mainMenu;
	private static MainLoop mainLoop;
	
	public static GameManagementUtils getGameManagement()
	{
		if(gameManagement == null)
		{
			gameManagement = new GameManagementUtils();
		}
		return gameManagement;
	}
	
	public static MainMenu getMainMenu()
	{
		if(mainMenu == null)
		{
			mainMenu = new MainMenu();
		}
		return mainMenu;
	}
	
	public static MainLoop getMainLoop()
	{
		if(mainLoop == null)
		{
			mainLoop = new MainLoop();
		}
		return mainLoop;
	}
}
