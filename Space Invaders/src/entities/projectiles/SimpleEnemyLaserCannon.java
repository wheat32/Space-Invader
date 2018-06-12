package entities.projectiles;

import java.util.Random;

import entities.Entity;
import entities.Sprite;
import entities.players.SpaceShip;
import system.Options;
import system.Time;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;
import utils.Wall;

public class SimpleEnemyLaserCannon extends Projectile implements ConstantValues 
{
	public SimpleEnemyLaserCannon(Entity target, Entity origin, float screenDivX, float screenDivY)
	{
		super(target, screenDivX, screenDivY, RenderLayer.SPRITE2, origin.entityFaction);
		super.setHealth(1);
		super.damage = 1;
		maxVelocity = 0.0008f;
		acceleration = 0.000008f;
		sprite = new Sprite(ENEMYPROJECTILESPRITESHEET1, MISSILE_SPRITE_COUNT);
		setVelocities();
	}

	@Override
	public void move() 
	{
		int elapsedTime = Time.deltaTime();
		
		px = rx/Options.SCREEN_WIDTH + vx * elapsedTime;
		rx = Options.SCREEN_WIDTH*px;
		py = ry/Options.SCREEN_HEIGHT + vy * elapsedTime;
		ry = Options.SCREEN_HEIGHT*py;
		
		vy += acceleration * elapsedTime/8.0;

		if(vy > maxVelocity)//check if it exceeds max velocity
		{
			vy = maxVelocity;
		}
		
		//System.out.println("vx = " + vx + " | vy = " + vy);
	}

	public void killSelf()
	{
		EntityManagement.removeEntity(this);
	}
	
	private void setVelocities()
	{
		Random rand = new Random();
		short range = 160;
		
		if(range - ObjectCollection.getGameManagement().getGameMode().level > 80)
		{
			range -= ObjectCollection.getGameManagement().getGameMode().level * 2;
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
		vx = ((target.getRx() + target.getDimension().width/2)/Options.SCREEN_WIDTH - (rx + getDimension().width/2)/Options.SCREEN_WIDTH) / 1400 + accuracy;
	}

	@Override
	public boolean inCollision() 
	{
		if(ObjectCollection.getGameManagement().getWall().isOutside(this) == true)
		{
			EntityManagement.removeEntity(this);
			return true;
		}
		
		for(Entity e : EntityManagement.getEntities())
		{
			if(e.entityFaction == target.entityFaction)
			{
				if(getBounds().intersects(e.getBounds()))
				{
					EntityManagement.removeEntity(this);
					return true;
				}
			}
		}
		return false;
	}
}
