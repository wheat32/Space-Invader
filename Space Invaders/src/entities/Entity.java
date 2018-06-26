package entities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entities.EntityTags.EntityFaction;
import system.Options;
import updates.GraphicsListener;
import utils.ConstantValues.RenderLayer;
import utils.ObjectCollection;

public abstract class Entity implements GraphicsListener
{
	protected float rx, ry;//positions params
	protected float vx, vy;//velocity params
	protected float px, py;//percentage across screen params
	
	protected Dimension dimension = new Dimension();
	protected float screenDivX, screenDivY;
	protected byte renderLayer;
	
	protected Sprite sprite;
	protected int active = 0;
	
	public final EntityFaction entityFaction;
	protected int maxHealth;
	protected int currHealth;
	protected int damage;
	
	public Entity(EntityFaction entityFaction)
	{
		this.entityFaction = entityFaction;
	}
	
	public Entity(Sprite sprite, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		this(-1f, -1f, sprite, RenderLayer.SPRITE1.layer, entityFaction);
	}
	
	/**
	 * Constructor for entity.</br><i>Note: The rendering layer used will be SPRITE1</i>
	 * @param screenDivX - How the screen should be divided in terms of X.
	 * @param screenDivY - How the screen should be divided in terms of Y.
	 */
	public Entity(float screenDivX, float screenDivY, Sprite sprite, EntityFaction entityFaction)
	{
		this(screenDivX, screenDivY, sprite, RenderLayer.SPRITE1.layer, entityFaction);
	}
	
	/**
	 * Constructor for entity.
	 * @param screenDivX - How the screen should be divided in terms of X.
	 * @param screenDivY - How the screen should be divided in terms of Y.
	 * @param renderLayer - The layer to be rendered on.
	 */
	public Entity(float screenDivX, float screenDivY, Sprite sprite, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		this(screenDivX, screenDivY, sprite, renderLayer.layer, entityFaction);
	}
	
	/**
	 * Constructor for entity.
	 * @param screenDivX - How the screen should be divided in terms of X.
	 * @param screenDivY - How the screen should be divided in terms of Y.
	 * @param renderLayer - The layer to be rendered on (byte).
	 */
	public Entity(float screenDivX, float screenDivY, Sprite sprite, byte renderLayer, EntityFaction entityFaction)
	{
		dimension = new Dimension((int) (Options.SCREEN_WIDTH*screenDivX), (int) (Options.SCREEN_HEIGHT*screenDivY));
		this.screenDivX = screenDivX;
		this.screenDivY = screenDivY;
		this.sprite = sprite;
		this.renderLayer = renderLayer;
		this.entityFaction = entityFaction;
		ObjectCollection.getMainLoop().addGraphicsListener(this, renderLayer);
	}
	
	public abstract void move();
	
	@Override
	public void graphicsCall(Graphics2D gfx)
	{
		sprite.drawImage(gfx, getBounds(), active);
	}
	
	@Override
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
	
	/***
	 * Should only be used for position (NOT COLLISION) related functions.
	 * @return Rectangle - Returns rx, ry, rx+width, and ry+height
	 */
	public Rectangle getBounds()
	{
		return new Rectangle((int)rx, (int)ry, (int) (rx+dimension.width), (int) (ry+dimension.height));
	}
	
	/**
	 * Should only be used for collision (NOT POSITION) related functions.
	 * @return Rectangle - Returns the rx, ry, width, and height of the entity.
	 */
	public Rectangle getCollider()
	{
		return new Rectangle((int)rx, (int)ry, dimension.width, dimension.height);
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
	
	/***
	 * Receives the amount of damage the entity is taking and returns a boolean based off if the entity is dead after the health reduction.
	 * @param damage
	 * @return boolean - True if the entity is dead after the health reduction. False if not.
	 */
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
