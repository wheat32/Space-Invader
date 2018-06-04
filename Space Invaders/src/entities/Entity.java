package entities;

import java.awt.Dimension;
import java.awt.Rectangle;

import gameModes.GameMode;
import system.Options;
import updates.GraphicsListener;

public abstract class Entity implements GraphicsListener
{
	protected float rx, ry;//positions params
	protected float vx, vy;//velocity params
	protected float px, py;//percentage accross screen params
	protected Dimension dimension = new Dimension();
	protected float screenDivX, screenDivY;
	protected int active = 0;
	protected int maxHealth;
	protected int currHealth;
	protected int damage;
	protected long timeBonusLength;
	
	protected Sprite sprite;
	protected GameMode gameMode;
	
	/**
	 * No-arg constructor for entity. Should only be used for special-condition entities.
	 */
	public Entity()
	{
		
	}
	
	/**
	 * For special cases where the entity is not tangible
	 * @param gameMode - The current game mode being played.
	 */
	public Entity(GameMode gameMode)
	{
		this.gameMode = gameMode;
	}
	
	/**
	 * Constructor for entity. Should be used for very basic entities with no AI or movement.
	 * @param screenDivX - How the screen should be divided in terms of X.
	 * @param screenDivY - How the screen should be divided in terms of Y.
	 */
	public Entity(float screenDivX, float screenDivY)
	{
		dimension = new Dimension((int) (Options.SCREEN_WIDTH*screenDivX), (int) (Options.SCREEN_HEIGHT*screenDivY));
		this.screenDivX = screenDivX;
		this.screenDivY = screenDivY;
	}
	
	/**
	 * Constructor for entity. Should be used for entities which need to know meta information.
	 * @param gameMode - The current game mode being played.
	 * @param screenDivX - How the screen should be divided in terms of X.
	 * @param screenDivY - How the screen should be divided in terms of Y.
	 */
	public Entity(GameMode gameMode, float screenDivX, float screenDivY)
	{
		this.gameMode = gameMode;
		dimension = new Dimension((int) (Options.SCREEN_WIDTH*screenDivX), (int) (Options.SCREEN_HEIGHT*screenDivY));
		this.screenDivX = screenDivX;
		this.screenDivY = screenDivY;
	}
	
	public abstract void move();
	public abstract int calculateScore();
	public abstract boolean inCollision(Entity e);
	
	//USE AS LITTLE AS POSSIBLE
	public Rectangle getBounds()
	{
		return new Rectangle((int)rx, (int)ry, dimension.width, dimension.height);
	}
	
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		if(dimension == null)
		{
			return;
		}
		
		dimension.width = (int) (Options.SCREEN_WIDTH*screenDivX);
		dimension.height = (int) (Options.SCREEN_HEIGHT*screenDivY);
		
		float percentageThroughX = rx/oldScreenWidth;
		float percentageThroughY = ry/oldScreenHeight;
		
		rx = Options.SCREEN_WIDTH * percentageThroughX;
		ry = Options.SCREEN_HEIGHT * percentageThroughY;
	}
	
	public void setPosition(float x, float y)
	{
		rx = x;
		ry = y;
	}
	
	public void setVelocity(float u, float v)
	{
		vx = u;
		vy = v;
	}
	
	public void setDimension(int width, int height)
	{
		dimension.width = width;
		dimension.height = height;
	}
	
	public Dimension getDimension()
	{
		return new Dimension(dimension);
	}
	
	public void activate(short type)
	{
		active = (int) type;
	}
	
	public void deactivate()
	{
		active = 0;
	}
	
	public int getActive()
	{
		return active;
	}
	
	public float getRx() 
	{
		return rx;
	}
	
	public void setRx(float rx) 
	{
		this.rx = rx;
	}
	
	public float getRy() 
	{
		return ry;
	}
	
	public void setRy(float ry) 
	{
		this.ry = ry;
	}
	
	public float getVx() 
	{
		return vx;
	}
	
	public float getVy() 
	{
		return vy;
	}
	
	public float getPx()
	{
		return px;
	}
	
	public void setPx(float px)
	{
		this.px = px;
	}
	
	public float getPy()
	{
		return py;
	}
	
	public void setPy(float py)
	{
		this.py = py;
	}
	
	public float getScreenDivX()
	{
		return screenDivX;
	}
	
	public float getScreenDivY()
	{
		return screenDivY;
	}

	public Sprite getSprite()
	{
		return sprite;
	}

	public void setSprite(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	public int getCurrentHealth()
	{
		return currHealth;
	}
	
	public int getMaxHealth()
	{
		return maxHealth;
	}
	
	public void setHealth(int health)
	{
		maxHealth = health;
		currHealth = health;
	}
	
	public boolean reduceHealth(int damage)
	{
		currHealth -= damage;
		return isDead();
	}
	
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public boolean isDead()
	{
		return (currHealth <= 0) ? true : false;
	}
}
