package io;

import java.awt.AlphaComposite;
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

import gameModes.GameMode;
import system.Options;
import updates.GraphicsListener;
import utils.ConstantValues;
import utils.ObjectCollection;

public class InGamePrints extends JFrame implements ConstantValues, GraphicsListener
{
	private static final long serialVersionUID = 5051460195790733156L;
	
	private Image spaceShip_IMG;
	private Image dialogueBox_IMG;
	
	private GameMode gameMode;
	
	public InGamePrints(GameMode gameMode)
	{
		this.gameMode = gameMode;
		ObjectCollection.getMainLoop().addGraphicsListener(this, RenderLayer.GUI1);
		
		try
		{
			URL url = this.getClass().getClassLoader().getResource(SHIPSPRITESHEET1);
			if(url == null)
			{
				throw new RuntimeException("InGamePrints: Spaceship image not found.");
			}
			spaceShip_IMG = ImageIO.read(url);
			
			url = this.getClass().getClassLoader().getResource(DIALOGUE_BOX);
			if(url == null)
			{
				throw new RuntimeException("InGamePrints: Dialogue box image not found.");
			}
			dialogueBox_IMG = ImageIO.read(url);
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
		
		gfx.setFont(FontManagement.font);
		gfx.setColor(Color.WHITE);
		gfx.drawString("Score: " + gameMode.score, 4, (int) FontManagement.font.getStringBounds("Score: ", gfx.getFontRenderContext()).getHeight());
		
		gfx.drawString("Time: " + gameMode.getRoundTimeFormatted(), 
				(int) (Options.SCREEN_WIDTH-FontManagement.font.getStringBounds("Time: " + gameMode.getRoundTimeFormatted(), gfx.getFontRenderContext()).getWidth()-4),
				(int) FontManagement.font.getStringBounds("Time: ", gfx.getFontRenderContext()).getHeight());
		
		/*gfx.drawString("Aliens killed: " + (gameMode.numberInPack-objColl.getAlienPack().getAliensInPack()) + "/" + gameMode.numberInPack, 4, 
				Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Level: ", gfx.getFontRenderContext()).getHeight() - 4);
		*///TODO
		gfx.drawString("Level: " + gameMode.level, 4, Options.SCREEN_HEIGHT-4);
		
		if(gameMode.lives <= 5)
		{
			gfx.drawString("Lives: ", Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getWidth()
					- (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() * (gameMode.lives * 0.666f), 
					Options.SCREEN_HEIGHT-8);
			
			for(int i = 0; i < gameMode.lives; i++)
			{
				gfx.drawImage(spaceShip_IMG,  Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() //w1
						- ((int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4,//w1
						Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() - 6,//h1
						(Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()) //w2
						+ (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() //w2
						- ((int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4, //w2
						Options.SCREEN_HEIGHT-4, //h2
						0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);
			}
		}
		else
		{
			gfx.drawString("Lives: " + gameMode.lives + "x", Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: " + gameMode.lives + "x", gfx.getFontRenderContext()).getWidth() 
					- (int) FontManagement.font.getStringBounds("Lives: x", gfx.getFontRenderContext()).getHeight() - 8, Options.SCREEN_HEIGHT-8);
			
			gfx.drawImage(spaceShip_IMG, Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() - 6, //w1
					Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() - 6, //h1
					Options.SCREEN_WIDTH-4, //w2
					Options.SCREEN_HEIGHT-4, //h2
					0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

		}
		 
		Shape rect = new Rectangle((int) (Options.SCREEN_WIDTH*0.82f + (int) FontManagement.font.getStringBounds("Gun:", gfx.getFontRenderContext()).getWidth()),
				Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() * 2 - 12, 
				(int) (Options.SCREEN_WIDTH*0.18f - 4 - (int) FontManagement.font.getStringBounds("Gun:", gfx.getFontRenderContext()).getWidth()), 
				(int) FontManagement.font.getStringBounds("Gun: ", gfx.getFontRenderContext()).getHeight());
		
		gfx.drawString("Gun:", Options.SCREEN_WIDTH*0.82f, 
				Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() - 16);
		gfx.setColor(Color.BLUE);
		gfx.draw(rect);
		
		if(gameMode.getPlayerShip().getAmmo() <= 3)
		{
			gfx.setColor(Color.RED);
		}
		else if(gameMode.getPlayerShip().getAmmo() <= 6)
		{
			gfx.setColor(Color.YELLOW);
		}
		else
		{
			gfx.setColor(Color.GREEN);
		}
		
		if(gameMode.getPlayerShip().getReloading() == true)
		{
			Font smallFont = new Font("Courier New", Font.PLAIN, FontManagement.font.getSize()-6);
			gfx.setFont(smallFont);
			gfx.drawString("Overheated...", Options.SCREEN_WIDTH*0.82f + 6 + (int) FontManagement.font.getStringBounds("Gun:", gfx.getFontRenderContext()).getWidth(), 
					Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() - 16);
			gfx.setFont(FontManagement.font);
		}
		else
		{
			gfx.fillRect(rect.getBounds().x+1, rect.getBounds().y+1,
					(int) ((rect.getBounds().width)/(10.0/gameMode.getPlayerShip().getAmmo())), rect.getBounds().height);
		}
		
		gfx.setFont(oldFont);
		gfx.setColor(foreground);
	}
	
	public void printMessages(Graphics2D gfx)
	{	
		Color foreground = gfx.getColor();
		Font oldFont = gfx.getFont();
		
		gfx.setFont(FontManagement.medFontBold);
		
		/*switch(ObjectCollection.getGameManagement().getGameStatus())
		{
			case 0:	
				gfx.setColor(Color.GREEN);
				gfx.drawString("Endless Mode", Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Endless Mode", gfx.getFontRenderContext()).getWidth()/2,
						Options.SCREEN_HEIGHT/16*4);
				gfx.drawString("Press [SPACE] to play...", Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Press [SPACE] to play...", gfx.getFontRenderContext()).getWidth()/2,
						Options.SCREEN_HEIGHT/16*8);
				gfx.setColor(Color.RED);
				gfx.drawString("Level " + (gameMode.level+1), Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Level " + (gameMode.level+1), gfx.getFontRenderContext()).getWidth()/2,
						Options.SCREEN_HEIGHT - 10);
				gfx.setFont(FontManagement.font);
				break;
			case 1:
				gfx.setColor(Color.GREEN);
				gfx.drawString(("Level " + gameMode.level + " completed. Press [SPACE] to continue..."), 
						Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Level " + gameMode.level + " completed. Press [SPACE] to continue...", gfx.getFontRenderContext()).getWidth()/2,
						(int) FontManagement.medFontBold.getStringBounds("Press [SPACE] to continue...", gfx.getFontRenderContext()).getHeight());
				gfx.setColor(Color.RED);
				gfx.drawString("Next level. Level " + (gameMode.level+1), Options.SCREEN_WIDTH/16*6, Options.SCREEN_HEIGHT/26*25);
				break;
			case 2:
				gfx.setColor(Color.RED);
				gfx.drawString(("You lost on level " + gameMode.level + ". Press [SPACE] to restart."), Options.SCREEN_WIDTH/16*4, Options.SCREEN_HEIGHT/26);
				break;
			case 3:
				gfx.setColor(Color.YELLOW);
				gfx.drawString(("Press [SPACE] to respawn."),
						Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Press [SPACE] to respawn.", gfx.getFontRenderContext()).getWidth()/2, 
						Options.SCREEN_HEIGHT/22*10);

				if(gameMode.lives <= 5)
				{
					gfx.setColor(Color.RED);
					gfx.drawString("Lives remaining: ", 
							Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Lives remaining: ", gfx.getFontRenderContext()).getWidth()/2,
							Options.SCREEN_HEIGHT/22*11);
					
					for(int i = 0; i < gameMode.lives; i++)
					{
						gfx.drawImage(spaceShip_IMG,  
								Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Lives remaining: ", gfx.getFontRenderContext()).getWidth()/2 //w1
								+ (int) FontManagement.medFontBold.getStringBounds("Lives remaining:", gfx.getFontRenderContext()).getWidth() + 4 //w1
								+ ((int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4,//w1
								Options.SCREEN_HEIGHT/22*11 - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3,//h1
								Options.SCREEN_WIDTH/2 - (int) FontManagement.medFontBold.getStringBounds("Lives remaining: ", gfx.getFontRenderContext()).getWidth()/2 + //w2
								(int) FontManagement.medFontBold.getStringBounds("Lives remaining:", gfx.getFontRenderContext()).getWidth() + 4 //w2
								+ (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() //w2
								+ ((int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight() * i / 3 * 2) - 4, //w2
								Options.SCREEN_HEIGHT/22*11 + (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
								0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);
					}
				}
				else
				{
					gfx.setColor(Color.GREEN);
					gfx.drawString("Lives remaining: " + gameMode.lives + "x", Options.SCREEN_WIDTH/16*6, Options.SCREEN_HEIGHT/26*13);
					gfx.drawImage(spaceShip_IMG, Options.SCREEN_WIDTH/16*6 + 230, Options.SCREEN_HEIGHT/26*13-16, Options.SCREEN_WIDTH/16*6 + 256, Options.SCREEN_HEIGHT/26*13+10, 0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

				}
				break;
			default:
				System.err.println("Some text could not be outputed");
		}
		
		if(ObjectCollection.getGameManagement().getGameStatus() == GameStatus.WIN || ObjectCollection.getGameManagement().getGameStatus() == GameStatus.LOSE)
		{
			gfx.setColor(Color.WHITE);
			gfx.setFont(FontManagement.font);
			gfx.drawString(("Score: " + gameMode.score), 4, Options.SCREEN_HEIGHT/26*2);
			gfx.drawString("Round time: " + gameMode.getRoundTimeFormatted(), 4, Options.SCREEN_HEIGHT/26*3);
			gfx.drawString("Total time: " + gameMode.getTotalTimeFormatted(), 4, Options.SCREEN_HEIGHT/26*4);
			
			if(gameMode.lives <= 5 && gameMode.lives > 0)
			{
				gfx.drawString("Lives: ", 4, Options.SCREEN_HEIGHT/26*5);
				
				for(int i = 0; i < gameMode.lives; i++)
				{
					gfx.drawImage(spaceShip_IMG,  
							((int) FontManagement.font.getStringBounds("Lives:", gfx.getFontRenderContext()).getWidth() + 6) + //w1
							((int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight()) * i / 3 * 2, //w1
							Options.SCREEN_HEIGHT/26*5 - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3,//h1
							((int) FontManagement.font.getStringBounds("Lives:", gfx.getFontRenderContext()).getWidth() + 6) + //w2
							((int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight()) * i / 3 * 2//w2
							+ (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight(), //w2
							Options.SCREEN_HEIGHT/26*5 + (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
							0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);
				}
			}
			else if(gameMode.lives == 0)
			{
				gfx.setColor(Color.RED);
				gfx.drawString("Lives: 0", 4, Options.SCREEN_HEIGHT/26*5);
				gfx.setColor(Color.WHITE);
			}
			else
			{
				gfx.drawString("Lives: " + gameMode.lives + "x", 4, Options.SCREEN_HEIGHT/26*5);
				gfx.drawImage(spaceShip_IMG, 
						((int) FontManagement.font.getStringBounds("Lives: " + gameMode.lives + "x", gfx.getFontRenderContext()).getWidth() + 6), //w1
						Options.SCREEN_HEIGHT/26*5 - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3, //h1
						((int) FontManagement.font.getStringBounds("Lives: " + gameMode.lives + "x", gfx.getFontRenderContext()).getWidth() + 6) //w2
						+ (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight(), //w2
						Options.SCREEN_HEIGHT/26*5 + (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
						0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

			}
			
			gfx.drawString(("  ----------------------"), 4, Options.SCREEN_HEIGHT/26*6);
			gfx.drawString(("Shots fired this round: " + gameMode.timesShot), 4, Options.SCREEN_HEIGHT/26*7);
			gfx.drawString(("Shots hit this round: " + gameMode.hits), 4, Options.SCREEN_HEIGHT/26*8);
			gfx.drawString(("Shot accuracy: " + gameMode.getAccuracy(false)), 4, Options.SCREEN_HEIGHT/26*9);
			gfx.drawString(("  ----------------------"), 4, Options.SCREEN_HEIGHT/26*10);
			gfx.drawString(("Total shots taken: " + gameMode.totalTimesShot), 4, Options.SCREEN_HEIGHT/26*11);
			gfx.drawString(("Total shots hit: " + gameMode.totalHits), 4, Options.SCREEN_HEIGHT/26*12);
			gfx.drawString(("Overall accuracy: " + gameMode.getAccuracy(true)), 4, Options.SCREEN_HEIGHT/26*13);
			gfx.drawString(("  ----------------------"), 4, Options.SCREEN_HEIGHT/26*14);
			gfx.drawString(("Total aliens killed: " + gameMode.killedAliens), 4, Options.SCREEN_HEIGHT/26*15);
		}*/
		
		gfx.setFont(oldFont);
		gfx.setColor(foreground);
	}
	
	public void displayPreRound(Graphics2D gfx)
	{
		gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		gfx.drawImage(dialogueBox_IMG, 0, (int) (Options.SCREEN_HEIGHT*0.3), Options.SCREEN_WIDTH, (int) (Options.SCREEN_HEIGHT*0.85), 
				0, 0, dialogueBox_IMG.getWidth(null), dialogueBox_IMG.getHeight(null), null);
		//middleScreenDraw(gfx, msg, xoffset, yoffset);
	}
	
	private void middleScreenDraw(Graphics2D gfx, String msg, int xoffset, int yoffset)
	{
		gfx.drawString(msg, Options.SCREEN_WIDTH/2 - (int) gfx.getFont().getStringBounds(msg, gfx.getFontRenderContext()).getWidth()/2 + xoffset, yoffset);
	}

	@Override
	public void graphicsCall(Graphics2D gfx)
	{
		switch(gameMode.getStatus())
		{
			case PRE_ROUND:
				displayPreRound(gfx);
				break;
			default:
				break;
		}
		gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
}
