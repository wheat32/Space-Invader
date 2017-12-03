package enemies;

import java.awt.Graphics2D;

import core.Entity;
import core.Sprite;
import players.SpaceShip;
import projectiles.LaserCannon;
import utils.ConstantValues;
import utils.ObjectCollection;
import utils.Stats;

public class MarchingAlien extends Entity implements ConstantValues 
{
	private long timeBonusLength = 0;
	private short numInRow = 0;
	
	public MarchingAlien(float screenDivX, float screenDivY, short numInRow)
	{
		super(screenDivX, screenDivY);
		sprite = new Sprite(ALIENSPRITESHEET1, ALIEN_SPRITE_COUNT);
		this.numInRow = numInRow;
		timeBonusLength = 25000;//25 seconds
	}
	
	@Override
	public void move(int tm) 
	{
		//AlienPack controls movement
	}
	
	public void lowerTimeBonus(long tm)
	{
		if(timeBonusLength > 0)
		{
			timeBonusLength -= tm;
		}
	}

	@Override
	public void draw(Graphics2D gfx, int tm) 
	{
		sprite.drawImage(gfx, (int)rx, (int)ry, (int)rx+dimension.width, (int)ry+dimension.height, tm, active);
	}
	
	public short getNumInRow() 
	{
		return numInRow;
	}
	
	private int calculateScore()//TODO fix pausing in game messes up the time bonus
	{
		if(timeBonusLength > 0)
		{
			return (int) Math.round(timeBonusLength/32.0*Stats.level/4.0 + (100.0*(Stats.level/4.0)));
		}
		else
		{
			return (int) Math.round(100.0*((Stats.level/4.0)));
		}
	}

	@Override
	public boolean inCollision(Entity e) 
	{	
		if(e instanceof LaserCannon)
		{
			LaserCannon m = (LaserCannon) e;
			
			if(getBounds().intersects(m.getBounds()))
			{
				ObjectCollection.getEntityManagement().onEndOfLife(this);
				ObjectCollection.getEntityManagement().onEndOfLife(m);
				Stats.killedAliens++;
				ObjectCollection.getAudio().playSound(SfxNames.HIT);
				Stats.score += (calculateScore());
				return true;
			}
		}
		if(e instanceof SpaceShip)
		{
			SpaceShip ss = (SpaceShip) e;
			
			if(getBounds().intersects(ss.getBounds()))
			{
				ObjectCollection.getEntityManagement().onEndOfLife(this);
				ObjectCollection.getEntityManagement().onEndOfLife(ss);
				Stats.killedAliens++;
			}
		}
		return false;
	}
}
