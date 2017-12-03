package utils;

import javax.swing.DebugGraphics;

import core.Renderer;
import io.Background;
import io.DebugPrints;
import io.FontManagement;
import io.MainMenu;
import system.Audio;

public final class ObjectCollection
{
	private static Audio audio;
	private static Background background;
	private static DebugPrints debugPrints;
	private static EntityManagement entityManagement;
	private static FontManagement fontManagement;
	private static GameManagementUtils gameManagement;
	private static MainMenu mainMenu;
	private static Renderer renderer;
	
	public static Audio getAudio()
	{
		if(audio == null)
		{
			audio = new Audio();
		}
		return audio;
	}
	
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
	
	public static FontManagement getFontManagement()
	{
		if(fontManagement == null)
		{
			fontManagement = new FontManagement();
		}
		return fontManagement;
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
