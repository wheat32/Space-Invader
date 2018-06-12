package utils;

import java.awt.Rectangle;

import entities.Entity;
import system.Options;

public class Wall
{
	private Rectangle dimensions;
	
	public Wall(float screenDivX1, float screenDivY1, float screenDivX2, float screenDivY2)
	{
		dimensions = new Rectangle((int) (Options.SCREEN_WIDTH*screenDivX1), (int) (Options.SCREEN_HEIGHT*screenDivY1), 
				(int) (Options.SCREEN_WIDTH*screenDivX2), (int) (Options.SCREEN_HEIGHT*screenDivY2));
	}

	public boolean isTouching(Entity e)
	{
		if((e.getRx() <= this.dimensions.x && e.getRx() + e.getDimension().width >= this.dimensions.x) 
				|| (e.getRx() <= this.dimensions.width && e.getRx() + e.getDimension().width >= this.dimensions.width))
				//TODO add height
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isOutside(Entity e)
	{
		return false;//TODO do this method
	}
	
	public Rectangle getDimensions()
	{
		return dimensions;
	}
}
