package projectiles;

import java.awt.Graphics2D;

import bosses.BossAlien;
import core.Entity;
import core.Sprite;
import enemies.MarchingAlien;
import miscEntities.Wall;
import shields.ShipShield;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.Stats;

public class LaserCannon extends Projectile implements ConstantValues 
{
	public LaserCannon(EntityManagement entityManagement, float screenDivX, float screenDivY)
	{
		super(entityManagement, screenDivX, screenDivY);
		maxVelocity = -0.0013f;
		acceleration = -0.00002f;
		sprite = new Sprite(PROJECTILESPRITESHEET1, MISSILE_SPRITE_COUNT);
		vx = -0.00012f;
	}

	@Override
	public void move(int tm) 
	{
		py = ry/Stats.SCREEN_HEIGHT + vy * tm;
		ry = Stats.SCREEN_HEIGHT*py;
		vy = vy + acceleration * tm/4;

		if(vy < maxVelocity)//check if it exceeds max velocity
		{
			vy = maxVelocity;
		}
		
		//System.out.println("MISSILE: py = " + py + " | ry = " + ry + " | vy = " + vy + " | SCREEN_HEIGHT: " + Stats.SCREEN_HEIGHT);
	}

	@Override
	public void draw(Graphics2D gfx, int tm) 
	{
		sprite.drawImage(gfx, (int)rx, (int)ry, (int)rx+dimension.width, (int)ry+dimension.height, tm, active);
	}

	@Override
	public boolean inCollision(Entity e) 
	{
		if(e instanceof Wall)
		{
			Wall w = (Wall) e;
			
			if(getBounds().intersects(w.getBounds()) == false)
			{
				//System.out.println("LaserCannon hit wall");
				entityManagement.onEndOfLife(this);
				return true;
			}
		}
		else if(e instanceof MarchingAlien)
		{
			MarchingAlien m = (MarchingAlien) e;
			
			if(getBounds().intersects(m.getBounds()))
			{
				//System.out.println("LaserCannon hit alien");
				entityManagement.onEndOfLife(this);
				Stats.increaseHits();
				return true;
			}
		}
		else if(e instanceof BossAlien)
		{
			BossAlien ba = (BossAlien) e;
			
			if(getBounds().intersects(ba.getBounds()) && ba.getShield().isRecharging() == true)
			{
				//The removal of the missile is handled in the BossAlien class.
				Stats.increaseHits();
				//System.out.println(System.currentTimeMillis() + " - LaserCannon hit BossAlien " + Stats.hits);
				return true;
			}
		}
		else if(e instanceof ShipShield)
		{
			ShipShield ss = (ShipShield) e;
			
			if(this.getBounds().intersects(ss.getBounds()) == true && ss.isRecharging() == false)
			{
				//The removal of the missile is handled in the ShipShield class.
				//THE ADDITION OF HITS IS HANDLED IN THE SHIPSHIELD CLASS
				return true;
			}
		}
		else if(e instanceof SpaceMine)
		{
			SpaceMine sm = (SpaceMine) e;
			
			if(this.getBounds().intersects(sm.getBounds()) == true && sm.isBlowingUp() == false)
			{
				//The removal of the missile is handled in the SpaceMine class.
				Stats.increaseHits();
				return true;
			}
		}
		//The check if missile is colliding with an alien happens in the MarchingAlien class.
		return false;
	}
}
