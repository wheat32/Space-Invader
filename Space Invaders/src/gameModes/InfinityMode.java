package gameModes;

import bosses.Boss;
import bosses.BossAlien;
import core.Entity;
import enemies.AlienPack;
import io.InGamePrints;
import miscEntities.Wall;
import players.SpaceShip;
import shields.ShipShield;
import utils.ConstantValues;
import utils.ObjectCollection;
import utils.Stats;

public class InfinityMode extends GameMode implements ConstantValues
{
	private short alienSteps = 16;
	private Boss boss;
	private Entity aliens;
	
	public InfinityMode()
	{
		super();
		alienSteps = ObjectCollection.getGameManagement().getAlienSteps();
	}

	@Override
	public void resetStats()
	{
		Stats.totalTimesShot = 0;
		Stats.level = 0;
		Stats.killedAliens = 0;
		Stats.score = 0;
		Stats.totalHits = 0;
		Stats.totalTime = 0;
		Stats.lives = ObjectCollection.getGameManagement().getStartingLives();
		resetPerRoundStats();
	}
	
	public void resetPerRoundStats()
	{
		Stats.timesShot = 0;
		Stats.hits = 0;
		Stats.roundTime = 0;
	}
	
	@Override
	protected void initEntitySetUp()
	{
		wall = new Wall(1.0f, 1.0f);
		ObjectCollection.getEntityManagement().addEntity(wall);
		ship = new SpaceShip(0.05f, 0.05f);
		ship.setDefaultPosition(Stats.SCREEN_WIDTH/2 - ship.getDimension().width,
				(Stats.SCREEN_HEIGHT/alienSteps)*(alienSteps-2));
		ship.reset();
		ObjectCollection.getEntityManagement().addEntity(ship);
		gamePrints = new InGamePrints(ship);
	}
	
	@Override
	protected void startNextRound()
	{
		Stats.level++;
		ObjectCollection.getGameManagement().setRoundType(getNextRoundType());
		spawnEntities();
		ObjectCollection.getGameManagement().setInGame(true);
		changeMusic();
	}

	private byte getNextRoundType()
	{
		//Is this next round a boss?
		return (byte) ((Stats.level % ObjectCollection.getGameManagement().getRoundsBetweenBoss() == 0) ? 1 : 0);
	}
	
	private void changeMusic()
	{
		switch(ObjectCollection.getGameManagement().getRoundType())
		{
			default:
			case 0://Normal wave
				if(ObjectCollection.getAudio().getCurrBGM() != TrackNames.BGM1_INTRO)
				{
					ObjectCollection.getAudio().changeTrack(TrackNames.BGM1_INTRO, TrackNames.BGM1_LOOP);
				}
				break;
			case 1://Boss
				if(ObjectCollection.getAudio().getCurrBGM() != TrackNames.BOSSBGM1)
				{
					ObjectCollection.getAudio().changeTrack(TrackNames.BOSSBGM1, null);
				}
				break;
		}
	}
	
	@Override
	protected void spawnEntities()
	{
		switch(gameManagement.getRoundType())
		{
			case 0://Normal wave
				aliens = new AlienPack((short) ((Stats.level < 3) ? 9 + Stats.level : 12));
				ObjectCollection.getEntityManagement().addEntity(aliens);
				break;
			case 1://Boss
				boss = new BossAlien(ship, 0.4f, 0.4f);
				if(boss instanceof BossAlien)
				{
					((BossAlien) boss).setShield(new ShipShield(boss));
				}
				ObjectCollection.getEntityManagement().addEntity(boss);
				break;
			default:
				throw new RuntimeException(gameManagement.getRoundType() + " is an invalid round type for Infinity Mode.");
		}
	}

	@Override
	public void startGame()
	{
		System.out.println("InfinityMode: startGame called.");
		ObjectCollection.getEntityManagement().removeAllEntities(true, true);
		boss = null;
		aliens = null;
		resetStats();
		initEntitySetUp();
		startNextRound();
	}
	
	private void checkGameStatus()
	{
		switch(gameManagement.getRoundType())
		{
			case 0://Normal wave
				if(aliens instanceof AlienPack == true)
				{
					if(((AlienPack) aliens).getAliensInPack() == 0)
					{
						gameManagement.setEndCode((short) 1);
						endRound();
					}
				}
				break;
			case 1://Boss
				break;
			default:
				throw new RuntimeException(gameManagement.getRoundType() + " is an invalid round type for Infinity Mode. Did the game "
						+ "type change during runtime?");
		}
	}
	
	private void endRound()
	{
		System.out.println("InfinityMode: endRound called.");
		gameManagement.setInGame(false);
		
		switch(gameManagement.getEndCode())
		{
			case 1://Win
				ObjectCollection.getEntityManagement().removeAllEntities(false, false);
				break;
			case 2://Lose
				if(ObjectCollection.getAudio().getCurrBGM() != TrackNames.LOSE)
				{
					ObjectCollection.getAudio().changeTrack(TrackNames.LOSE, null);
				}
				break;
			default:
				throw new RuntimeException(gameManagement.getEndCode() + " is an unrecognized end code for Infinity Mode.");
		}
	}

	@Override
	public void stopGame()
	{
		System.out.println("InfinityMode: stopGame called.");
		ObjectCollection.getEntityManagement().removeAllEntities(true, true);
		resetStats();
	}

	@Override
	public void frameCall(int elapsedTime)
	{
		if(gameManagement.getInGame() == false)
		{
			return;
		}
		
		checkGameStatus();
	}
}
