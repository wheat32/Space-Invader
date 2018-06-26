package entities.players;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import entities.projectiles.LaserCannon;
import gameModes.GameMode;
import input.KeyInputManagement;
import system.Audio;
import system.Audio.Sfxs;
import system.Options;
import system.Time;
import updates.CollisionListener;
import updates.UpdateListener;
import utils.ConstantValues;
import utils.EntityManagement;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;
import utils.Wall;

public class SpaceShip extends Entity implements ConstantValues, UpdateListener, CollisionListener
{
	private GameMode gameMode;
	
	private float defaultPosX, defaultPosY;
	
	private final float MAX_VELOCITY = 0.00035f;
	private final float DELTA_VELOCITY = 0.00003f;
	
	private final long FIRING_INTERVAL = 250; //milliseconds
	private long canFireAt = System.currentTimeMillis();
	private float reloadSpeed = 0.0018f;
	private float ammo = 10.0f;
	private boolean reloading = false;
	
	public SpaceShip(float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY, new Sprite(SHIPSPRITESHEET1, SPACESHIP_SPRITE_COUNT), RenderLayer.SPRITE1, EntityFaction.FRIENDLY);
		super.setHealth(1);
		super.damage = 1;
		gameMode = ObjectCollection.getGameManagement().getGameMode();
		ObjectCollection.getMainLoop().addUpdateListener(this);
		ObjectCollection.getMainLoop().addCollisionListener(this);
	}
	
	@Override
	public void move() 
	{
		px = rx/Options.SCREEN_WIDTH + vx * Time.deltaTime();
		rx = Options.SCREEN_WIDTH*px;
		py = ry/Options.SCREEN_HEIGHT;
		//System.out.println("SpaceShip | Moving ship: rx - " + rx + " ry - " + ry);
		if(ObjectCollection.getGameManagement().getGameStatus() == GameStatus.WIN && ry > this.getDimension().height*-1)
		{
			py -= 0.00016f * Time.deltaTime();
			ry = Options.SCREEN_HEIGHT*py;
			//System.out.println("SpaceShip | ry = " + ry + " | py = " + py);
		}
	}

	private void processKeys() 
	{	
		if(gameMode.getStatus() != GameStatus.IN_GAME)
		{
			return;
		}
		
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
			if(canFireAt - System.currentTimeMillis() <= 0 && reloading == false)
			{
				if(reloading == false)
				{
					shoot((byte) 0);
				}
				else
				{
					Audio.playSound(Sfxs.GunOverheat);
					canFireAt = System.currentTimeMillis() + FIRING_INTERVAL;
				}
				//System.out.println("Processing fire key.");	
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
			
			if(Math.abs(vx) < DELTA_VELOCITY*1.5f)
			{
				vx = 0;
			}
			
			active = 0;
		}
		else
		{
			active = 1;
		}
		
		if(Math.abs(vx) > MAX_VELOCITY)
		{
			vx = (vx > 0) ? MAX_VELOCITY : -MAX_VELOCITY;
		}
	}
	
	private void checkAmmo()
	{
		reloading = (ammo == 0) ? true : false;
		reloadSpeed = (ammo == 0) ? 0.0013f : 0.0018f;
		
		if(ammo < 10)
		{
			ammo += reloadSpeed * Time.deltaTime();
			
			if(ammo > 10)
			{
				ammo = 10;
			}
		}
	}
	
	@Override
	public void update()
	{
		processKeys();
		move();
		checkAmmo();
	}
	
	private void shoot(byte type)
	{
		switch(type)
		{
			case 0:
				LaserCannon laserCannon = new LaserCannon(this, 0.0166f, 0.0333f);
				laserCannon.setPosition(rx + dimension.width/2 - laserCannon.getDimension().width/2,
						ry - dimension.height/2);
				EntityManagement.addEntity(laserCannon);
				gameMode.totalTimesShot++;
				gameMode.timesShot++;
				ammo--;
				vx *= 0.4f;
				canFireAt = System.currentTimeMillis() + FIRING_INTERVAL;
				Audio.playSound(Sfxs.FriendlyShoot1);
				break;
		}
	}
	
	public void reset()
	{
		this.setPosition(defaultPosX, defaultPosY);
		resetAmmo();
	}
	
	public void resetAmmo()
	{
		ammo = 10;
		canFireAt = System.currentTimeMillis();
	}
	
	/***
	 * Sets the default position given the percentage across the screen
	 * @param x - float between 0.0 and 1.0
	 * @param y - float between 0.0 and 1.0
	 */
	public void setDefaultPosition(float x, float y)
	{
		defaultPosX = (x*Options.SCREEN_WIDTH)-this.dimension.width/2;
		defaultPosY = (y*Options.SCREEN_HEIGHT)-this.dimension.height/2;
		//System.out.println("SpaceShip | defaultPosX: " + defaultPosX + " given " + x + " percentage - defaultPosY: " + defaultPosY + " given " + y + " percentage");
	}

	@Override
	public void onCollision(CollisionListener o) 
	{
		if(o instanceof Wall)
		{
			if(((Wall) o).isTouching(this) == true)
			{
				if(this.getDimension().getWidth() + rx >= o.getCollider().width)
				{
					vx = 0;
					rx = (float) (o.getCollider().width - this.getDimension().getWidth());
				}
				if(rx <= o.getCollider().x)
				{
					vx = 0;
					rx = 0;
				}
			}
		}
	}

	public boolean getReloading()
	{
		return reloading;
	}
	
	public float getAmmo()
	{
		return ammo;
	}
	
	@Override
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		super.resize(oldScreenWidth, oldScreenHeight);
		
		setDefaultPosition(0.5f, ((Options.SCREEN_HEIGHT*1.0f/gameMode.getScreenSteps())*(1.0f*gameMode.getScreenSteps()-2)/(1.0f*Options.SCREEN_HEIGHT)));
	}
}
