package utils;

public interface ConstantValues 
{
	byte UP_KEY = 0x08;
	byte LEFT_KEY = 0x04;
	byte DOWN_KEY = 0x02;
	byte RIGHT_KEY = 0x01;
	byte SPACE_KEY = 0x16;
	
	final short ALIEN_SPRITE_COUNT = 3;
	final short SPACESHIP_SPRITE_COUNT = 3;
	final short MISSILE_SPRITE_COUNT = 3;
	final short EXPLOSION_SPRITE_COUNT = 14;
	final short SPACE_MINE_COUNT = 2;
	final short SHOCKWAVE_SPRITE_COUNT = 2;
	
	//[START] --- AUDIO STRINGS --- \\
	
	//Background Music
	enum TrackNames {BGM1_INTRO, BGM1_LOOP, BGM2_INTRO, BGM2_LOOP, BIGWIN1_INTRO, BIGWIN1_LOOP, BIGWIN2_INTRO, BIGWIN2_LOOP, BOSSBGM1, LOSE};
	
	//Sound Effects
	enum SfxNames {BEEP1, ENEMYSHOOT, FRIENDLYSHOOT, GUNOVERHEAT, HIT, MENUBADSELECT, MENUMOVEMENT, SEISMICBOOM, SHIELDRICOCHET};
	
	//[END] --- AUDIO STRINGS --- \\
	
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
	final String SPACEBG1 = "sprites/spaceBG1.png";
	final String SPACEMINESHEET = "sprites/spaceMineSheet.png";
	//[END] --- SPRITE STRINGS --- \\
}
