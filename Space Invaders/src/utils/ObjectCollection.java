package utils;

import core.Renderer;
import io.DebugPrints;
import menus.MainMenu;
import spaceBackgrounds.Background;

public final class ObjectCollection
{
	private static Background background;
	private static DebugPrints debugPrints;
	private static EntityManagement entityManagement;
	private static GameManagementUtils gameManagement;
	private static MainMenu mainMenu;
	private static Renderer renderer;
	
	public static Background getBackground()
	{
		if(background == null)
		{
			background = new Background();
		}
		return background;
	}
	
	public static DebugPrints getDebugPrints()
	{
		if(debugPrints == null)
		{
			debugPrints = new DebugPrints();
		}
		return debugPrints;
	}
	
	public static EntityManagement getEntityManagement()
	{
		if(entityManagement == null)
		{
			entityManagement = new EntityManagement();
		}	
		return entityManagement;
	}
	
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
	
	public static Renderer getRenderer()
	{
		if(renderer == null)
		{
			renderer = new Renderer();
		}
		return renderer;
	}
}
