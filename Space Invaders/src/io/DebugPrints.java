package io;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import system.Options;
import system.Time;
import updates.GraphicsListener;
import utils.ConstantValues;
import utils.ConstantValues.RenderLayer;
import utils.ObjectCollection;

public class DebugPrints implements GraphicsListener
{	
	public boolean displayFPS = true;
	private short second = 0;
	private short currFPS = 0;
	private short fps = 0;
	
	public DebugPrints()
	{
		ObjectCollection.getMainLoop().addGraphicsListener(this, RenderLayer.GUI3);
	}
	
	private void printGraphics(Graphics2D gfx)
	{
		if(ObjectCollection.getMainMenu().inMainMenu() == true)
		{
			gfx.setFont(FontManagement.arialNarrow_small);
			gfx.setColor(Color.LIGHT_GRAY);
			gfx.drawString(("Ver: " + ConstantValues.majorRelease + "." + ConstantValues.minorRelease + "." + ConstantValues.releaseRevisions 
					+ " | " + ConstantValues.buildID), 4, Options.SCREEN_HEIGHT-6);
		}
		
		if(displayFPS == true)
		{
			if(second/1000 >= 1)
			{
				second -= 1000;
				currFPS = fps;
				fps = 0;
				//System.out.println("FPS: " + currFPS);
			}
			else
			{
				second += Time.deltaTime();
				fps++;
			}
			
			gfx.setFont(FontManagement.arialNarrow_small.deriveFont(FontManagement.arialNarrow_small.getSize()*0.7f));
			Color tempColor = gfx.getColor();
			gfx.setColor(Color.GRAY);
			gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			//Draw the background rect
			gfx.fillRect(0, 0, 
					(int) (gfx.getFont().getStringBounds("-FPS: " + currFPS + "-",gfx.getFontRenderContext()).getWidth()),
					(int) (gfx.getFont().getStringBounds("FPS:??", gfx.getFontRenderContext()).getHeight()*1.1f));
			
			gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.92f));
			gfx.setColor(Color.GREEN);
			//Draw the text
			gfx.drawString(" FPS: " + currFPS + " ", 0, (int) (gfx.getFont().getStringBounds("FPS:??", gfx.getFontRenderContext()).getHeight()*0.96f));
			
			gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			gfx.setColor(tempColor);
		}
	}

	@Override
	public void graphicsCall(Graphics2D gfx)
	{
		Font oldFont = gfx.getFont();
		printGraphics(gfx);
		gfx.setFont(oldFont);
	}

	@Override
	public void resize(int oldWidth, int oldHeight)
	{
		return;
	}
}
