package io;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import system.Options;
import system.Time;
import updates.GraphicsListener;
import utils.ObjectCollection;

public class DebugPrints implements GraphicsListener
{	
	public short majorRelease = 0;
	public String minorRelease = "pre_3";
	public short releaseRevisions = 0;
	
	public boolean displayFPS = true;
	private short second = 0;
	private short currFPS = 0;
	private short fps = 0;
	
	public DebugPrints()
	{
		ObjectCollection.getRenderer().addGraphicsListener(this);
	}
	
	private void printGraphics(Graphics2D gfx)
	{
		if(ObjectCollection.getMainMenu().inMainMenu() == true)
		{
			gfx.setFont(FontManagement.arialNarrow_small);
			gfx.setColor(Color.LIGHT_GRAY);
			gfx.drawString(("Ver: " + majorRelease + "." + minorRelease + "." + releaseRevisions), 4, Options.SCREEN_HEIGHT-6);
		}
		
		if(displayFPS == true)
		{
			if(second/1000 >= 1)
			{
				second = 0;
				currFPS = fps;
				fps = 0;
				System.out.println("FPS: " + currFPS);
			}
			else
			{
				second += Time.deltaTime();
				fps++;
			}
		}
	}

	@Override
	public void graphicsCall(Graphics2D gfx, boolean resized)
	{
		Font oldFont = gfx.getFont();
		printGraphics(gfx);
		gfx.setFont(oldFont);
	}
}
