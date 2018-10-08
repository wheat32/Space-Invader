package entities.enemies;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import gameModes.GameMode;
import system.Time;
import updates.CollisionListener;
import updates.UpdateListener;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;
import utils.Wall;

public class MarchingAlien extends Entity implements ConstantValues, CollisionListener, UpdateListener
{
	private short numInRow = 0;
	private AlienPack alienPack;
	private long timeBonusLength = 0;
	private GameMode gameMode;
	
	public MarchingAlien(float screenDivX, float screenDivY, short numInRow)
	{
		super(screenDivX, screenDivY, new Sprite(ALIENSPRITESHEET1, ALIEN_SPRITE_COUNT), RenderLayer.SPRITE1, EntityFaction.ALIEN);
		this.numInRow = numInRow;
		timeBonusLength = 25000;//25 seconds
		gameMode = ObjectCollection.getGameManagement().getGameMode();
		ObjectCollection.getMainLoop().addUpdateListener(this);
		ObjectCollection.getMainLoop().addCollisionListener(this);
	}
	
	public void setAlienPack(AlienPack alienPack)
	{
		this.alienPack = alienPack;
	}
	
	@Override
	public void move() 
	{
		//AlienPack controls movement
	}
	
	public short getNumInRow() 
	{
		return numInRow;
	}
	
	public int calculateScore()//TODO fix pausing in game messes up the time bonus
	{
		return (int) Math.round(((timeBonusLength > 0) ? timeBonusLength/32.0*gameMode.getLevel()/4.0 : 0) + 100.0*((gameMode.getLevel()/4.0)));
	}
	
	@Override
	public void update()
	{
		if(timeBonusLength <= 0)
		{
			return;
		}
		
		timeBonusLength -= Time.deltaTime();
	}

	@Override
	public void onCollision(CollisionListener o) 
	{	
		if(o instanceof Wall)
		{
			if(((Wall) o).isTouching(this) == true)
			{
				if(alienPack != null)
				{
					alienPack.moveDown(this);
					return;
				}
			}
		}
		else if(o instanceof Entity)
		{
			Entity e = (Entity) o;
			
			if(e.entityFaction == EntityFaction.FRIENDLY)
			{
				if(this.reduceHealth(e.getDamage()) == true)
				{
					gameMode.increaseKills();
					gameMode.increaseScore(calculateScore());
					
					if(alienPack != null)
					{
						alienPack.killAlien(this);
					}
					else
					{
						EntityManagement.removeEntity(this);
					}
					gameMode.checkGameStatus();
				}
				else
				{
					gameMode.increaseScore(calculateScore());
				}
			}
		}
	}
}
