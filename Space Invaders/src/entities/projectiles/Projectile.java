package entities.projectiles;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import utils.ConstantValues.RenderLayer;

public abstract class Projectile extends Entity
{
	protected Entity target;
	
	protected float maxVelocity;
	protected float acceleration;
	
	public Projectile(Entity target, float screenDivX, float screenDivY, EntityFaction entityFaction)
	{
		this(screenDivX, screenDivY, RenderLayer.SPRITE2, entityFaction);
		this.target = target;
	}
	
	public Projectile(Entity target, float screenDivX, float screenDivY, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		this(screenDivX, screenDivY, renderLayer, entityFaction);
		this.target = target;
	}
	
	public Projectile(Entity target, float screenDivX, float screenDivY, byte renderLayer, EntityFaction entityFaction)
	{
		this(screenDivX, screenDivY, renderLayer, entityFaction);
		this.target = target;
	}
	
	public Projectile(float screenDivX, float screenDivY, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, renderLayer, entityFaction);
	}
	
	public Projectile(float screenDivX, float screenDivY, byte renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, renderLayer, entityFaction);
	}
	
	protected void renderUseless()
	{
		this.setHealth(0);
		this.setDamage(0);
	}
}
