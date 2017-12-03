package shields;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import bosses.BossAlien;
import core.Entity;
import projectiles.LaserCannon;
import utils.ConstantValues;
import utils.ObjectCollection;
import utils.Stats;

public class ShipShield extends Entity implements ConstantValues
{
	private Entity entity;
	private BufferedImage image;
	private BufferedImage hitImage;
	private float alpha = 0.6f;
	private boolean increasingAlpha = true;
	private short timeStamp = 0;
	private int maxHP = 0;
	private double currHP = 0;
	private float size = 1.0f;
	private boolean recharging = false;
	
	public ShipShield(Entity entity)
	{
		this.entity = entity;
		
		if(entity instanceof BossAlien == true)
		{
			BossAlien ba = (BossAlien) entity;
			Random rand = new Random();
			
			maxHP = (int) (ba.getMaxHP()*0.44f * (rand.nextFloat() * 0.08 + 0.96f));
			currHP = maxHP;
			System.out.println("Shield Max HP: " + maxHP);
		}
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(BLUESHIELD);
			if(url == null)
			{
				System.err.println("Image not found.");
			}
			image = ImageIO.read(url);
		}
		catch(IOException e)
		{
			System.err.println("Failed to load image.");
		}
		
		resize(-1, -1);
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(SHIELDHIT);
			if(url == null)
			{
				System.err.println("Image not found.");
			}
			hitImage = ImageIO.read(url);
		}
		catch(IOException e)
		{
			System.err.println("Failed to load image.");
		}
	}
	
	public class Hits extends Entity
	{
		private short timeStamp = 0;
		private Entity shield;
		private float rotation;
		private float alpha;
		private short fadeMod = 0;
		
		public Hits(Entity shield, float loc)
	    {
			super(shield.getScreenDivX(), shield.getScreenDivY());
			this.shield = shield;
			this.rotation = (float) Math.toRadians (loc*-270);
			this.dimension = shield.getDimension();
			timeStamp = -160;//160 ms is how long until fade
			alpha = 1.0f;
			
			//System.out.println(loc + " to radians: " + rotation);
	    }

		@Override
		public void move(int tm)
		{
			this.rx = shield.getRx();
			this.ry = shield.getRy();
			
			timeStamp += tm;
			
			if(timeStamp + fadeMod > 50)
			{
				alpha -= 0.04f;
				fadeMod += 6;
				
				timeStamp = 0;
			}
		}

		@Override
		public void draw(Graphics2D gfx, int tm)
		{
			if(alpha <= 0 || recharging == true)
			{
				ObjectCollection.getEntityManagement().onEndOfLife(this);
				return;
			}
			
			gfx.rotate(rotation, shield.getRx() + shield.getDimension().width/2, shield.getRy() + shield.getDimension().height/2);
			gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			gfx.drawImage(hitImage, (int)shield.getRx(), (int) shield.getRy(), shield.getDimension().width, shield.getDimension().height, null);
			gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			gfx.rotate(-rotation, shield.getRx() + shield.getDimension().width/2, shield.getRy() + shield.getDimension().height/2);
		}

		@Override
		public boolean inCollision(Entity e)
		{
			//DOES NOT COLLIDE
			return false;
		}

		@Override
		public void resize(int oldScreenWidth, int oldScreenHeight)
		{
			//NOTHING HAPPENS HERE
		}
	}

	@Override
	public void move(int tm)
	{
		rx = entity.getRx() - (this.dimension.width - entity.getDimension().width)/2;
		ry = entity.getRy() - (this.dimension.height - entity.getDimension().height)/2;
		
		timeStamp += tm;
		
		if(timeStamp > 200)
		{
			if(alpha > 0.7f)
			{
				increasingAlpha = false;
			}
			else if(alpha < 0.6f)
			{
				increasingAlpha = true;
			}
			
			if(increasingAlpha == true)
			{
				alpha += 0.012f;
			}
			else
			{
				alpha -= 0.012f;
			}
			//System.out.println(alpha);
			
			timeStamp = 0;
		}
		
		if(currHP <= 0)
		{
			recharging = true;
			currHP = 0;
		}
		else if(currHP >= maxHP)
		{
			recharging = false;
			currHP = maxHP;
		}
		
		if(recharging == true)
		{
			currHP += tm*0.27;
		}
		else if(currHP < maxHP)//health naturally regens
		{
			currHP += tm*0.04;
		}
		
		//System.out.println(currHP);
		
		if(recharging == true)
	    {
	    	if(size > 0.0f)
	    	{
	    		resize(0, 0);
	    		size -= tm * 0.01;
	    	}
	    	else if(size < 0.0f)
	    	{
	    		size = 0.0f;
	    	}
	    }
		else if(size < 1.0f)
		{
			size += tm * 0.01;
			
			if(size > 1.0f)
			{
				size = 1.0f;
			}
			
			resize(0, 0);
		}
	}

	@Override
	public void draw(Graphics2D gfx, int tm)
	{
		if(size > 0.0f)
		{
		    gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		    gfx.drawImage(image, (int)rx, (int) ry, this.dimension.width, this.dimension.height, null);
		    gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}

	@Override
	public boolean inCollision(Entity e)
	{
		if(e instanceof LaserCannon && recharging == false)
		{
			try
			{
				LaserCannon m = (LaserCannon) e;
				
				if(m.getBounds().intersects(this.getBounds()) == true)
				{
					if(m.getRx() - this.rx >= 0 && m.getRy() - this.ry >= 0)//top left point on missile
					{
						if(image.getRGB((int) (m.getRx() - this.rx), (int) (m.getRy() - this.ry)) != 0) 
						{
							float arg = ((m.getRx())/Stats.SCREEN_WIDTH - (this.rx + this.getDimension().width/2)/Stats.SCREEN_WIDTH);
							takeHit(m, arg);
						}
					}
					else if(m.getRx() + m.getDimension().width - this.rx >= 0 && m.getRy() - this.ry >= 0)//top right point on missile
					{
						if(image.getRGB((int) (m.getRx() + m.getDimension().width - this.rx), (int) (m.getRy() - this.ry)) != 0) 
						{
							float arg = ((m.getRx() + m.getDimension().width)/Stats.SCREEN_WIDTH - (this.rx + this.getDimension().width/2)/Stats.SCREEN_WIDTH);
							takeHit(m, arg);
						}
					}
				}
			}
			catch(Exception ex)
			{
				System.err.println(ex);
			}
		}
		return false;
	}
	
	private void takeHit(LaserCannon m, float arg)
	{
		ShipShield.Hits hit = this.new Hits(this, arg);
		ObjectCollection.getEntityManagement().addEntity(hit);
		
		ObjectCollection.getEntityManagement().onEndOfLife(m);
		currHP -= 100;
		Stats.increaseHits();
		ObjectCollection.getAudio().playSound(SfxNames.SHIELDRICOCHET);
	}
	
	@Override
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		if(size <= 0)
		{
			return;
		}
		
		int hypotinuse = (int) Math.sqrt(Math.pow(entity.getDimension().width, 2) + Math.pow(entity.getDimension().height, 2));
		this.dimension.width = (int) ((hypotinuse - entity.getDimension().width + entity.getDimension().width)*size);
		this.dimension.height = (int) ((hypotinuse - entity.getDimension().height + entity.getDimension().height)*size);
		rx = entity.getRx() - (this.dimension.width - entity.getDimension().width)/2;
		ry = entity.getRy() - (this.dimension.height - entity.getDimension().height)/2;
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(BLUESHIELD);
			if(url == null)
			{
				System.err.println("Image not found.");
			}
			image = ImageIO.read(url);
		}
		catch(IOException e)
		{
			System.err.println("Failed to load image.");
		}
		
		try
		{
			//System.out.println("Shield takes up " + (this.dimension.width*1.0/Stats.SCREEN_WIDTH)*100 + "% of the screen's width and " + (this.dimension.height*1.0/Stats.SCREEN_HEIGHT)*100 + "% of the screen's height");
			//System.out.println("Original res.: " + image.getWidth(null) + "x" + image.getHeight(null));
			BufferedImage newImage = new BufferedImage(this.dimension.width, this.dimension.height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = newImage.createGraphics();
			g.drawImage(image, 0, 0, this.dimension.width, this.dimension.height, null);
			g.dispose();
			image = newImage;
			//System.out.println("Updated res.: " + image.getWidth(null) + "x" + image.getHeight(null));
			//System.out.println("Shield dim. is " + this.dimension.width + "x" + this.dimension.height + " | size: " + size);
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
	}

	public int getMaxHP()
	{
		return maxHP;
	}
	
	public double getCurrentHP()
	{
		return currHP;
	}
	
	public boolean isRecharging()
	{
		return recharging;
	}
}
