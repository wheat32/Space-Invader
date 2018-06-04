package miscEntities;

import entities.Entity;

public class Wall extends Entity
{
	public Wall(float screenDimX, float screenDimY)
	{
		super(screenDimX, screenDimY);
	}
		
	@Override
	public void move() 
	{
		return;
		//Nothing because walls don't move
	}
	
	@Override
	public int calculateScore()
	{
		return 0;
	}
	
	public boolean isOutSideWall(Entity e)
	{
		return false;
	}
	
	public boolean isTouchingWall(Entity e)
	{
		if(e.getDimension().width + e.getRx() > this.dimension.width || e.getRx() < 0
				|| e.getDimension().height + ry > this.dimension.height || e.getRy() < 0)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public boolean inCollision(Entity e) 
	{			
		return false;
	}
}
