package projectiles;

import core.Entity;
import utils.EntityManagement;

public abstract class Projectile extends Entity
{
	protected EntityManagement entityManagement;
	protected Entity target;
	
	protected float maxVelocity;
	protected float acceleration;
	
	public Projectile(EntityManagement entityManagement, float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY);
		this.entityManagement = entityManagement;
	}
	
	public Projectile(EntityManagement entityManagement, Entity target, float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY);
		this.target = target;
		this.entityManagement = entityManagement;
	}
}
