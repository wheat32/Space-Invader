package utils;

import gameModes.GameMode;
import gameModes.InfinityMode;

public class GameManagementUtils implements ConstantValues
{
	private byte roundsBetweenBoss = 3;
	private byte startingLives = 3;
	private short alienSteps = 16;
	private float aPackSpdMod = 1.0f;
	private float wpnCoolMod = 1.0f;
	
	private byte gameType = 0;//0 = story, 1 = infinity mode
	private GameMode game;
	
	private byte roundType = 0;//0 = normal alien pack; 1 = boss alien
	
	private short endCode = 0;//0 = in-game; 1 = win; 2= lose; 3 = awaiting respawn
	private boolean inGame = false;

	public void startGame(byte gameType, byte[] b_mods, float[] f_mods)
	{
		this.gameType = gameType;
		//Set modifiers
		roundsBetweenBoss = b_mods[0];
		startingLives = b_mods[1];
		aPackSpdMod = f_mods[0];
		wpnCoolMod = f_mods[1];
		
		switch(this.gameType)
		{
			case 1://Infinity Mode
				game = new InfinityMode();
				game.startGame();
				break;
		}
	}
	
	public void stopGame()
	{
		game.stopGame();
	}
	
	public short getAlienSteps()
	{
		return alienSteps;
	}
	
	public byte getRoundsBetweenBoss()
	{
		return roundsBetweenBoss;
	}

	public byte getStartingLives()
	{
		return startingLives;
	}

	public float getAPackSpdMod()
	{
		return aPackSpdMod;
	}

	public float getWpnCoolMod()
	{
		return wpnCoolMod;
	}
	
	public void setInGame(boolean inGame)
	{
		this.inGame = inGame;
	}
	
	public boolean getInGame()
	{
		return inGame;
	}

	public byte getRoundType()
	{
		return roundType;
	}

	public void setRoundType(byte roundType)
	{
		this.roundType = roundType;
	}

	public short getEndCode()
	{
		return endCode;
	}

	public void setEndCode(short endCode)
	{
		this.endCode = endCode;
	}
}
