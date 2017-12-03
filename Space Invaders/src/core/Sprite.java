package core;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Sprite 
{
	private int spriteCount;
	private int[] src;
	private int curr = 0;
	private short bufferBeforeSpriteChange = 0;
	private Image image;
	private short storedActiveValue = 0;
	
	public Sprite(String resid, int sprites)
	{
		spriteCount = sprites;
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(resid);
			if(url == null)
			{
				System.err.println("Sprite not found.");
			}
			image = ImageIO.read(url);
		}
		catch(IOException e)
		{
			System.err.println("Failed to load sprite.");
		}
		
		src = new int[spriteCount];
		
		for(int i = 0; i < src.length; i++)
		{
			int width = image.getWidth(null)/spriteCount;
			src[i] = i * width;
		}
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public BufferedImage getCurrentImage()
	{
		BufferedImage img = ((BufferedImage) image).getSubimage((image.getWidth(null)/spriteCount)*curr, 0, image.getWidth(null)/spriteCount, image.getHeight(null)); //fill in the corners of the desired crop location here
		BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = copyOfImage.createGraphics();
		g.drawImage(img, 0, 0, null);
		
		return copyOfImage;
	}
	
	public int drawImage(Graphics2D gfx, int rx, int ry, int width, int height, long tm, int active)
	{
		if(storedActiveValue != active)
		{
			storedActiveValue = (short) active;
			bufferBeforeSpriteChange = 0;
		}
		
		int sw = image.getWidth(null)/spriteCount + src[curr];
		gfx.drawImage(image, rx, ry, width, height, src[curr], 0, sw, image.getHeight(null), null);
		
		switch(active)
		{
			case 0:
				break;
			case 1:
			default:
				if(bufferBeforeSpriteChange <= 0)
				{
					curr++;
					curr %= spriteCount;
					bufferBeforeSpriteChange += 100;
				}
				else
				{
					bufferBeforeSpriteChange -= tm;
				}
				break;
			case 2:
				if(bufferBeforeSpriteChange <= 0)
				{
					curr %= 2;
					curr++;
					bufferBeforeSpriteChange += 740;
				}
				else
				{
					bufferBeforeSpriteChange -= tm;
				}
				break;
			case 3:
				curr = 0;
				break;
			case 4:
				if(bufferBeforeSpriteChange <= 0)
				{
					curr++;
					curr %= spriteCount;
					bufferBeforeSpriteChange += 900;//0.9 seconds
				}
				else
				{
					bufferBeforeSpriteChange -= tm;
				}
				break;
			case 5:
				if(bufferBeforeSpriteChange <= 0)
				{
					curr++;
					curr %= spriteCount;
					bufferBeforeSpriteChange += 120;
				}
				else
				{
					bufferBeforeSpriteChange -= tm;
				}
				break;
			case 6:
				if(bufferBeforeSpriteChange <= 0)
				{
					curr++;

					if(curr == spriteCount)
					{
						return 1;
					}
					
					bufferBeforeSpriteChange += 100;
				}
				else
				{
					bufferBeforeSpriteChange -= tm;
				}
				break;
		}
		
		return 0;
	}
	
	public void addToCurr(int tm, int speed)
	{
		if(bufferBeforeSpriteChange <= 0)
		{
			curr++;
			curr %= spriteCount;
			bufferBeforeSpriteChange += speed;
		}
		else
		{
			bufferBeforeSpriteChange -= tm;
		}
	}
}