package entities.projectiles;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import utils.ConstantValues.RenderLayer;

public abstract class Projectile extends Entity
{
	protected Entity target;
	
	protected float maxVelocity;
	protected float acceleration;
	
	public Projectile(Entity target, float screenDivX, float screenDivY, Sprite sprite, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, sprite, RenderLayer.SPRITE2.layer, entityFaction);
		this.target = target;
	}
	
	public Projectile(Entity target, float screenDivX, float screenDivY, Sprite sprite, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, sprite, renderLayer.layer, entityFaction);
		this.target = target;
	}
	
	public Projectile(Entity target, float screenDivX, float screenDivY, Sprite sprite, byte renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, sprite, renderLayer, entityFaction);
		this.target = target;
	}
	
	public Projectile(float screenDivX, float screenDivY, Sprite sprite, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, sprite, renderLayer, entityFaction);
	}
	
	public Projectile(float screenDivX, float screenDivY, Sprite sprite, byte renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, sprite, renderLayer, entityFaction);
	}
	
	protected void renderUseless()
	{
		this.setHealth(0);
		this.setDamage(0);
		this.acceleration = 0;
		this.setVelocity(0, 0);
	}
}
