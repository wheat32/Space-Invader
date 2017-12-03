package bosses;

import core.Entity;
import shields.ShipShield;

public abstract class Boss extends Entity
{
	protected Entity enemy;//The boss' enemy
	protected ShipShield shield;
	
	protected boolean isDead = false;
	protected long timeBonusLength;
	protected int maxHealth;
	protected int health;
	
	public Boss(Entity enemy, float screenDivX, float screenDivY)
	{
		super(screenDivX, screenDivY);
		this.enemy = enemy;
	}
	
	public abstract int getMaxHP();
	public abstract int getCurrentHP();
	
	public boolean hasShield()
	{
		return (shield == null) ? false : true;
	}
}
