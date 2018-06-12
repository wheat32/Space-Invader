package entities.bosses;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.shields.ShipShield;
import utils.ConstantValues.RenderLayer;

public abstract class Boss extends Entity
{
	protected Entity enemy;//The boss' enemy
	protected ShipShield shield;
	
	public Boss(Entity enemy, float screenDivX, float screenDivY, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		this(enemy, screenDivX, screenDivY, renderLayer.layer, entityFaction);
	}
	
	public Boss(Entity enemy, float screenDivX, float screenDivY, byte renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, renderLayer, entityFaction);
		this.enemy = enemy;
	}
	
	public boolean hasShield()
	{
		return (shield == null) ? false : true;
	}
}
