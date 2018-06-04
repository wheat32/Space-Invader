package entities.bosses;

import java.util.Random;

import entities.Entity;
import entities.Sprite;
import entities.projectiles.LaserCannon;
import entities.projectiles.SimpleEnemyLaserCannon;
import entities.projectiles.SpaceMine;
import entities.shields.ShipShield;
import gameModes.GameMode;
import miscEntities.Wall;
import system.Audio;
import system.Audio.Sfxs;
import system.Options;
import system.Time;
import updates.UpdateListener;
import utils.ConstantValues;
import utils.ObjectCollection;

public class BossAlien extends Boss implements ConstantValues, UpdateListener
{
	private Random rand = new Random();
	private ShipShield shield;
	private float vx;
	private int timesToFire = 0;
	private int normalFireDelay = 1600;
	private short intervalForMineCheck = 6000;
	private float failedMineChecks = 0.0f;
	
	public BossAlien(GameMode gameMode, Entity enemy, float screenDivX, float screenDivY)
	{
		super(gameMode, enemy, screenDivX, screenDivY);
		sprite = new Sprite(BOSSALIENSPRITESHEET1, ALIEN_SPRITE_COUNT);
		ObjectCollection.getRenderer().addUpdateListener(this);
		
		setPosition(Options.SCREEN_WIDTH / 2 - dimension.width / 2, dimension.height / 3 * -1);
		activate((short) 2);
		
		maxHealth = (int) Math.round(3000 + 120 * (gameMode.level * 1.1)) + rand.nextInt(240 + gameMode.level*36);
		currHealth = maxHealth;
		System.out.println("Boss has " + maxHealth + " health.");
		
		vx = 0.00004f + 0.000008f * (gameMode.level/8.0f);

		if(vx > 0.00106f)
		{
			vx = 0.00106f;
		}
		
		timeBonusLength = 120000  + System.currentTimeMillis();//120 seconds
	}
	
	@Override
	public void move() 
	{
		px = rx/Options.SCREEN_WIDTH + vx * Time.deltaTime();
		rx = Options.SCREEN_WIDTH*px;
		//System.out.println("BossAlien: rx is " + rx + " | ry is " + ry);
	}
	
	private void checkFire()
	{
		if(currHealth <= 0)
		{
			gameMode.killedAliens++;
			gameMode.score += calculateScore();
			ObjectCollection.getEntityManagement().removeEntity(this);
			//System.out.println("Score gained from death hit: " + calculateScore(true));
			return;
		}
		
		int elapsedTime = Time.deltaTime();
		
		//Time calculations
		timeBonusLength -= elapsedTime;	
		normalFireDelay -= elapsedTime;
		intervalForMineCheck -= elapsedTime;
		
		//Set how many times to fire
		if(timesToFire == 0)
		{
			this.active = ((short) 2);
			
			if(currHealth > maxHealth/3.0*2.0)
			{
				timesToFire = 3;
				normalFireDelay = 2400;
			}
			else if(currHealth > maxHealth/2.0)
			{
				timesToFire = 5;
				normalFireDelay = 2100;
			}
			else if(currHealth > maxHealth/3.0)
			{
				timesToFire = 7;
				normalFireDelay = 1800;
			}
			else
			{
				timesToFire = 9;
				normalFireDelay = 1600;
			}
		}
		
		//Start firing
		if(normalFireDelay <= 0 && timesToFire > 0)
		{
			shoot((byte) 0);
			normalFireDelay = 290;
		}
		
		//Check to fire a mine
		if(intervalForMineCheck <= 0)
		{
			if(rand.nextFloat() >= 0.88f - failedMineChecks)
			{
				shoot((byte) 1);
				intervalForMineCheck = 6400;
				failedMineChecks = 0;
			}
			else
			{
				failedMineChecks += 0.009f;
				intervalForMineCheck = (short) (4800 - (failedMineChecks*10000));//Subtracts intervals of 90
			}
		}
	}
	
	public void reset()
	{
		timesToFire = 0;
		intervalForMineCheck = 6000;
	}
	
	private void shoot(byte type)
	{			
		switch(type)
		{
			case 0://Normal cannon
				//System.out.println("Boss fired");
				this.active = ((short) 3);
				timesToFire--;
				SimpleEnemyLaserCannon enemyMissile = new SimpleEnemyLaserCannon(gameMode, enemy, 0.0166f, 0.0333f);
				enemyMissile.setPosition(rx + dimension.width/2, ry + (dimension.height/8)*4);
				ObjectCollection.getEntityManagement().addEntity(enemyMissile);
				Audio.playSound(Sfxs.EnemyShoot1);
				break;
			case 1://Mine
				SpaceMine mine = new SpaceMine(gameMode, enemy, 0.06f, 0.06f);
				mine.setPosition(rx + dimension.width/2, ry + (dimension.height/8)*4);
				ObjectCollection.getEntityManagement().addEntity(mine);
				break;
		}
		
	}
	
	public int calculateScore()
	{
		if(currHealth <= 0)
		{
			if(isDead() == true)
			{
				return (int) Math.round(timeBonusLength/12.6*gameMode.level/2.4 + (442.0*(gameMode.level/2.8)));
			}
			else
			{
				return (int) Math.round(442.0*((gameMode.level/2.8)));
			}
		}
		else
		{
			if(timeBonusLength > 0)
			{
				return (int) Math.round(timeBonusLength/164.4 * (gameMode.level/6.8) + 10 + (1.8*(gameMode.level/6.4)));
			}
			else
			{
				return 10 + (int) Math.round(1.8*((gameMode.level/6.4)));
			}
		}
	}

	@Override
	public boolean inCollision(Entity e) 
	{
		if(e instanceof Wall)//TODO finish this
		{
			if(rx < 0)
			{
				vx = Math.abs(vx);
				rx += Math.abs(rx);
			}
			else if(rx > Options.SCREEN_WIDTH - this.getDimension().width)
			{
				vx = -Math.abs(vx);
				rx -= rx+this.getDimension().width-Options.SCREEN_WIDTH;
			}
		}
		if(e instanceof LaserCannon)
		{
			LaserCannon m = (LaserCannon) e;
			
			if(isDead() == false && getBounds().intersects(m.getBounds()) && shield.isRecharging() == true)//Whether the alien is dead or not is evaluated in the draw method
			{
				ObjectCollection.getEntityManagement().removeEntity(m);
				currHealth -= 100;
				
				if(currHealth < 0)
				{
					currHealth = 0;
				}
				
				gameMode.score += calculateScore();
				Audio.playSound(Sfxs.Hit);
				//System.out.println("Score gained from normal hit: " + calculateScore(false));
				return true;
			}
		}
		return false;
	}
	
	public void setShield(ShipShield shield)
	{
		this.shield = shield;
		ObjectCollection.getEntityManagement().addEntity(shield);
	}
	
	public ShipShield getShield()
	{
		return shield;
	}

	@Override
	public void update()
	{
		checkFire();
	}
}
