package gameModes;

import java.text.DecimalFormat;

import entities.players.SpaceShip;
import io.InGamePrints;
import system.Time;
import updates.UpdateListener;
import utils.EntityManagement;
import utils.GameManagementUtils;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;
import utils.Wall;

public abstract class GameMode implements UpdateListener
{
	//Stats
	protected int level = 0;
	protected long score = 0;
	protected int totalTimesShot = 0;
	protected int totalHits = 0;
	protected int timesShot = 0;
	protected int hits = 0;
	protected int kills = 0;
	protected short lives = 0;
	protected long totalTime = 0;
	protected long roundTime = 0;
	
	protected boolean inGame = false;
	protected byte roundType = 0;
	protected GameStatus status = GameStatus.PRE_ROUND;
	
	protected short screenSteps = 16;
	
	protected GameManagementUtils gameManagement;
	protected InGamePrints gamePrints;
	protected SpaceShip ship;
	protected Wall wall;
	
	public GameMode()
	{
		gameManagement = ObjectCollection.getGameManagement();
		ObjectCollection.getMainLoop().addUpdateListener(this);
	}
	
	protected abstract void resetStats();
	protected abstract void initEntitySetUp();
	protected abstract void spawnEntities();
	public abstract void checkGameStatus();
	
	@Override
	public void update()
	{
		if(status == GameStatus.IN_GAME)
		{
			roundTime += Time.deltaTime();
			totalTime += Time.deltaTime();
		}
	}
	
	public void startGame()
	{
		inGame = true;
	}
	
	public void stopGame()
	{
		inGame = false;
		EntityManagement.removeAllEntities(true);
		resetStats();
	}
	
	public String getAccuracy(boolean total)
	{
		DecimalFormat format = new DecimalFormat("##0.##%");
		
		if(total == true)
		{
			if((totalHits*1.0/totalTimesShot*1.0) >= 1)
			{
				return format.format(1);
			}
			else
			{
				return format.format(totalHits*1.0f/totalTimesShot*1.0f);
			}
		}
		else
		{
			if((hits*1.0/timesShot*1.0) >= 1)
			{
				return format.format(1);
			}
			else
			{
				return format.format(hits*1.0f/timesShot*1.0f);
			}
		}
	}
	
	public void increaseShots()
	{
		timesShot++;
		totalTimesShot++;
	}
	
	public void increaseKills()
	{
		kills++;
	}
	
	public void increaseHits()
	{
		totalHits++;
		hits++;
	}
	
	public void increaseScore(int score)
	{
		this.score += score;
	}
	
	public SpaceShip getPlayerShip()
	{
		return ship;
	}
	
	public boolean getInGame()
	{
		return inGame;
	}
	
	public GameStatus getStatus()
	{
		return status;
	}
	
	public short getScreenSteps()
	{
		return screenSteps;
	}

	public int getLevel()
	{
		return level;
	}

	public long getScore()
	{
		return score;
	}

	public int getTimesShot()
	{
		return timesShot;
	}

	public short getLives()
	{
		return lives;
	}

	public long getTotalTime()
	{
		return totalTime;
	}

	public long getRoundTime()
	{
		return roundTime;
	}

	public byte getRoundType()
	{
		return roundType;
	}

	public int getTotalTimesShot()
	{
		return totalTimesShot;
	}

	public int getTotalHits()
	{
		return totalHits;
	}

	public int getHits()
	{
		return hits;
	}

	public int getKills()
	{
		return kills;
	}
}
