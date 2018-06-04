package entities.players;

import entities.Entity;
import entities.Sprite;
import entities.projectiles.LaserCannon;
import gameModes.GameMode;
import input.KeyInputManagement;
import miscEntities.Wall;
import system.Audio;
import system.Audio.Sfxs;
import system.Options;
import system.Time;
import utils.ConstantValues;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;

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
	private float defaultPosX, defaultPosY;
	private boolean movingUp = false;
	
	public SpaceShip(GameMode gameMode, float screenDivX, float screenDivY)
	{
		super(gameMode, screenDivX, screenDivY);
		super.setHealth(1);
		super.damage = 1;
		sprite = new Sprite(SHIPSPRITESHEET1, SPACESHIP_SPRITE_COUNT);
	}
	
	@Override
	public void move() 
	{
		int elapsedTime = Time.deltaTime();
		
		px = rx/Options.SCREEN_WIDTH + vx * elapsedTime;
		rx = Options.SCREEN_WIDTH*px;
		py = ry/Options.SCREEN_HEIGHT;
		//System.out.println("Moving ship: rx: " + rx);
		if(movingUp == true)
		{
			py -= 0.00016f * elapsedTime;
			ry = Options.SCREEN_HEIGHT*py;
			
			if(ry <= defaultPosY*Options.SCREEN_HEIGHT || gameMode.getStatus() == GameStatus.IN_GAME)
			{
				ry = defaultPosY*Options.SCREEN_HEIGHT;
				movingUp = false;
			}
			//System.out.println("movingUp: " + movingUp + " | ry = " + ry + " | py = " + py);
		}
		
		if(ObjectCollection.getGameManagement().getInGame() == false && ry > this.getDimension().height*-1)
		{
			vx = 0;
			py = ry/Options.SCREEN_WIDTH - 0.0005f * elapsedTime;
			ry = Options.SCREEN_WIDTH*py;
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
			ammof += reloadSpeed * elapsedTime;
		}
		else if(ammof > 10)
		{
			ammof = 10;
		}
	}

	public void processKeys() 
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
			if(System.currentTimeMillis() - lastFired > FIRING_INTERVAL && ammo > 0)
			{
				//System.out.println("Processing fire key.");
				shoot((byte) 0);
			}
			else if(System.currentTimeMillis() - lastFired > FIRING_INTERVAL && ammo == 0)
			{
				Audio.playSound(Sfxs.GunOverheat);
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
				LaserCannon laserCannon = new LaserCannon(gameMode, 0.0166f, 0.0333f);
				laserCannon.setPosition(rx + dimension.width/2 - laserCannon.getDimension().width/2,
						ry - dimension.height/2);
				ObjectCollection.getEntityManagement().addEntity(laserCannon);
				gameMode.totalTimesShot++;
				gameMode.timesShot++;
				ammo--;
				ammof -= 1.0f;
				vx *= 0.4f;
				lastFired = System.currentTimeMillis();
				Audio.playSound(Sfxs.FriendlyShoot1);
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
		defaultPosX = x/Options.SCREEN_WIDTH;
		defaultPosY = y/Options.SCREEN_HEIGHT;
	}
	
	public void reset()
	{
		resetAmmo();
		
		if(rx == 0 && ry == 0)
		{
			moveUp();
		}
		else
		{
			setPosition(defaultPosX*Options.SCREEN_WIDTH, defaultPosY*Options.SCREEN_HEIGHT);
		}
	}
	
	private void moveUp()
	{
		setPosition(defaultPosX*Options.SCREEN_WIDTH, Options.SCREEN_HEIGHT+this.getDimension().height);
		movingUp = true;
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
			
			if(this.getDimension().getWidth() + rx >= w.getDimension().width)
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
	
	@Override
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		super.resize(oldScreenWidth, oldScreenHeight);
		
		setDefaultPosition(Options.SCREEN_WIDTH/2 - this.getDimension().width,
				(Options.SCREEN_HEIGHT/gameMode.getScreenSteps())*(gameMode.getScreenSteps()-2));
	}
}
