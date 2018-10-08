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
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import gameModes.GameMode;
import system.Options;
import updates.GraphicsListener;
import utils.ConstantValues;
import utils.GameManagementUtils.GameStatus;
import utils.ObjectCollection;

public class InGamePrints implements ConstantValues, GraphicsListener
{
	private static DecimalFormat millisecondsFormat = new DecimalFormat("000");
	private static DecimalFormat secondsFormat = new DecimalFormat("00");
	
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
		gfx.drawString("Score: " + gameMode.getScore(), 4, (int) FontManagement.font.getStringBounds("Score: ", gfx.getFontRenderContext()).getHeight());
		
		//TIME
		gfx.drawString("Time: " + getRoundTimeFormatted(), 
				(int) (Options.SCREEN_WIDTH-FontManagement.font.getStringBounds("Time: " + getRoundTimeFormatted(), gfx.getFontRenderContext()).getWidth()-4),
				(int) FontManagement.font.getStringBounds("Time: ", gfx.getFontRenderContext()).getHeight());
		
		/*gfx.drawString("Aliens killed: " + (gameMode.numberInPack-objColl.getAlienPack().getAliensInPack()) + "/" + gameMode.numberInPack, 4, 
				Options.SCREEN_HEIGHT - (int) FontManagement.font.getStringBounds("Level: ", gfx.getFontRenderContext()).getHeight() - 4);
		*///TODO
		gfx.drawString("Level: " + gameMode.getLevel(), 4, Options.SCREEN_HEIGHT-4);
		
