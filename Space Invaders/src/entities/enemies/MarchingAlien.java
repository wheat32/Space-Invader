package entities.enemies;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import gameModes.GameMode;
import updates.CollisionListener;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;
import utils.Wall;

public class MarchingAlien extends Entity implements ConstantValues, CollisionListener
{
	private short numInRow = 0;
	private AlienPack alienPack;
	private long timeBonusLength = 0;
	private GameMode gameMode;
	
	public MarchingAlien(float screenDivX, float screenDivY, short numInRow)
	{
		super(screenDivX, screenDivY, new Sprite(ALIENSPRITESHEET1, ALIEN_SPRITE_COUNT), RenderLayer.SPRITE1, EntityFaction.ALIEN);
		this.numInRow = numInRow;
		timeBonusLength = 25000 + System.currentTimeMillis();//25 seconds
		gameMode = ObjectCollection.getGameManagement().getGameMode();
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
		int tempTimeBonus = (int) (System.currentTimeMillis() - timeBonusLength);
		if(tempTimeBonus > 0)
		{
			return (int) Math.round(tempTimeBonus/32.0*gameMode.level/4.0 + (100.0*(gameMode.level/4.0)));
		}
		else
		{
			return (int) Math.round(100.0*((gameMode.level/4.0)));
		}
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
					gameMode.killedAliens++;
					gameMode.score += (calculateScore());
					
					if(alienPack != null)
					{
						alienPack.killAlien(this);
					}
					else
					{
						EntityManagement.removeEntity(this);
					}
				}
			}
		}
	}
}
