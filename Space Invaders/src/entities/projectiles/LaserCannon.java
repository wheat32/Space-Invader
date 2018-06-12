package entities.projectiles;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import entities.shields.ShipShield;
import gameModes.GameMode;
import system.Options;
import system.Time;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;

public class LaserCannon extends Projectile implements ConstantValues 
{
	private GameMode gameMode;
	
	public LaserCannon(Entity origin, float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY, RenderLayer.SPRITE2, origin.entityFaction);
		super.damage = 100;
		super.setHealth(1);
		maxVelocity = -0.0013f;
		acceleration = -0.00002f;
		sprite = new Sprite(PROJECTILESPRITESHEET1, MISSILE_SPRITE_COUNT);
		vx = -0.00012f;
		gameMode = ObjectCollection.getGameManagement().getGameMode();
	}

	@Override
	public void move() 
	{
		py = ry/Options.SCREEN_HEIGHT + vy * Time.deltaTime();
		ry = Options.SCREEN_HEIGHT*py;
		vy = vy + acceleration * Time.deltaTime()/4;

		if(vy < maxVelocity)//check if it exceeds max velocity
		{
			vy = maxVelocity;
		}
		
		//System.out.println("MISSILE: py = " + py + " | ry = " + ry + " | vy = " + vy + " | SCREEN_HEIGHT: " + Stats.SCREEN_HEIGHT);
	}

	@Override
	public boolean inCollision() 
	{
		if(this.isDead() == true)
		{
			return false;
		}
		else if(gameMode.getWall().isOutside(this) == true)
		{
			//System.out.println("LaserCannon hit wall");
			EntityManagement.removeEntity(this);
			this.renderUseless();
			return true;
		}
		
		for(Entity e : EntityManagement.getEntities())
		{
			if(e instanceof ShipShield)
			{
				ShipShield ss = (ShipShield) e;
				
				if(this.getBounds().intersects(ss.getBounds()) == true && ss.isRecharging() == false)
				{
					EntityManagement.removeEntity(this);
					gameMode.increaseHits();
					this.renderUseless();
					return true;
				}
			}
			else if(e instanceof SpaceMine)
			{
				SpaceMine sm = (SpaceMine) e;
				
				if(this.getBounds().intersects(sm.getBounds()) == true && sm.isBlowingUp() == false)
				{
					EntityManagement.removeEntity(this);
					gameMode.increaseHits();
					this.renderUseless();
					return true;
				}
			}
			else if(e.entityFaction != EntityFaction.FRIENDLY && getBounds().intersects(e.getBounds()))
			{
				EntityManagement.removeEntity(this);
				gameMode.increaseHits();
				this.renderUseless();
				return true;
			}
		}
		
		//The check if missile is colliding with an alien happens in the MarchingAlien class.
		return false;
	}
}