		if(gameMode.getLives() <= 5)
		{
			gfx.drawString("Lives: ", Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getWidth()
					- (int) FontManagement.font.getStringBounds("Lives: ", gfx.getFontRenderContext()).getHeight() * (gameMode.getLives() * 0.666f), 
					Options.SCREEN_HEIGHT-8);
			
			for(int i = 0; i < gameMode.getLives(); i++)
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
			gfx.drawString("Lives: " + gameMode.getLives() + "x", Options.SCREEN_WIDTH - (int) FontManagement.font.getStringBounds("Lives: " + gameMode.getLives() 
			+ "x", gfx.getFontRenderContext()).getWidth() - (int) FontManagement.font.getStringBounds("Lives: x", gfx.getFontRenderContext()).getHeight() - 8, 
					Options.SCREEN_HEIGHT-8);
			
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
		
		if(gameMode.getPlayerShip().getWeapon().getCurrAmmo() <= 3)
		{
			gfx.setColor(Color.RED);
		}
		else if(gameMode.getPlayerShip().getWeapon().getCurrAmmo() <= 6)
		{
			gfx.setColor(Color.YELLOW);
		}
		else
		{
			gfx.setColor(Color.GREEN);
		}
		
		if(gameMode.getPlayerShip().getWeapon().isReloading() == true)
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
					(int) ((rect.getBounds().width)/(10.0/gameMode.getPlayerShip().getWeapon().getCurrAmmo())), rect.getBounds().height);
		}
		
		gfx.setFont(oldFont);
		gfx.setColor(foreground);
	}
	
	public void printPostStats(Graphics2D gfx)
	{	
		Color foreground = gfx.getColor();
		Font oldFont = gfx.getFont();
		
		gfx.setFont(FontManagement.medFontBold);

		gfx.setColor(Color.WHITE);
		gfx.setFont(FontManagement.font);
		gfx.drawString(("Score: " + gameMode.getScore()), 4, Options.SCREEN_HEIGHT/26*2);
		gfx.drawString("Round time: " + getRoundTimeFormatted(), 4, Options.SCREEN_HEIGHT/26*3);
		gfx.drawString("Total time: " + getTotalTimeFormatted(), 4, Options.SCREEN_HEIGHT/26*4);
		
		if(gameMode.getLives() <= 5 && gameMode.getLives() > 0)
		{
			gfx.drawString("Lives: ", 4, Options.SCREEN_HEIGHT/26*5);
			
			for(int i = 0; i < gameMode.getLives(); i++)
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
		else if(gameMode.getLives() == 0)
		{
			gfx.setColor(Color.RED);
			gfx.drawString("Lives: 0", 4, Options.SCREEN_HEIGHT/26*5);
			gfx.setColor(Color.WHITE);
		}
		else
		{
			gfx.drawString("Lives: " + gameMode.getLives() + "x", 4, Options.SCREEN_HEIGHT/26*5);
			gfx.drawImage(spaceShip_IMG, 
					((int) FontManagement.font.getStringBounds("Lives: " + gameMode.getLives() + "x", gfx.getFontRenderContext()).getWidth() + 6), //w1
					Options.SCREEN_HEIGHT/26*5 - (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4*3, //h1
					((int) FontManagement.font.getStringBounds("Lives: " + gameMode.getLives() + "x", gfx.getFontRenderContext()).getWidth() + 6) //w2
					+ (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight(), //w2
					Options.SCREEN_HEIGHT/26*5 + (int) FontManagement.font.getStringBounds("Lives: 5x", gfx.getFontRenderContext()).getHeight()/4, //h2
					0, 0, spaceShip_IMG.getWidth(null)/3, spaceShip_IMG.getHeight(null), null);

		}
		
		gfx.drawString(("  ----------------------"), 4, Options.SCREEN_HEIGHT/26*6);
		gfx.drawString(("Shots fired this round: " + gameMode.getTimesShot()), 4, Options.SCREEN_HEIGHT/26*7);
		gfx.drawString(("Shots hit this round: " + gameMode.getHits()), 4, Options.SCREEN_HEIGHT/26*8);
		gfx.drawString(("Shot accuracy: " + gameMode.getAccuracy(false)), 4, Options.SCREEN_HEIGHT/26*9);
		gfx.drawString(("  ----------------------"), 4, Options.SCREEN_HEIGHT/26*10);
		gfx.drawString(("Total shots taken: " + gameMode.getTotalTimesShot()), 4, Options.SCREEN_HEIGHT/26*11);
		gfx.drawString(("Total shots hit: " + gameMode.getTotalHits()), 4, Options.SCREEN_HEIGHT/26*12);
		gfx.drawString(("Overall accuracy: " + gameMode.getAccuracy(true)), 4, Options.SCREEN_HEIGHT/26*13);
		gfx.drawString(("  ----------------------"), 4, Options.SCREEN_HEIGHT/26*14);
		gfx.drawString(("Total aliens killed: " + gameMode.getKills()), 4, Options.SCREEN_HEIGHT/26*15);
		
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
	
	private String getRoundTimeFormatted()//TODO this doesn't belong here
	{
		return TimeUnit.MILLISECONDS.toMinutes(gameMode.getRoundTime()) + ":" + 
				secondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(gameMode.getRoundTime())%60) + ":" + 
				millisecondsFormat.format(TimeUnit.MILLISECONDS.toMillis(gameMode.getRoundTime())%1000);
	}
	
	private String getTotalTimeFormatted()//TODO this doesn't belong here
	{
		return TimeUnit.MILLISECONDS.toMinutes(gameMode.getTotalTime()) + ":" + 
				secondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(gameMode.getTotalTime())%60) + ":" + 
				millisecondsFormat.format(TimeUnit.MILLISECONDS.toMillis(gameMode.getTotalTime())%1000);
	}

	@Override
	public void graphicsCall(Graphics2D gfx)
	{
		switch(gameMode.getStatus())
		{
			case PRE_ROUND:
				displayPreRound(gfx);
				break;
			case IN_GAME:
				printStats(gfx);
				break;
			case POST_ROUND:
				printPostStats(gfx);
			default:
				break;
		}
		gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	@Override
	public void resize(int oldWidth, int oldHeight)
	{
		
	}
}
