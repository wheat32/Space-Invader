package core;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import utils.Stats;

public abstract class Entity 
{
	protected Sprite sprite;
	
	protected float rx, ry;//positions params
	protected float vx, vy;//velocity params
	protected float px, py;//percentage accross screen params
	protected Dimension dimension = new Dimension();
	protected float screenDivX, screenDivY;
	protected float defaultPosX, defaultPosY;
	protected int active = 0;
	
	/**
	 * No-arg constructor for entity. Should only be used for special-condition entities.
	 */
	public Entity()
	{
		
	}
	
	/**
	 * Constructor for entity. Should be used for very basic entities with no AI or movement.
	 * @param screenDivX - How the screen should be divided in terms of X.
	 * @param screenDivY - How the screen should be divided in terms of Y.
	 */
	public Entity(float screenDivX, float screenDivY)
	{
		dimension = new Dimension((int) (Stats.SCREEN_WIDTH*screenDivX), (int) (Stats.SCREEN_HEIGHT*screenDivY));
		this.screenDivX = screenDivX;
		this.screenDivY = screenDivY;
	}
	
	public abstract void move(int tm);
	public abstract void draw(Graphics2D gfx, int tm);
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
		
		dimension.width = (int) (Stats.SCREEN_WIDTH*screenDivX);
		dimension.height = (int) (Stats.SCREEN_HEIGHT*screenDivY);
		
		float percentageThroughX = rx/oldScreenWidth;
		float percentageThroughY = ry/oldScreenHeight;//TODO add fps counter
		
		rx = Stats.SCREEN_WIDTH * percentageThroughX;
		ry = Stats.SCREEN_HEIGHT * percentageThroughY;
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
}
