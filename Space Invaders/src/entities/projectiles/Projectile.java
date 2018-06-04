package entities.projectiles;

import entities.Entity;
import gameModes.GameMode;

public abstract class Projectile extends Entity
{
	protected Entity target;
	
	protected float maxVelocity;
	protected float acceleration;
	
	public Projectile(GameMode gameMode, float screenDivX, float screenDivY)
	{
		super(gameMode, screenDivX, screenDivY);
	}
	
	public Projectile(GameMode gameMode, Entity target, float screenDivX, float screenDivY)
	{
		super(gameMode, screenDivX, screenDivY);
		this.target = target;
	}
	
	protected void renderUseless()
	{
		this.setHealth(0);
		this.setDamage(0);
	}
}
