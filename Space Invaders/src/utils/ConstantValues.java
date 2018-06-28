package utils;

public interface ConstantValues 
{
	public final byte majorRelease = 0;
	public final String minorRelease = "pre_3";
	public final short releaseRevisions = 0;
	public final String buildID = "11-6.28.18";
	
	/**
	 * Note: Particle layers are always before a sprite layer of the same number. Ex: SPRITE1 is before PARTICLE2 but after PARTICLE1.
	 * @author Wheat
	 *
	 */
	public enum RenderLayer
	{
		BACKGROUND1((byte) 1),
		BACKGROUND2((byte) 3),
		BACKGROUND3((byte) 5),
		PARTICLE1((byte) 10),
		SPRITE1((byte) 12),
		PARTICLE2((byte) 16),
		SPRITE2((byte) 18),
		PARTICLE3((byte) 22),
		SPRITE3((byte) 24),
		PARTICLE4((byte) 28),
		GUI1((byte) 40),
		GUI2((byte) 45),
		GUI3((byte) 50);
		
		public final byte layer;
		
		private RenderLayer(byte layerNum)
		{
			layer = layerNum;
		}
	}
	
	final byte ALIEN_SPRITE_COUNT = 3;
	final byte BLUESHIELD_SPRITE_COUNT = 1;
	final byte SPACESHIP_SPRITE_COUNT = 3;
	final byte MISSILE_SPRITE_COUNT = 3;
	final byte EXPLOSION_SPRITE_COUNT = 14;
	final byte SPACE_MINE_COUNT = 2;
	final byte SHOCKWAVE_SPRITE_COUNT = 2;
	
	//[START] --- SPRITE STRINGS --- \\
	final String ALIENSPRITESHEET1 = "sprites/alienSpriteSheet1.png";
	final String BLUESHIELD = "sprites/blueShield.png";
	final String BOSSALIENSPRITESHEET1 = "sprites/bossAlienSpriteSheet1.png";
	final String ENEMYPROJECTILESPRITESHEET1 = "sprites/alienSpriteSheet1.png";
	final String EXPLOSIONSPRITESHEET = "sprites/explosionSpriteSheet.png";
	final String MOON = "sprites/moon.png";
	final String PLANET1 = "sprites/planet1.png";
	final String PLANET2 = "sprites/planet2.png";
	final String PLANET3 = "sprites/planet3.png";
	final String PROJECTILESPRITESHEET1 = "sprites/projectileSpriteSheet1.png";
	final String SHIELDHIT = "sprites/shieldHit.png";
	final String SHIPSPRITESHEET1 = "sprites/shipSpriteSheet1.png";
	final String SHOCKWAVESHEET = "sprites/shockwaveSheet.png";
	final String SPACEMINESHEET = "sprites/spaceMineSheet.png";
	//[END] --- SPRITE STRINGS --- \\
	
	//[START] --- IMAGES --- \\
	final String SPACEBG1 = "images/spaceBG1.png";
	final String DIALOGUE_BOX = "images/dialogue_box.png";
	//[END] --- IMAGES --- \\
}
