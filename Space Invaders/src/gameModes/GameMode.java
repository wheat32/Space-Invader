package gameModes;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import entities.players.SpaceShip;
import io.InGamePrints;
import miscEntities.Wall;
import updates.UpdateListener;
import utils.GameManagementUtils;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;

public abstract class GameMode implements UpdateListener
{
	//Stats
	public int level = 0;
	public long score = 0;
	public int totalTimesShot = 0;
	public int totalHits = 0;
	public int timesShot = 0;
	public int hits = 0;
	public int killedAliens = 0;
	public short lives = 0;
	public long totalTime = 0;
	public long roundTime = 0;
	
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
		ObjectCollection.getRenderer().addUpdateListener(this);
	}
	
	protected abstract void resetStats();
	protected abstract void initEntitySetUp();
	protected abstract void spawnEntities();
	
	public void startGame()
	{
		inGame = true;
	}
	
	public void stopGame()
	{
		inGame = false;
		ObjectCollection.getEntityManagement().removeAllEntities(true, true);
		resetStats();
	}
	
	private static DecimalFormat millisecondsFormat = new DecimalFormat("000");
	private static DecimalFormat secondsFormat = new DecimalFormat("00");
	
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
	
	public void increaseHits()
	{
		totalHits++;
		hits++;
	}
	
	public String getRoundTimeFormatted()//TODO this doesn't belong here
	{
		return TimeUnit.MILLISECONDS.toMinutes(roundTime) + ":" + 
				secondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(roundTime)%60) + ":" + 
				millisecondsFormat.format(TimeUnit.MILLISECONDS.toMillis(roundTime)%1000);
	}
	
	public String getTotalTimeFormatted()//TODO this doesn't belong here
	{
		return TimeUnit.MILLISECONDS.toMinutes(totalTime) + ":" + 
				secondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(totalTime)%60) + ":" + 
				millisecondsFormat.format(TimeUnit.MILLISECONDS.toMillis(totalTime)%1000);
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
}
