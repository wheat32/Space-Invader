package entities.weapons;

import entities.Entity;
import entities.projectiles.Laser;
import system.Audio.Sfxs;
import utils.EntityManagement;
import utils.ObjectCollection;

public class LaserCannon extends Weapon
{
	public LaserCannon(Entity source)
	{
		super(source, 10f, //Max ammo
				1f, //Base recharge speed
				2.5f, //Max recharge speed
				1.2f, //Parabolic modifier
				1f, //Ammo per shot
				(short) 250, //Firing interval
				Sfxs.FriendlyShoot1, Sfxs.GunOverheat, true, true,
				0.6f, //Cooldown rate (this times the recharge speed when reloading)
				0.4f); //Cooldown threshold (when the currAmmo is >= than this, reloading is finished)
	}

	@Override
	protected void weaponFire()
	{
		Laser laser = new Laser(source, 0.0166f, 0.0333f);
		laser.setPosition(source.getRx() + source.getDimension().width/2 - laser.getDimension().width/2,
				source.getRy() - source.getDimension().height/2);
		EntityManagement.addEntity(laser);
		source.setVelocity(source.getVx()*0.4f, source.getVy());
		ObjectCollection.getGameManagement().getGameMode().increaseShots();
	}
}
