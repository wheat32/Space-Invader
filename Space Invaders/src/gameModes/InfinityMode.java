package gameModes;

import entities.Entity;
import entities.bosses.Boss;
import entities.bosses.BossAlien;
import entities.enemies.AlienPack;
import entities.players.SpaceShip;
import entities.shields.ShipShield;
import input.KeyInputManagement;
import io.InGamePrints;
import system.Audio;
import system.Audio.Tracks;
import system.Logger;
import system.Options;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;
import utils.Wall;

/*
 * For infinity mode:
 * - roundType: 0 = normal wave, 1 = boss wave
 */

public class InfinityMode extends GameMode implements ConstantValues
{
	public int numberInPack = 0;
	private Boss boss;
	private Entity aliens;
	
	public InfinityMode()
	{
		super();
		screenSteps = ObjectCollection.getGameManagement().getAlienSteps();
	}

	@Override
	public void resetStats()
	{
		super.totalTimesShot = 0;
		super.level = 0;
		super.kills = 0;
		super.score = 0;
		super.totalHits = 0;
		super.totalTime = 0;
		super.lives = ObjectCollection.getGameManagement().getStartingLives();
		resetPerRoundStats();
	}
	
	public void resetPerRoundStats()
	{
		timesShot = 0;
		hits = 0;
		roundTime = 0;
	}
	
	@Override
	protected void initEntitySetUp()
	{
		wall = new Wall(0f, 0f, 1.0f, 1.0f);
		ship = new SpaceShip(0.05f, 0.05f);
		ship.setDefaultPosition(0.5f, ((Options.SCREEN_HEIGHT*1.0f/screenSteps)*(1.0f*screenSteps-2))/(1.0f*Options.SCREEN_HEIGHT));
		EntityManagement.addEntity(ship);
	}
	
	private Runnable startNextRound = new Runnable()
	{	
		@Override
		public void run()
		{
			status = GameStatus.PRE_ROUND;
			Audio.changeTrack(Tracks.BGM3);
			ship.reset();
			waitForInput();
			status = GameStatus.IN_GAME;
			inGame = true;
			level++;
			roundType = getNextRoundType();
			spawnEntities();
			Audio.changeTrack(Tracks.BGM1);
		}
	};

	private byte getNextRoundType()
	{
		//Is this next round a boss?
		return (byte) ((super.level % ObjectCollection.getGameManagement().getRoundsBetweenBoss() == 0) ? 1 : 0);
	}
	
	@Override
	protected void spawnEntities()
	{
		switch(roundType)
		{
			case 0://Normal wave
				numberInPack = 4 * ((super.level < 3) ? 9 + super.level : 12);
				aliens = new AlienPack(numberInPack);
				EntityManagement.addEntity(aliens);
				break;
			case 1://Boss
				boss = new BossAlien(ship, 0.4f, 0.4f);
				if(boss.hasShield() == true)
				{
					((BossAlien) boss).setShield(new ShipShield(boss));
				}
				EntityManagement.addEntity(boss);
				break;
			default:
				throw new RuntimeException(roundType + " is an invalid round type for Infinity Mode.");
		}
	}

	@Override
	public void startGame()
	{
		Logger.stdout(this.getClass(), "startGame called.");
		super.startGame();
		EntityManagement.removeAllEntities(true);
		gamePrints = new InGamePrints(this);
		Audio.openClips(new Tracks[] {Tracks.BGM1, Tracks.BGM3, Tracks.BossBGM1, Tracks.Victory1, Tracks.Victory2, Tracks.Lose1});
		boss = null;
		aliens = null;
		resetStats();
		initEntitySetUp();
		new Thread(startNextRound).start();
	}
	
	@Override
	public void checkGameStatus()
	{
		switch(roundType)
		{
			case 0://Normal wave
				if(aliens instanceof AlienPack == true)
				{
					if(((AlienPack) aliens).getAliensAlive() == 0)//Won normal round//TODO make this more universal
					{
						status = GameStatus.WIN;//TODO change this back to WIN
						endRound();
					}
				}
				break;
			case 1://Boss
				break;
			default:
				throw new RuntimeException(roundType + " is an invalid round type for Infinity Mode. Did the game "
						+ "type change during runtime?");
		}
	}
	
	private void endRound()
	{
		Logger.stdout(this.getClass(), "endRound called.");
		inGame = false;
		
		switch(status)
		{
			case WIN:
				EntityManagement.removeAllEntities(false);
				Audio.changeTrack(Tracks.Victory2);
				status = GameStatus.POST_ROUND;
				break;
			case LOSE:
				Audio.changeTrack(Tracks.Lose1);
				break;
			case BIG_WIN:
				Audio.changeTrack(Tracks.Victory1);
				status = GameStatus.POST_ROUND;
				break;
			default:
				throw new RuntimeException(gameManagement.getGameStatus() + " is an unrecognized end code for Infinity Mode.");
		}
	}
	
	private void waitForInput()
	{
		while(KeyInputManagement.fireKeyPressed == true)
		{
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		while(KeyInputManagement.fireKeyPressed != true)
		{
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
