package entities.projectiles;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import entities.shields.ShipShield;
import gameModes.GameMode;
import system.Options;
import system.Time;
import updates.CollisionListener;
import updates.UpdateListener;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.ObjectCollection;
import utils.Wall;

public class LaserCannon extends Projectile implements ConstantValues, UpdateListener, CollisionListener
{
	private GameMode gameMode;
	
	public LaserCannon(Entity origin, float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY, new Sprite(PROJECTILESPRITESHEET1, MISSILE_SPRITE_COUNT), RenderLayer.SPRITE2, origin.entityFaction);
		super.damage = 100;
		super.setHealth(1);
		maxVelocity = -0.0013f;
		acceleration = -0.00002f;
		vx = -0.00012f;
		gameMode = ObjectCollection.getGameManagement().getGameMode();
		ObjectCollection.getMainLoop().addUpdateListener(this);
		ObjectCollection.getMainLoop().addCollisionListener(this);
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
	public void update()
	{
		move();
	}

	@Override
	public void onCollision(CollisionListener o) 
	{
		if(this.isDead() == true)
		{
			return;
		}
		
		if(o instanceof Wall)
		{
			if(((Wall) o).isOutside(this) == true)
			{
				//System.out.println("LaserCannon hit wall");
				EntityManagement.removeEntity(this);
				this.renderUseless();
			}
			return;
		}
		else if(o instanceof ShipShield)
		{
			if(((ShipShield) o).isRecharging() == false)
			{
				EntityManagement.removeEntity(this);
				gameMode.increaseHits();
				this.renderUseless();
				return;
			}
		}
		else if(o instanceof SpaceMine)
		{
			if(((SpaceMine) o).isBlowingUp() == false)
			{
				EntityManagement.removeEntity(this);
				gameMode.increaseHits();
				this.renderUseless();
				return;
			}
		}
		else if(o instanceof Entity)
		{
			Entity e = (Entity) o;
			
			if(e.entityFaction != EntityFaction.FRIENDLY)
			{
				/*System.out.println("LaserCannon | Collided with " + e.toString() + " " + ((MarchingAlien) e).getNumInRow() + "\n" 
						+ "\tLaserCannon:   {" + this.getBounds() + "}\n"
						+ "\tMarchingAlien: {" + e.getBounds() + "}");*/
				EntityManagement.removeEntity(this);
				gameMode.increaseHits();
				this.renderUseless();
			}
		}
	}
}
