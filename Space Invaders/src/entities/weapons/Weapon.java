package entities.weapons;

import entities.Entity;
import system.Audio;
import system.Audio.Sfxs;
import system.Time;
import updates.UpdateListener;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;

public abstract class Weapon implements UpdateListener
{
	public final Entity source;
	//Customizables
	private float maxAmmo;
	private float currAmmo;
	private float currAmmoF;
	private float rechargeSpeed = 1.0f;
	private float ammoPerShot = 1.0f;
	/***
	 * In milliseconds
	 */
	private short firingInterval = 250;
	private boolean integerAmmo = false;
	private boolean overheats = false;
	private float overheatedReloadMod = 0.8f;
	private float cooldownThreshold = 0.4f;
	private Sfxs shotSound;
	private Sfxs overheatSound;
	
	private boolean reloading = false;
	private long canFireAgainAt = 0;
	
	protected Weapon(Entity source, float maxAmmo, float rechargeSpeed, float ammoPerShot, short firingInterval, Sfxs shotSound, Sfxs overheatSound)
	{
		this.source = source;
		this.maxAmmo = maxAmmo;
		this.currAmmo = maxAmmo;
		this.currAmmoF = maxAmmo;
		this.rechargeSpeed = rechargeSpeed;
		this.ammoPerShot = ammoPerShot;
		this.firingInterval = firingInterval;
		this.shotSound = shotSound;
		this.overheatSound = overheatSound;
		ObjectCollection.getMainLoop().addUpdateListener(this);
	}
	
	/***
	 * 
	 * @param maxAmmo
	 * @param rechargeSpeed
	 * @param ammoPerShot
	 * @param integerAmmo
	 * @param overheats
	 * @param overheatedReloadMod
	 * @param cooldownThreshold
	 */
	protected Weapon(Entity source, float maxAmmo, float rechargeSpeed, float ammoPerShot, short firingInterval, Sfxs shotSound, Sfxs overheatSound,
			boolean integerAmmo, boolean overheats, float overheatedReloadMod, float cooldownThreshold)
	{
		this(source, maxAmmo, rechargeSpeed, ammoPerShot, firingInterval, shotSound, overheatSound);
		this.integerAmmo = integerAmmo;
		this.overheats = overheats;
		this.overheatedReloadMod = overheatedReloadMod;
		this.cooldownThreshold = cooldownThreshold;
	}
	
	public short getFiringInterval()
	{
		return firingInterval;
	}

	public void setFiringInterval(short firingInterval)
	{
		this.firingInterval = firingInterval;
	}

	public Sfxs getShotSound()
	{
		return shotSound;
	}

	public void setShotSound(Sfxs shotSound)
	{
		this.shotSound = shotSound;
	}

	public void fire()
	{
		if(reloading == false && System.currentTimeMillis() >= canFireAgainAt)
		{
			weaponFire();
			Audio.playSound(shotSound);
			currAmmo -= ammoPerShot;
			currAmmoF -= ammoPerShot;
			
			if(currAmmo <= 0)
			{
				currAmmo = 0;
				currAmmoF = 0;
				reloading = (overheats == true) ? true : false;
			}
			
			canFireAgainAt = System.currentTimeMillis() + firingInterval;
		}
		else if(reloading == true && System.currentTimeMillis() >= canFireAgainAt)
		{
			Audio.playSound(overheatSound);
			canFireAgainAt = System.currentTimeMillis() + firingInterval;
		}
	}
	
	protected abstract void weaponFire();
	
	private void reload()
	{
		if(currAmmoF < maxAmmo)
		{
			currAmmoF += (rechargeSpeed * Time.deltaTime()) * 0.0026f * ((reloading == true) ? overheatedReloadMod : 1.0f);
			currAmmo = ((integerAmmo == true) ? (int) currAmmoF : currAmmoF);
		}
		
		if(currAmmoF > maxAmmo)
		{
			currAmmoF = maxAmmo;
			currAmmo = maxAmmo;
		}
		
		if(reloading == true && currAmmo >= maxAmmo*cooldownThreshold)
		{
			reloading = false;
			canFireAgainAt = 0;
		}
	}
	
	public void reset()
	{
		currAmmo = maxAmmo;
		currAmmoF = maxAmmo;
		reloading = false;
		canFireAgainAt = 0;
	}
	
	@Override
	public void update()
	{
		if(ObjectCollection.getGameManagement().getGameStatus() == GameStatus.IN_GAME)
		{
			reload();
		}
	}

	public float getMaxAmmo()
	{
		return maxAmmo;
	}

	public void setMaxAmmo(float maxAmmo)
	{
		this.maxAmmo = maxAmmo;
	}

	public float getRechargeSpeed()
	{
		return rechargeSpeed;
	}

	public void setRechargeSpeed(float rechargeSpeed)
	{
		this.rechargeSpeed = rechargeSpeed;
	}

	public float getAmmoPerShot()
	{
		return ammoPerShot;
	}

	public void setAmmoPerShot(float ammoPerShot)
	{
		this.ammoPerShot = ammoPerShot;
	}

	public boolean isIntegerAmmo()
	{
		return integerAmmo;
	}

	public void setIntegerAmmo(boolean integerAmmo)
	{
		this.integerAmmo = integerAmmo;
	}

	public boolean isOverheats()
	{
		return overheats;
	}

	public void setOverheats(boolean overheats)
	{
		this.overheats = overheats;
	}

	public float getCooldownThreshold()
	{
		return cooldownThreshold;
	}

	public void setCooldownThreshold(float cooldownThreshold)
	{
		this.cooldownThreshold = cooldownThreshold;
	}

	public float getCurrAmmo()
	{
		return currAmmo;
	}

	public boolean isReloading()
	{
		return reloading;
	}

	public void setOverheatedReloadMod(float overheatedReloadMod)
	{
		this.overheatedReloadMod = overheatedReloadMod;
	}
}
