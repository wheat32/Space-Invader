package entities.bosses;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import entities.Sprite;
import entities.shields.ShipShield;
import utils.ConstantValues.RenderLayer;

public abstract class Boss extends Entity
{
	protected Entity enemy;//The boss' enemy
	protected ShipShield shield;
	
	public Boss(Entity enemy, float screenDivX, float screenDivY, Sprite sprite, RenderLayer renderLayer, EntityFaction entityFaction)
	{
		this(enemy, screenDivX, screenDivY, sprite, renderLayer.layer, entityFaction);
	}
	
	public Boss(Entity enemy, float screenDivX, float screenDivY, Sprite sprite, byte renderLayer, EntityFaction entityFaction)
	{
		super(screenDivX, screenDivY, sprite, renderLayer, entityFaction);
		this.enemy = enemy;
	}
	
	public boolean hasShield()
	{
		return (shield == null) ? false : true;
	}
}
