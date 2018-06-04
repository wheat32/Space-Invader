package gameModes;

import entities.Entity;
import entities.bosses.Boss;
import entities.bosses.BossAlien;
import entities.enemies.AlienPack;
import entities.players.SpaceShip;
import entities.shields.ShipShield;
import input.KeyInputManagement;
import io.InGamePrints;
import miscEntities.Wall;
import system.Audio;
import system.Audio.Tracks;
import system.Options;
import utils.ConstantValues;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;

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
		super.killedAliens = 0;
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
		wall = new Wall(1.0f, 1.0f);
		ObjectCollection.getEntityManagement().addEntity(wall);
		ship = new SpaceShip(this, 0.05f, 0.05f);
		ship.setDefaultPosition(Options.SCREEN_WIDTH/2 - ship.getDimension().width,
				(Options.SCREEN_HEIGHT/screenSteps)*(screenSteps-2));
		ObjectCollection.getEntityManagement().addEntity(ship);
	}
	
	private Runnable startNextRound = new Runnable()
	{	
		@Override
		public void run()
		{
			status = GameStatus.PRE_ROUND;
			changeMusic();
			ship.reset();
			waitForInput();
			status = GameStatus.IN_GAME;
			inGame = true;
			level++;
			roundType = getNextRoundType();
			spawnEntities();
			changeMusic();
		}
	};

	private byte getNextRoundType()
	{
		//Is this next round a boss?
		return (byte) ((super.level % ObjectCollection.getGameManagement().getRoundsBetweenBoss() == 0) ? 1 : 0);
	}
	
	private void changeMusic()
	{
		switch(status)
		{
			case PRE_ROUND:
				Audio.changeTrack(Tracks.BGM3);
				break;
			case IN_GAME:
				switch(roundType)
				{
					default:
					case 0://Normal wave
						Audio.changeTrack(Tracks.BGM1);
						break;
					case 1://Boss
						Audio.changeTrack(Tracks.BossBGM1);
						break;
				}
				break;
			case BIG_WIN:
				Audio.changeTrack(Tracks.Victory1);
			default:
				break;
		}
	}
	
	@Override
	protected void spawnEntities()
	{
		switch(roundType)
		{
			case 0://Normal wave
				numberInPack = 4 * ((super.level < 3) ? 9 + super.level : 12);
				aliens = new AlienPack(this, numberInPack);
				ObjectCollection.getEntityManagement().addEntity(aliens);
				break;
			case 1://Boss
				boss = new BossAlien(this, ship, 0.4f, 0.4f);
				if(boss.hasShield() == true)
				{
					((BossAlien) boss).setShield(new ShipShield(boss));
				}
				ObjectCollection.getEntityManagement().addEntity(boss);
				break;
			default:
				throw new RuntimeException(roundType + " is an invalid round type for Infinity Mode.");
		}
	}

	@Override
	public void startGame()
	{
		System.out.println("InfinityMode: startGame called.");
		super.startGame();
		ObjectCollection.getEntityManagement().removeAllEntities(true, true);
		gamePrints = new InGamePrints(this);
		Audio.openClips(new Tracks[] {Tracks.BGM1, Tracks.BGM3, Tracks.BossBGM1, Tracks.Victory1, Tracks.Victory2, Tracks.Lose1});
		boss = null;
		aliens = null;
		resetStats();
		initEntitySetUp();
		new Thread(startNextRound).start();;
	}
	
	private void checkGameStatus()
	{
		switch(roundType)
		{
			case 0://Normal wave
				if(aliens instanceof AlienPack == true)
				{
					if(((AlienPack) aliens).getAliensAlive() == 0)//Won normal round
					{
						score += aliens.calculateScore();
						status = GameStatus.BIG_WIN;//TODO change this back to WIN
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
		System.out.println("InfinityMode: endRound called.");
		inGame = false;
		
		switch(status)
		{
			case WIN:
				ObjectCollection.getEntityManagement().removeAllEntities(false, false);
				break;
			case LOSE:
				break;
			case BIG_WIN:
				break;
			default:
				throw new RuntimeException(gameManagement.getGameStatus() + " is an unrecognized end code for Infinity Mode.");
		}
		
		changeMusic();
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

	@Override
	public void update()
	{
		if(inGame == false)
		{
			return;
		}
		
		checkGameStatus();
	}
}
