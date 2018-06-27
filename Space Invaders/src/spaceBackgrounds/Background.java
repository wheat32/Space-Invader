package spaceBackgrounds;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import system.Logger;
import system.Options;
import system.Time;
import updates.GraphicsListener;
import utils.ConstantValues;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;

public class Background extends JFrame implements ConstantValues, GraphicsListener
{
	private static final long serialVersionUID = -4945973544988537397L;

	private Image bg1;
	private Image bg2;
	private float ry1 = 0;
	private float ry2 = 0;
	
	public Background()
	{	
		URL url = this.getClass().getClassLoader().getResource(SPACEBG1);
		if(url == null)
		{
			throw new NullPointerException("URL for " + SPACEBG1 + " returned null.");
		}
		
		try
		{
			bg1 = ImageIO.read(url);
		}
		catch (IOException e)
		{
			Logger.exception(e);
		}
		
		bg2 = bg1;
		
		ObjectCollection.getMainLoop().addGraphicsListener(this, RenderLayer.BACKGROUND1);
	}
	
	@Override
	public void graphicsCall(Graphics2D gfx)
	{	
		gfx.setColor(Color.BLACK);
		gfx.fillRect(0, 0, Options.SCREEN_WIDTH, Options.SCREEN_HEIGHT);	
		
		int deltaTime = Time.deltaTime();
		
		if(ry1 >= Options.SCREEN_HEIGHT)
		{
			ry1 -= Options.SCREEN_HEIGHT;
		}
		else
		{
			if(ObjectCollection.getGameManagement().getGameStatus() == GameStatus.WIN)
			{
				ry1 += 0.09*deltaTime;
			}
			else
			{
				ry1 += 0.02*deltaTime;
			}
		}
		
		
		gfx.drawImage(bg1, 0, (int) ry1, Options.SCREEN_WIDTH, (int) (Options.SCREEN_HEIGHT+ry1), 0, 0, bg1.getWidth(null), bg1.getHeight(null), null);
		gfx.drawImage(bg1, 0, (int) ry1-Options.SCREEN_HEIGHT, Options.SCREEN_WIDTH, (int) ry1, 0, 0, bg1.getWidth(null), bg1.getHeight(null), null);
		
		if(ry2 <= -Options.SCREEN_HEIGHT)
		{
			ry2 += Options.SCREEN_HEIGHT;
		}
		else
		{
			if(ObjectCollection.getGameManagement().getGameStatus() == GameStatus.WIN)
			{
				ry2 -= 0.35*deltaTime;
			}
			else
			{
				ry2 -= 0.07*deltaTime;
			}
		}
		
		gfx.rotate(Math.toRadians(180), Options.SCREEN_WIDTH/2, Options.SCREEN_HEIGHT/2);
		gfx.drawImage(bg2, 0, (int) ry2, Options.SCREEN_WIDTH, (int) (Options.SCREEN_HEIGHT+ry2), 0, 0, bg2.getWidth(null), bg2.getHeight(null), null);
		gfx.drawImage(bg2, 0, (int) ry2+Options.SCREEN_HEIGHT, Options.SCREEN_WIDTH, (int) ry2+Options.SCREEN_HEIGHT*2, 0, 0, bg2.getWidth(null), bg2.getHeight(null), null);//TODO
		gfx.rotate(Math.toRadians(-180), Options.SCREEN_WIDTH/2, Options.SCREEN_HEIGHT/2);
	}
}
