package entities.projectiles;

import entities.Entity;
import entities.Sprite;
import entities.bosses.BossAlien;
import entities.enemies.MarchingAlien;
import entities.shields.ShipShield;
import gameModes.GameMode;
import miscEntities.Wall;
import system.Options;
import system.Time;
import utils.ConstantValues;
import utils.ObjectCollection;

public class LaserCannon extends Projectile implements ConstantValues 
{
	public LaserCannon(GameMode gameMode, float screenDivX, float screenDivY)
	{
		super(gameMode, screenDivX, screenDivY);
		super.damage = 100;
		super.setHealth(1);
		maxVelocity = -0.0013f;
		acceleration = -0.00002f;
		sprite = new Sprite(PROJECTILESPRITESHEET1, MISSILE_SPRITE_COUNT);
		vx = -0.00012f;
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
	public int calculateScore()
	{
		return 0;
	}

	@Override
	public boolean inCollision(Entity e) 
	{
		if(this.isDead() == true)
		{
			return false;
		}
		
		if(e instanceof Wall)
		{
			Wall w = (Wall) e;
			
			if(getBounds().intersects(w.getBounds()) == false)//Must be like this so the shot is completely off the screen before disappearing
			{
				//System.out.println("LaserCannon hit wall");
				ObjectCollection.getEntityManagement().removeEntity(this);
				this.renderUseless();
				return true;
			}
		}
		else if(e instanceof MarchingAlien)
		{
			MarchingAlien m = (MarchingAlien) e;
			
			if(getBounds().intersects(m.getBounds()))
			{
				//System.out.println("LaserCannon hit alien");
				ObjectCollection.getEntityManagement().removeEntity(this);
				gameMode.increaseHits();
				this.renderUseless();
				return true;
			}
		}
		else if(e instanceof BossAlien)
		{
			BossAlien ba = (BossAlien) e;
			
			if(getBounds().intersects(ba.getBounds()) && ba.getShield().isRecharging() == true)
			{
				//The removal of the missile is handled in the BossAlien class.
				gameMode.increaseHits();
				//System.out.println(System.currentTimeMillis() + " - LaserCannon hit BossAlien " + Stats.hits);
				this.renderUseless();
				return true;
			}
		}
		else if(e instanceof ShipShield)
		{
			ShipShield ss = (ShipShield) e;
			
			if(this.getBounds().intersects(ss.getBounds()) == true && ss.isRecharging() == false)
			{
				ObjectCollection.getEntityManagement().removeEntity(this);
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
				ObjectCollection.getEntityManagement().removeEntity(this);
				gameMode.increaseHits();
				this.renderUseless();
				return true;
			}
		}
		//The check if missile is colliding with an alien happens in the MarchingAlien class.
		return false;
	}
}
