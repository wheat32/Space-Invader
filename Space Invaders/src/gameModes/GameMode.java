package gameModes;

import io.InGamePrints;
import miscEntities.Wall;
import players.SpaceShip;
import utils.FrameListener;
import utils.GameManagementUtils;
import utils.ObjectCollection;

public abstract class GameMode implements FrameListener
{
	protected GameManagementUtils gameManagement;
	protected InGamePrints gamePrints;
	protected SpaceShip ship;
	protected Wall wall;
	
	public GameMode()
	{
		gameManagement = ObjectCollection.getGameManagement();
		ObjectCollection.getRenderer().addFrameListener(this);
	}
	
	protected abstract void resetStats();
	protected abstract void initEntitySetUp();
	protected abstract void startNextRound();
	protected abstract void spawnEntities();
	public abstract void startGame();
	public abstract void stopGame();
}
