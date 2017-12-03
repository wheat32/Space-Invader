package utils;

import java.awt.event.KeyEvent;

public class KeyInputManagement
{
	public static boolean upKeyPressed = false;
	public static boolean downKeyPressed = false;
	public static boolean leftKeyPressed = false;
	public static boolean rightKeyPressed = false;
	public static boolean fireKeyPressed = false;
	public static boolean escKeyPressed = false;
	
	private static short[] upKeys = {KeyEvent.VK_UP, KeyEvent.VK_W};
	private static short[] downKeys = {KeyEvent.VK_DOWN, KeyEvent.VK_S};
	private static short[] leftKeys = {KeyEvent.VK_LEFT, KeyEvent.VK_A};
	private static short[] rightKeys = {KeyEvent.VK_RIGHT, KeyEvent.VK_D};
	private static short[] fireKeys = {KeyEvent.VK_SPACE, KeyEvent.VK_ENTER};
	private static short[] escKeys = {KeyEvent.VK_ESCAPE};
	
	private static short[][] allKeys = {upKeys, downKeys, leftKeys, rightKeys, fireKeys, escKeys};

	public static void keyPressed(KeyEvent e)
	{
		for(int i = 0; i < allKeys.length; i++)
		{
			for(int j = 0; j < allKeys[i].length; j++)
			{
				if(e.getKeyCode() == allKeys[i][j])
				{
					switch(i)
					{
						case 0:
							upKeyPressed = true;
							return;
						case 1:
							downKeyPressed = true;
							return;
						case 2:
							leftKeyPressed = true;
							return;
						case 3:
							rightKeyPressed = true;
							return;
						case 4:
							fireKeyPressed = true;
							return;
						case 5:
							escKeyPressed = true;
							return;
					}
				}
			}
		}
	}

	public static void keyReleased(KeyEvent e)
	{
		for(int i = 0; i < allKeys.length; i++)
		{
			for(int j = 0; j < allKeys[i].length; j++)
			{
				if(e.getKeyCode() == allKeys[i][j])
				{
					switch(i)
					{
						case 0:
							upKeyPressed = false;
							return;
						case 1:
							downKeyPressed = false;
							return;
						case 2:
							leftKeyPressed = false;
							return;
						case 3:
							rightKeyPressed = false;
							return;
						case 4:
							fireKeyPressed = false;
							return;
						case 5:
							escKeyPressed = false;
							return;
					}
				}
			}
		}
	}

	public static void keyTyped(KeyEvent e)
	{
		//NOTHING
	}
}
