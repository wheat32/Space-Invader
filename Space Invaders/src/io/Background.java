package io;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import utils.ConstantValues;
import utils.ObjectCollection;
import utils.Stats;

public class Background extends JFrame implements ConstantValues
{
	private static final long serialVersionUID = -4945973544988537397L;

	private Image bg1;
	private Image bg2;
	private float ry1 = 0;
	private float ry2 = 0;
	
	public Background()
	{	
		try
		{
			URL url = this.getClass().getClassLoader().getResource(SPACEBG1);
			if(url == null)
			{
				System.err.println("Image not found.");
			}
			bg1 = ImageIO.read(url);
			bg2 = bg1;
		}
		catch(IOException e)
		{
			System.err.println("Failed to load sprite.");
		}
	}
	
	public void displayBackground(Graphics2D gfx, long tm)
	{	
		if(ry1 >= Stats.SCREEN_HEIGHT)
		{
			ry1 -= Stats.SCREEN_HEIGHT;
		}
		else
		{
			if(ObjectCollection.getGameManagement().getEndCode() == 1)
			{
				ry1 += 0.09*tm;
			}
			else
			{
				ry1 += 0.02*tm;
			}
		}
		
		gfx.drawImage(bg1, 0, (int) ry1, Stats.SCREEN_WIDTH, (int) (Stats.SCREEN_HEIGHT+ry1), 0, 0, bg1.getWidth(null), bg1.getHeight(null), null);
		gfx.drawImage(bg1, 0, (int) ry1-Stats.SCREEN_HEIGHT, Stats.SCREEN_WIDTH, (int) ry1, 0, 0, bg1.getWidth(null), bg1.getHeight(null), null);
		
		if(ry2 <= -Stats.SCREEN_HEIGHT)
		{
			ry2 += Stats.SCREEN_HEIGHT;
		}
		else
		{
			if(ObjectCollection.getGameManagement().getEndCode() == 1)
			{
				ry2 -= 0.35*tm;
			}
			else
			{
				ry2 -= 0.07*tm;
			}
		}
		
		gfx.rotate(Math.toRadians(180), Stats.SCREEN_WIDTH/2, Stats.SCREEN_HEIGHT/2);
		gfx.drawImage(bg2, 0, (int) ry2, Stats.SCREEN_WIDTH, (int) (Stats.SCREEN_HEIGHT+ry2), 0, 0, bg2.getWidth(null), bg2.getHeight(null), null);
		gfx.drawImage(bg2, 0, (int) ry2+Stats.SCREEN_HEIGHT, Stats.SCREEN_WIDTH, (int) ry2+Stats.SCREEN_HEIGHT*2, 0, 0, bg2.getWidth(null), bg2.getHeight(null), null);//TODO
		gfx.rotate(Math.toRadians(-180), Stats.SCREEN_WIDTH/2, Stats.SCREEN_HEIGHT/2);
	}
}
