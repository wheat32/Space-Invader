package entities.players;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import entities.weapons.LaserCannon;
import entities.weapons.Weapon;
import gameModes.GameMode;
import input.KeyInputManagement;
import system.Options;
import system.Time;
import updates.CollisionListener;
import updates.UpdateListener;
import utils.ConstantValues;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;
import utils.Wall;

public class SpaceShip extends Entity implements ConstantValues, UpdateListener, CollisionListener
{
	private GameMode gameMode;
	private Weapon weapon;
	
	private float defaultPosX, defaultPosY;
	
	private final float MAX_VELOCITY = 0.00035f;
	private final float DELTA_VELOCITY = 0.00003f;
	
	public SpaceShip(float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY, new Sprite(SHIPSPRITESHEET1, SPACESHIP_SPRITE_COUNT), RenderLayer.SPRITE1, EntityFaction.FRIENDLY);
		super.setHealth(1);
		super.damage = 1;
		gameMode = ObjectCollection.getGameManagement().getGameMode();
		weapon = new LaserCannon(this);
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
		//After winning a round, float up
		if((ObjectCollection.getGameManagement().getGameStatus() == GameStatus.WIN || ObjectCollection.getGameManagement().getGameStatus() == GameStatus.BIG_WIN 
				|| ObjectCollection.getGameManagement().getGameStatus() == GameStatus.POST_ROUND) && ry > this.getDimension().height*-1)
		{
			slowDown();
			py -= 0.00056f * Time.deltaTime();
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
			weapon.fire();
		}
		
		if(KeyInputManagement.leftKeyPressed == false && KeyInputManagement.rightKeyPressed == false)
		{
			slowDown();
			
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
	
	@Override
	public void update()
	{
		processKeys();
		move();
	}
	
	private void slowDown()
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
	}
	
	public void reset()
	{
		this.setPosition(defaultPosX, defaultPosY);
		weapon.reset();
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
	
	@Override
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		super.resize(oldScreenWidth, oldScreenHeight);
		
		setDefaultPosition(0.5f, ((Options.SCREEN_HEIGHT*1.0f/gameMode.getScreenSteps())*(1.0f*gameMode.getScreenSteps()-2)/(1.0f*Options.SCREEN_HEIGHT)));
	}

	public Weapon getWeapon()
	{
		return weapon;
	}
}
