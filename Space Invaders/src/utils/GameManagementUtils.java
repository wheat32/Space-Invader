package utils;

import gameModes.GameMode;
import gameModes.InfinityMode;
import system.Audio;
import system.Audio.Tracks;

public class GameManagementUtils implements ConstantValues
{
	public static enum GameStatus
	{
		IN_GAME, PRE_ROUND, POST_ROUND, WIN, LOSE, BIG_WIN, AWAITING_RESPAWN;
	}
	
	private byte roundsBetweenBoss = 3;
	private byte startingLives = 3;
	private short alienSteps = 16;
	private float aPackSpdMod = 1.0f;
	private float wpnCoolMod = 1.0f;
	
	private byte gameType = 0;//0 = story, 1 = infinity mode
	private GameMode game;

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
				Audio.openClips(new Tracks[] {Tracks.BGM1, Tracks.BGM3, Tracks.BossBGM1, Tracks.Victory1, Tracks.Victory2});
				game = new InfinityMode();
				game.startGame();
				break;
			default:
				throw new RuntimeException("GameManagementUtils | Unknown gamemode type: " + this.gameType + ".");
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
	
	public GameMode getGameMode()
	{
		return game;
	}
	
	public boolean getInGame()
	{
		return game.getInGame();
	}

	public GameStatus getGameStatus()
	{
		return (game == null) ? GameStatus.IN_GAME : game.getStatus();
	}
	
	public Wall getWall()
	{
		return game.getWall();
	}
}
