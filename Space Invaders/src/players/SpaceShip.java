package players;

import java.awt.Graphics2D;

import core.Entity;
import core.Sprite;
import miscEntities.Wall;
import projectiles.LaserCannon;
import utils.ConstantValues;
import utils.KeyInputManagement;
import utils.ObjectCollection;
import utils.Stats;

public class SpaceShip extends Entity implements ConstantValues
{
	private final float MAX_VELOCITY = 0.00035f;
	private final float DELTA_VELOCITY = 0.00003f;
	private final long FIRING_INTERVAL = 250; //milliseconds
	private float reloadSpeed = 0.0018f;
	private int ammo = 10;
	private float ammof = 10.0f;
	private boolean reloading = false;
	private long lastFired = System.currentTimeMillis();
	
	public SpaceShip(float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY);
		sprite = new Sprite(SHIPSPRITESHEET1, SPACESHIP_SPRITE_COUNT);
	}
	
	@Override
	public void move(int tm) 
	{
		px = rx/Stats.SCREEN_WIDTH + vx * tm;
		rx = Stats.SCREEN_WIDTH*px;
		py = ry/Stats.SCREEN_HEIGHT;
		//System.out.println("Moving ship: rx: " + rx);
		
		if(ObjectCollection.getGameManagement().getInGame() == false && ry > this.getDimension().height*-1)
		{
			vx = 0;
			py = ry/Stats.SCREEN_WIDTH - 0.0005f * tm;
			ry = Stats.SCREEN_WIDTH*py;
		}
		
		if(ammo == 0)
		{
			reloading = true;
			reloadSpeed = 0.0013f;
			
			if(ammof >= 5)
			{
				ammo = Math.round(ammof);
			}
		}
		else
		{
			reloading = false;
			reloadSpeed = 0.0018f;
			ammo = Math.round(ammof);
		}
		
		if(ammof < 10)
		{
			ammof += reloadSpeed * tm;
		}
		else if(ammof > 10)
		{
			ammof = 10;
		}
	}

	@Override
	public void draw(Graphics2D gfx, int tm) 
	{
		sprite.drawImage(gfx, (int)rx, (int)ry, (int)rx+dimension.width, (int)ry+dimension.height, tm, active);
	}

	public void processKeys() 
	{	
		if(KeyInputManagement.leftKeyPressed == true)
		{
			//System.out.println("Processing left key.");
			vx = -Math.abs(vx) - DELTA_VELOCITY;
		}
		
		if(KeyInputManagement.rightKeyPressed == true)
		{
			//System.out.println("Processing right key.");
			vx = Math.abs(vx) + DELTA_VELOCITY;
		}
		
		if(KeyInputManagement.fireKeyPressed == true)
		{
			if(System.currentTimeMillis() - lastFired > FIRING_INTERVAL && ammo > 0)
			{
				//System.out.println("Processing fire key.");
				shoot((byte) 0);
			}
			else if(System.currentTimeMillis() - lastFired > FIRING_INTERVAL && ammo == 0)
			{
				ObjectCollection.getAudio().playSound(SfxNames.GUNOVERHEAT);
				lastFired = System.currentTimeMillis();
			}
		}
		
		if(KeyInputManagement.leftKeyPressed == false && KeyInputManagement.rightKeyPressed == false)
		{
			if(vx > 0)
			{
				vx -= DELTA_VELOCITY*1.5f;
			}
			else if(vx < 0)
			{
				vx += DELTA_VELOCITY*1.5f;
			}
			
			if(vx > -DELTA_VELOCITY*1.5f && vx < DELTA_VELOCITY*1.5f)
			{
				vx = 0;
			}
			
			active = 0;
		}
		else
		{
			active = 1;
		}
		
		if(vx < -MAX_VELOCITY)
		{
			vx = -MAX_VELOCITY;
		}
		if(vx > MAX_VELOCITY)
		{
			vx = MAX_VELOCITY;
		}
	}
	
	private void shoot(byte type)
	{
		switch(type)
		{
			case 0:
				LaserCannon laserCannon = new LaserCannon(ObjectCollection.getEntityManagement(), 0.0166f, 0.0333f);
				laserCannon.setPosition(rx + dimension.width/2 - laserCannon.getDimension().width/2,
						ry - dimension.height/2);
				ObjectCollection.getEntityManagement().addEntity(laserCannon);
				Stats.totalTimesShot++;
				Stats.timesShot++;
				ammo--;
				ammof -= 1.0f;
				vx *= 0.4f;
				lastFired = System.currentTimeMillis();
				ObjectCollection.getAudio().playSound(SfxNames.FRIENDLYSHOOT);
				break;
		}
	}
	
	public void resetAmmo()
	{
		ammo = 10;
		ammof = 10;
		lastFired = System.currentTimeMillis();
	}
	
	public void setDefaultPosition(float x, float y)
	{
		defaultPosX = x;
		defaultPosY = y;
	}
	
	public void reset()
	{
		resetAmmo();
		setPosition(defaultPosX, defaultPosY);
	}

	@Override
	public boolean inCollision(Entity e) 
	{
		if(e instanceof Wall)
		{
			Wall w = (Wall) e;
			
			if(rx >= w.getDimension().width - getDimension().getWidth())
			{
				vx = 0;
				rx = w.getDimension().width - (int)getDimension().getWidth();
				return true;
			}
			if(rx <= 0)
			{
				vx = 0;
				rx = 0;
				return true;
			}
		}
		return false;
	}

	public boolean getReloading()
	{
		return reloading;
	}
	
	public int getAmmo()
	{
		return ammo;
	}
}
