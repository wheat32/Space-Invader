package entities.projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.Sprite;
import entities.players.SpaceShip;
import gameModes.GameMode;
import miscEntities.Wall;
import system.Audio;
import system.Audio.Sfxs;
import system.Options;
import system.Time;
import utils.ConstantValues;
import utils.ObjectCollection;

public class SpaceMine extends Projectile implements ConstantValues
{
	private BufferedImage image;
	private boolean blowingUp = false;
	private float alpha = 1f;

	public SpaceMine(GameMode gameMode, Entity target, float screenDivX, float screenDivY)
	{
		super(gameMode, target, screenDivX, screenDivY);
		super.setHealth(300);
		
		sprite = new Sprite(SPACEMINESHEET, SPACE_MINE_COUNT);
		active = 4;
	}

	@Override
	public void move()
	{
		if(blowingUp == false)
		{
			px = rx/Options.SCREEN_WIDTH;
			py = ry/Options.SCREEN_HEIGHT + 0.0001f * Time.deltaTime();
			ry = Options.SCREEN_HEIGHT*py;
		}
		
		if(isDead() == true && blowingUp == false)
		{
			blowUp();
			gameMode.score += Math.round(60.0*((gameMode.level/4.0)));
		}
		
		if(blowingUp == true)
		{
			growExplosion(Time.deltaTime());
		}
		
		checkProximity();
	}

	/*@Override//TODO figure out what was going on in here
	public void draw(Graphics2D gfx, int tm)
	{
		if(blowingUp == false)
		{
			sprite.drawImage(gfx, (int)rx, (int)ry, (int)rx+dimension.width, (int)ry+dimension.height, tm, active);
		}
		else
		{
			gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		    gfx.drawImage(image, (int)rx, (int) ry, this.dimension.width, this.dimension.height, null);
		    gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		    sprite.addToCurr(tm, 50);
		}
	}*/
	
	@Override
	public int calculateScore()
	{
		//TODO
		return 0;
	}

