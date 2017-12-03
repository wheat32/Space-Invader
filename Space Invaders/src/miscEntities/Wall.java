package miscEntities;

import java.awt.Graphics2D;

import core.Entity;
import utils.ConstantValues;

public class Wall extends Entity implements ConstantValues 
{
	public Wall(float screenDimX, float screenDimY)
	{
		super(screenDimX, screenDimY);
	}
		
	@Override
	public void move(int tm) 
	{
		return;
		//Nothing because walls don't move
	}

	@Override
	public void draw(Graphics2D gfx, int tm) 
	{
		return;
		//Unnecessary
	}

	@Override
	public boolean inCollision(Entity e) 
	{	
		return false;
	}
}
