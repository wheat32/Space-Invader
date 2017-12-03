package projectiles;

import java.awt.Graphics2D;
import java.util.Random;

import core.Entity;
import core.Sprite;
import miscEntities.Wall;
import players.SpaceShip;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;
import utils.Stats;

public class SimpleEnemyLaserCannon extends Projectile implements ConstantValues 
{
	public SimpleEnemyLaserCannon(EntityManagement entityManagement, Entity target, float screenDivX, float screenDivY)
	{
		super(entityManagement, target, screenDivX, screenDivY);
		maxVelocity = 0.0008f;
		acceleration = 0.000008f;
		sprite = new Sprite(ENEMYPROJECTILESPRITESHEET1, MISSILE_SPRITE_COUNT);
		setVelocities();
	}

	@Override
	public void move(int tm) 
	{
		px = rx/Stats.SCREEN_WIDTH + vx * tm;
		rx = Stats.SCREEN_WIDTH*px;
		py = ry/Stats.SCREEN_HEIGHT + vy * tm;
		ry = Stats.SCREEN_HEIGHT*py;
		
		vy += acceleration * tm/8.0;

		if(vy > maxVelocity)//check if it exceeds max velocity
		{
			vy = maxVelocity;
		}
		
		//System.out.println("vx = " + vx + " | vy = " + vy);
	}

	@Override
	public void draw(Graphics2D gfx, int tm) 
	{
		sprite.drawImage(gfx, (int)rx, (int)ry, (int)rx+dimension.width, (int)ry+dimension.height, tm, active);
	}
	
	public void killSelf()
	{
		ObjectCollection.getEntityManagement().onEndOfLife(this);
	}
	
	private void setVelocities()
	{
		Random rand = new Random();
		short range = 160;
		
		if(range - Stats.level > 80)
		{
			range -= Stats.level * 2;
		}
		else
		{
			range = 40;
		}
		
		float accuracy = (float) (rand.nextInt(range));
		accuracy -= range/2.0f;
		accuracy /= 1000000.0f;
		//System.out.println("Accuracy = " + accuracy);
		
		vy = 0.00004f;
		vx = ((target.getRx() + target.getDimension().width/2)/Stats.SCREEN_WIDTH 
				- (rx + getDimension().width/2)/Stats.SCREEN_WIDTH) / 1400 + accuracy;
	}

	@Override
	public boolean inCollision(Entity e) 
	{
		if(e instanceof Wall)
		{
			Wall w = (Wall) e;
			
			if(!getBounds().intersects(w.getBounds()))
			{
				entityManagement.onEndOfLife(this);
				return true;
			}
		}
		if(e instanceof SpaceShip)
		{
			SpaceShip s = (SpaceShip) e;
			
			if(getBounds().intersects(s.getBounds()))
			{
				entityManagement.onEndOfLife(this);
				entityManagement.onEndOfLife(s);
				return true;
			}
		}
		return false;
	}
}
