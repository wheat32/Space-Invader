package entities.enemies;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import gameModes.GameMode;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;

public class MarchingAlien extends Entity implements ConstantValues 
{
	private short numInRow = 0;
	private AlienPack alienPack;
	private long timeBonusLength = 0;
	private GameMode gameMode;
	
	public MarchingAlien(float screenDivX, float screenDivY, short numInRow)
	{
		super(screenDivX, screenDivY, RenderLayer.SPRITE1, EntityFaction.ALIEN);
		sprite = new Sprite(ALIENSPRITESHEET1, ALIEN_SPRITE_COUNT);
		this.numInRow = numInRow;
		timeBonusLength = 25000 + System.currentTimeMillis();//25 seconds
		gameMode = ObjectCollection.getGameManagement().getGameMode();
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
	public boolean inCollision() 
	{	
		if(ObjectCollection.getGameManagement().getWall().isTouching(this) == true)
		{
			if(alienPack != null)
			{
				alienPack.moveDown(this);
				return true;
			}
		}
		
		for(Entity e : EntityManagement.getEntities())
		{
			if(e.entityFaction != EntityFaction.FRIENDLY)
			{
				continue;
			}
			
			if(this.getBounds().intersects(e.getBounds()) == true)
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
				
				return true;
			}
		}
		
		return false;
	}
}
