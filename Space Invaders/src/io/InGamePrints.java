package io;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import players.SpaceShip;
import utils.ConstantValues;
import utils.GraphicsListener;
import utils.ObjectCollection;
import utils.Stats;

public class InGamePrints extends JFrame implements ConstantValues, GraphicsListener
{
	private static final long serialVersionUID = 5051460195790733156L;
	
	private Image spaceShip_IMG;
	
	private final FontManagement fonts;
	private SpaceShip spaceShip;
	
	public InGamePrints(SpaceShip spaceShip)
	{
		this.spaceShip = spaceShip;
		fonts = ObjectCollection.getFontManagement();
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(SHIPSPRITESHEET1);
			if(url == null)
			{
				System.err.println("Image not found.");
			}
			spaceShip_IMG = ImageIO.read(url);
		}
		catch(IOException e)
		{
			System.err.println("Failed to load sprite.");
		}
	}
	
	public void printStats(Graphics2D gfx)
	{
		Color foreground = gfx.getColor();
		Font oldFont = gfx.getFont();
		
		gfx.setFont(fonts.font);
		gfx.setColor(Color.WHITE);
		gfx.drawString("Score: " + Stats.score, 4, (int) fonts.font.getStringBounds("Score: ", gfx.getFontRenderContext()).getHeight());
		
		gfx.drawString("Time: " + Stats.getRoundTimeFormatted(), 
				(int) (Stats.SCREEN_WIDTH-fonts.font.getStringBounds("Time: " + Stats.getRoundTimeFormatted(), gfx.getFontRenderContext()).getWidth()-4),
				(int) fonts.font.getStringBounds("Time: ", gfx.getFontRenderContext()).getHeight());
		
		/*gfx.drawString("Aliens killed: " + (Stats.numberInPack-objColl.getAlienPack().getAliensInPack()) + "/" + Stats.numberInPack, 4, 
				Stats.SCREEN_HEIGHT - (int) fonts.font.getStringBounds("Level: ", gfx.getFontRenderContext()).getHeight() - 4);
		*///TODO
		gfx.drawString("Level: " + Stats.level, 4, Stats.SCREEN_HEIGHT-4);
		
		if(Stats.lives <= 5)
		{
			gfx.drawString("Lives: ", Stats.SCREEN_WIDTH - (int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getWidth()
					- (int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() * (Stats.lives * 0.666f), 
					Stats.SCREEN_HEIGHT-8);
			
			for(int i = 0; i < Stats.lives; i++)
			{
				gfx.drawImage(spaceShip_IMG,  Stats.SCREEN_WIDTH - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() //w1
						- ((int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4,//w1
						Stats.SCREEN_HEIGHT - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() - 6,//h1
						(Stats.SCREEN_WIDTH - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()) //w2
						+ (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() //w2
						- ((int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4, //w2
						Stats.SCREEN_HEIGHT-4, //h2
						0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);
			}
		}
		else
		{
			gfx.drawString("Lives: " + Stats.lives + "x", Stats.SCREEN_WIDTH - (int) fonts.font.getStringBounds("Lives: " + Stats.lives + "x", gfx.getFontRenderContext()).getWidth() 
					- (int) fonts.font.getStringBounds("Lives: x", gfx.getFontRenderContext()).getHeight() - 8, Stats.SCREEN_HEIGHT-8);
			
			gfx.drawImage(spaceShip_IMG, Stats.SCREEN_WIDTH - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() - 6, //w1
					Stats.SCREEN_HEIGHT - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() - 6, //h1
					Stats.SCREEN_WIDTH-4, //w2
					Stats.SCREEN_HEIGHT-4, //h2
					0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

		}
		 
		Shape rect = new Rectangle((int) (Stats.SCREEN_WIDTH*0.82f + (int) fonts.font.getStringBounds("Gun:", gfx.getFontRenderContext()).getWidth()),
				Stats.SCREEN_HEIGHT - (int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() * 2 - 12, 
				(int) (Stats.SCREEN_WIDTH*0.18f - 4 - (int) fonts.font.getStringBounds("Gun:", gfx.getFontRenderContext()).getWidth()), 
				(int) fonts.font.getStringBounds("Gun: ", gfx.getFontRenderContext()).getHeight());
		
		gfx.drawString("Gun:", Stats.SCREEN_WIDTH*0.82f, 
				Stats.SCREEN_HEIGHT - (int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() - 16);
		gfx.setColor(Color.BLUE);
		gfx.draw(rect);
		
		if(spaceShip.getAmmo() <= 3)
		{
			gfx.setColor(Color.RED);
		}
		else if(spaceShip.getAmmo() <= 6)
		{
			gfx.setColor(Color.YELLOW);
		}
		else
		{
			gfx.setColor(Color.GREEN);
		}
		
		if(spaceShip.getReloading() == true)
		{
			Font smallFont = new Font("Courier New", Font.PLAIN, fonts.font.getSize()-6);
			gfx.setFont(smallFont);
			gfx.drawString("Overheated...", Stats.SCREEN_WIDTH*0.82f + 6 + (int) fonts.font.getStringBounds("Gun:", gfx.getFontRenderContext()).getWidth(), 
					Stats.SCREEN_HEIGHT - (int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() - 16);
			gfx.setFont(fonts.font);
		}
		else
		{
			gfx.fillRect(rect.getBounds().x+1, rect.getBounds().y+1,
					(int) ((rect.getBounds().width)/(10.0/spaceShip.getAmmo())), rect.getBounds().height);
		}
		
		gfx.setFont(oldFont);
		gfx.setColor(foreground);
	}
	
	public void printMessages(Graphics2D gfx)
	{	
		Color foreground = gfx.getColor();
		Font oldFont = gfx.getFont();
		
		gfx.setFont(fonts.medFontBold);
		
		switch(ObjectCollection.getGameManagement().getEndCode())
		{
			case 0:	
				gfx.setColor(Color.GREEN);
				gfx.drawString("Endless Mode", Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Endless Mode", gfx.getFontRenderContext()).getWidth()/2,
						Stats.SCREEN_HEIGHT/16*4);
				gfx.drawString("Press [SPACE] to play...", Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Press [SPACE] to play...", gfx.getFontRenderContext()).getWidth()/2,
						Stats.SCREEN_HEIGHT/16*8);
				gfx.setColor(Color.RED);
				gfx.drawString("Level " + (Stats.level+1), Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Level " + (Stats.level+1), gfx.getFontRenderContext()).getWidth()/2,
						Stats.SCREEN_HEIGHT - 10);
				gfx.setFont(fonts.font);
				break;
			case 1:
				gfx.setColor(Color.GREEN);
				gfx.drawString(("Level " + Stats.level + " completed. Press [SPACE] to continue..."), 
						Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Level " + Stats.level + " completed. Press [SPACE] to continue...", gfx.getFontRenderContext()).getWidth()/2,
						(int) fonts.medFontBold.getStringBounds("Press [SPACE] to continue...", gfx.getFontRenderContext()).getHeight());
				gfx.setColor(Color.RED);
				gfx.drawString("Next level. Level " + (Stats.level+1), Stats.SCREEN_WIDTH/16*6, Stats.SCREEN_HEIGHT/26*25);
				break;
			case 2:
				gfx.setColor(Color.RED);
				gfx.drawString(("You lost on level " + Stats.level + ". Press [SPACE] to restart."), Stats.SCREEN_WIDTH/16*4, Stats.SCREEN_HEIGHT/26);
				break;
			case 3:
				gfx.setColor(Color.YELLOW);
				gfx.drawString(("Press [SPACE] to respawn."),
						Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Press [SPACE] to respawn.", gfx.getFontRenderContext()).getWidth()/2, 
						Stats.SCREEN_HEIGHT/22*10);

				if(Stats.lives <= 5)
				{
					gfx.setColor(Color.RED);
					gfx.drawString("Lives remaining: ", 
							Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Lives remaining: ", gfx.getFontRenderContext()).getWidth()/2,
							Stats.SCREEN_HEIGHT/22*11);
					
					for(int i = 0; i < Stats.lives; i++)
					{
						gfx.drawImage(spaceShip_IMG,  
								Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Lives remaining: ", gfx.getFontRenderContext()).getWidth()/2 //w1
								+ (int) fonts.medFontBold.getStringBounds("Lives remaining:", gfx.getFontRenderContext()).getWidth() + 4 //w1
								+ ((int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4,//w1
								Stats.SCREEN_HEIGHT/22*11 - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3,//h1
								Stats.SCREEN_WIDTH/2 - (int) fonts.medFontBold.getStringBounds("Lives remaining: ", gfx.getFontRenderContext()).getWidth()/2 + //w2
								(int) fonts.medFontBold.getStringBounds("Lives remaining:", gfx.getFontRenderContext()).getWidth() + 4 //w2
								+ (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() //w2
								+ ((int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4, //w2
								Stats.SCREEN_HEIGHT/22*11 + (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
								0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);
					}
				}
				else
				{
					gfx.setColor(Color.GREEN);
					gfx.drawString("Lives remaining: " + Stats.lives + "x", Stats.SCREEN_WIDTH/16*6, Stats.SCREEN_HEIGHT/26*13);
					gfx.drawImage(spaceShip_IMG, Stats.SCREEN_WIDTH/16*6 + 230, Stats.SCREEN_HEIGHT/26*13-16, Stats.SCREEN_WIDTH/16*6 + 256, Stats.SCREEN_HEIGHT/26*13+10, 0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

				}
				break;
			default:
				System.err.println("Some text could not be outputed");
		}
		
		if(ObjectCollection.getGameManagement().getEndCode() == 1 || ObjectCollection.getGameManagement().getEndCode() == 2)
		{
			gfx.setColor(Color.WHITE);
			gfx.setFont(fonts.font);
			gfx.drawString(("Score: " + Stats.score), 4, Stats.SCREEN_HEIGHT/26*2);
			gfx.drawString("Round time: " + Stats.getRoundTimeFormatted(), 4, Stats.SCREEN_HEIGHT/26*3);
			gfx.drawString("Total time: " + Stats.getTotalTimeFormatted(), 4, Stats.SCREEN_HEIGHT/26*4);
			
			if(Stats.lives <= 5 && Stats.lives > 0)
			{
				gfx.drawString("Lives: ", 4, Stats.SCREEN_HEIGHT/26*5);
				
				for(int i = 0; i < Stats.lives; i++)
				{
					gfx.drawImage(spaceShip_IMG,  
							((int) fonts.font.getStringBounds("Lives:", gfx.getFontRenderContext()).getWidth() + 6) + //w1
							((int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight()) * i / 3 * 2, //w1
							Stats.SCREEN_HEIGHT/26*5 - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3,//h1
							((int) fonts.font.getStringBounds("Lives:", gfx.getFontRenderContext()).getWidth() + 6) + //w2
							((int) fonts.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight()) * i / 3 * 2//w2
							+ (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight(), //w2
							Stats.SCREEN_HEIGHT/26*5 + (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
							0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);
				}
			}
			else if(Stats.lives == 0)
			{
				gfx.setColor(Color.RED);
				gfx.drawString("Lives: 0", 4, Stats.SCREEN_HEIGHT/26*5);
				gfx.setColor(Color.WHITE);
			}
			else
			{
				gfx.drawString("Lives: " + Stats.lives + "x", 4, Stats.SCREEN_HEIGHT/26*5);
				gfx.drawImage(spaceShip_IMG, 
						((int) fonts.font.getStringBounds("Lives: " + Stats.lives + "x", gfx.getFontRenderContext()).getWidth() + 6), //w1
						Stats.SCREEN_HEIGHT/26*5 - (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3, //h1
						((int) fonts.font.getStringBounds("Lives: " + Stats.lives + "x", gfx.getFontRenderContext()).getWidth() + 6) //w2
						+ (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight(), //w2
						Stats.SCREEN_HEIGHT/26*5 + (int) fonts.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
						0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

			}
			
			gfx.drawString(("  ----------------------"), 4, Stats.SCREEN_HEIGHT/26*6);
			gfx.drawString(("Shots fired this round: " + Stats.timesShot), 4, Stats.SCREEN_HEIGHT/26*7);
			gfx.drawString(("Shots hit this round: " + Stats.hits), 4, Stats.SCREEN_HEIGHT/26*8);
			gfx.drawString(("Shot accuracy: " + Stats.getAccuracy(false)), 4, Stats.SCREEN_HEIGHT/26*9);
			gfx.drawString(("  ----------------------"), 4, Stats.SCREEN_HEIGHT/26*10);
			gfx.drawString(("Total shots taken: " + Stats.totalTimesShot), 4, Stats.SCREEN_HEIGHT/26*11);
			gfx.drawString(("Total shots hit: " + Stats.totalHits), 4, Stats.SCREEN_HEIGHT/26*12);
			gfx.drawString(("Overall accuracy: " + Stats.getAccuracy(true)), 4, Stats.SCREEN_HEIGHT/26*13);
			gfx.drawString(("  ----------------------"), 4, Stats.SCREEN_HEIGHT/26*14);
			gfx.drawString(("Total aliens killed: " + Stats.killedAliens), 4, Stats.SCREEN_HEIGHT/26*15);
		}
		
		gfx.setFont(oldFont);
		gfx.setColor(foreground);
	}
	
	public void printPregameScreen(Graphics2D gfx)
	{
		//middleScreenDraw(gfx, msg, xoffset, yoffset);
	}
	
	private void middleScreenDraw(Graphics2D gfx, String msg, int xoffset, int yoffset)
	{
		gfx.drawString(msg, Stats.SCREEN_WIDTH/2 - (int) gfx.getFont().getStringBounds(msg, gfx.getFontRenderContext()).getWidth()/2 + xoffset, yoffset);
	}

	@Override
	public void graphicsCall(Graphics2D gfx, int elapsedTime)
	{
		
	}
}
