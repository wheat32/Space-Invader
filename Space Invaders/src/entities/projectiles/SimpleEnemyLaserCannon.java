package entities.projectiles;

import java.util.Random;

import entities.Entity;
import entities.Sprite;
import entities.players.SpaceShip;
import gameModes.GameMode;
import miscEntities.Wall;
import system.Options;
import system.Time;
import utils.ConstantValues;
import utils.ObjectCollection;

public class SimpleEnemyLaserCannon extends Projectile implements ConstantValues 
{
	public SimpleEnemyLaserCannon(GameMode gameMode, Entity target, float screenDivX, float screenDivY)
	{
		super(gameMode, target, screenDivX, screenDivY);
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
		ObjectCollection.getEntityManagement().removeEntity(this);
	}
	
	private void setVelocities()
	{
		Random rand = new Random();
		short range = 160;
		
		if(range - gameMode.level > 80)
		{
			range -= gameMode.level * 2;
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
	public int calculateScore()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean inCollision(Entity e) 
	{
		if(e instanceof Wall)
		{
			Wall w = (Wall) e;
			
			if(!getBounds().intersects(w.getBounds()))
			{
				ObjectCollection.getEntityManagement().removeEntity(this);
				return true;
			}
		}
		if(e instanceof SpaceShip)
		{
			SpaceShip s = (SpaceShip) e;
			
			if(getBounds().intersects(s.getBounds()))
			{
				ObjectCollection.getEntityManagement().removeEntity(this);
				return true;
			}
		}
		return false;
	}
}