	@Override
	public boolean inCollision(Entity e)
	{
		if(e instanceof LaserCannon && blowingUp == false)
		{
			LaserCannon m = (LaserCannon) e;
			
			if(this.getBounds().intersects(m.getBounds()) == true)
			{
				ObjectCollection.getEntityManagement().removeEntity(m);
				currHealth -= 100;
				Audio.playSound(Sfxs.Hit);
				return true;
			}
		}
		else if(e instanceof Wall && blowingUp == false)
		{
			Wall w = (Wall) e;
			
			if(this.getBounds().intersects(w.getBounds()) == false)
			{
				ObjectCollection.getEntityManagement().removeEntity(this);
				return true;
			}
		}
		else if(e instanceof SpaceShip)
		{
			SpaceShip ss = (SpaceShip) e;
			
			if(this.getBounds().intersects(ss.getBounds()) == true)
			{
				if(blowingUp == false)
				{
					blowUp();
				}
				else
				{
					if(ss.getRx() - this.getRx() >= 0 && ss.getRy() - this.ry >= 0)//top left point on ship
					{
						if(image.getRGB((int) (ss.getRx() - this.rx), (int) (ss.getRy() - this.ry)) != 0) 
						{
							ObjectCollection.getEntityManagement().removeEntity(this);
							ObjectCollection.getEntityManagement().removeEntity(ss);
							return true;
						}
					}
					else if(ss.getRx() + ss.getDimension().width - this.rx >= 0 && ss.getRy() - this.ry >= 0)//top right point on ship
					{
						if(image.getRGB((int) (ss.getRx() + ss.getDimension().width - this.rx), (int) (ss.getRy() - this.ry)) != 0) 
						{
							ObjectCollection.getEntityManagement().removeEntity(this);
							ObjectCollection.getEntityManagement().removeEntity(ss);
							return true;
						}
					}
					else if(ss.getRx() - this.rx >= 0 && ss.getRy() + ss.getDimension().height - this.ry >= 0)//bottom left point on ship
					{
						if(image.getRGB((int) (ss.getRx() - this.rx), (int) (ss.getRy() - this.ry)) != 0) 
						{
							ObjectCollection.getEntityManagement().removeEntity(this);
							ObjectCollection.getEntityManagement().removeEntity(ss);
							return true;
						}
					}
					else if(ss.getRx() + ss.getDimension().width - this.rx >= 0 && ss.getRy() + ss.getDimension().height - this.ry >= 0)//bottom right point on ship
					{
						if(image.getRGB((int) (ss.getRx() + ss.getDimension().width - this.rx), (int) (ss.getRy() - this.ry)) != 0) 
						{
							ObjectCollection.getEntityManagement().removeEntity(this);
							ObjectCollection.getEntityManagement().removeEntity(ss);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private void blowUp()
	{
		blowingUp = true;
		currHealth = 0;
		
		sprite = new Sprite(SHOCKWAVESHEET, SHOCKWAVE_SPRITE_COUNT);
		image = (BufferedImage) sprite.getCurrentImage();
		active = 0;
		
		Audio.playSound(Sfxs.SeismicBoom);
	}
	
	private void growExplosion(int tm)
	{
		float midX = rx + this.dimension.width/2;
		float midY = ry + this.dimension.height/2;
		
		if(screenDivX < 0.42f)
		{
			screenDivX += tm * 0.0008f;//Rate at which the explosion will grow
			screenDivY = screenDivX;
		}
		else
		{
			alpha -= tm * 0.004f;
			
			if(alpha <= 0)
			{
				ObjectCollection.getEntityManagement().removeEntity(this);
			}
			
			return;
		}
		
		dimension.width = (int) (Options.SCREEN_WIDTH*screenDivX);
		dimension.height = (int) (Options.SCREEN_HEIGHT*screenDivY);
		rx -= rx + this.dimension.width/2 - midX;
		ry -= ry + this.dimension.height/2 - midY;
		
		image = sprite.getCurrentImage();
		
		//System.out.println("Shield takes up " + (this.dimension.width*1.0/Stats.SCREEN_WIDTH)*100 + "% of the screen's width and " + (this.dimension.height*1.0/Stats.SCREEN_HEIGHT)*100 + "% of the screen's height");
		//System.out.println("Original res.: " + image.getWidth(null) + "x" + image.getHeight(null));
		BufferedImage newImage = new BufferedImage(this.dimension.width, this.dimension.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImage.createGraphics();
		g.drawImage(image, 0, 0, this.dimension.width, this.dimension.height, null);
		g.dispose();
		image = newImage;
		//System.out.println("Updated res.: " + image.getWidth(null) + "x" + image.getHeight(null));
	}
	
	private void checkProximity()
	{
		if(blowingUp == true)
		{
			return;
		}
		
		float shipMidX = target.getPx() + (target.getDimension().width*1.0f/Options.SCREEN_WIDTH)/2;
		float shipMidY = target.getPy() + (target.getDimension().height*1.0f/Options.SCREEN_HEIGHT)/2;
		float thisMidX = this.px + (this.getDimension().width*1.0f/Options.SCREEN_WIDTH)/2;
		float thisMidY = this.py + (this.getDimension().height*1.0f/Options.SCREEN_HEIGHT)/2;
		
		if(Math.abs(Math.abs(thisMidX - shipMidX) + Math.abs(thisMidY - shipMidY)) <= 0.28f)
		{
			//System.out.println("tx - sx = " + Math.abs(thisMidX - shipMidX) + " | ty - sy = " + Math.abs(thisMidY - shipMidY));
			blowUp();
		}
		else if(Math.abs((thisMidX - shipMidX) - (thisMidY - shipMidY)) <= 0.46f)
		{
			active = 5;
		}
		else if(active != 4)
		{
			active = 4;
		}
		
		//System.out.println(Math.abs((thisMidX - shipMidX) - (thisMidY - shipMidY)));//TODO fix bug where locations are wrong
	}
	
	public void killSelf()
	{
		ObjectCollection.getEntityManagement().removeEntity(this);
	}
	
	public boolean isBlowingUp()
	{
		return blowingUp;
	}
}
