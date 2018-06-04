package entities.bosses;

import entities.Entity;
import entities.shields.ShipShield;
import gameModes.GameMode;

public abstract class Boss extends Entity
{
	protected Entity enemy;//The boss' enemy
	protected ShipShield shield;
	
	public Boss(GameMode gameMode, Entity enemy, float screenDivX, float screenDivY)
	{
		super(gameMode, screenDivX, screenDivY);
		this.enemy = enemy;
	}
	
	public boolean hasShield()
	{
		return (shield == null) ? false : true;
	}
}
