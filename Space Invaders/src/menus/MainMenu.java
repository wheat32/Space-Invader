package menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashMap;

import input.KeyInputManagement;
import io.FontManagement;
import strings.MainMenuStrs;
import system.Audio;
import system.Audio.Sfxs;
import system.Audio.Tracks;
import updates.UpdateListener;
import updates.GraphicsListener;
import system.Options;
import utils.ConstantValues;
import utils.MainMenuMsg;
import utils.ObjectCollection;

public class MainMenu implements ConstantValues, GraphicsListener, UpdateListener, MainMenuStrs
{
	//Where the cursor is at
	private int selection = 1;
	private boolean inMainMenu = false;
	//Timeout values to curb rapid selections caused by holding the button down
	private long timeouts[] = {0, 0, 0, 0, 0, 0};
	private MainMenuMsg changeMsg;
	private Color[] arrowColor = {Color.YELLOW, Color.YELLOW};
	
	private byte onDifficulty = 2;//0 = Very Easy; 1 = Easy; 2 = Normal (default); 3 = Hard; 4 = Nightmare; 5 = Custom
	
	//Contains everything that goes in the menu
	private HashMap<Integer, MainMenuMsg> messages = new HashMap<Integer, MainMenuMsg>();
	
	private void setUpMsgs()
	{
		messages.clear();//Clean up the HashMap first
		
		//Main Menu
		messages.put(0, new MainMenuMsg(Color.GREEN, TITLE, FontManagement.agencyFBPlain_large, 0, Options.SCREEN_HEIGHT/26*4));
		messages.put(1, new MainMenuMsg(Color.GRAY, STORY_MODE, 0, Options.SCREEN_HEIGHT/26*14));
		messages.put(2, new MainMenuMsg(Color.YELLOW, ARCADE_MODE, 0, Options.SCREEN_HEIGHT/26*16));
		messages.put(3, new MainMenuMsg(Color.GRAY, OPTIONS, 0, Options.SCREEN_HEIGHT/26*18));
		messages.put(4, new MainMenuMsg(new Color(59, 187, 255), EXIT_TO_DESKTOP, 0, Options.SCREEN_HEIGHT/26*20));//Color blue
		//Arcade Mode
		messages.put(21, new MainMenuMsg(Color.YELLOW, INFINITY_MODE, 0, Options.SCREEN_HEIGHT/26*14));
		messages.put(22, new MainMenuMsg(Color.GRAY, ENDLESS_WAVE_MODE, 0, Options.SCREEN_HEIGHT/26*16));
		messages.put(23, new MainMenuMsg(Color.GRAY, BOSS_RUSH, 0, Options.SCREEN_HEIGHT/26*18));
		messages.put(24, new MainMenuMsg(new Color(59, 187, 255), RETURN_TO_MAIN_MENU, 0, Options.SCREEN_HEIGHT/26*20));//Blue
		//Infinity Mode
		messages.put(201, new MainMenuMsg(Color.GREEN, START_GAME, 0, Options.SCREEN_HEIGHT/26*11));
		messages.put(202, new MainMenuMsg(new Color[]{Color.YELLOW, Color.WHITE}, 
				new String[]{DIFFICULTY, DIFFICULTIES[onDifficulty]}, 0, Options.SCREEN_HEIGHT/26*13));
		messages.put(203, new MainMenuMsg(new Color[]{((onDifficulty == 5) ? Color.YELLOW : Color.GRAY), ((onDifficulty == 5) ? Color.WHITE : Color.GRAY)},
				new String[]{ROUNDS_BETWEEN_BOSSES, ObjectCollection.getGameManagement().getRoundsBetweenBoss()+""}, 0, Options.SCREEN_HEIGHT/26*15));
		messages.put(204, new MainMenuMsg(new Color[]{((onDifficulty == 5) ? Color.YELLOW : Color.GRAY), ((onDifficulty == 5) ? Color.WHITE : Color.GRAY)},
				new String[]{STARTING_LIVES, ObjectCollection.getGameManagement().getStartingLives()+""}, 0, Options.SCREEN_HEIGHT/26*17));
		messages.put(205, new MainMenuMsg(new Color[]{((onDifficulty == 5) ? Color.YELLOW : Color.GRAY), ((onDifficulty == 5) ? Color.WHITE : Color.GRAY)},
				new String[]{ALIEN_PACK_SPEED_MOD, ObjectCollection.getGameManagement().getAPackSpdMod()+""}, 0, Options.SCREEN_HEIGHT/26*19));
		messages.put(206, new MainMenuMsg(new Color[]{((onDifficulty == 5) ? Color.YELLOW : Color.GRAY), ((onDifficulty == 5) ? Color.WHITE : Color.GRAY)},
				new String[]{WEAPON_COOLDOWN_MOD, ObjectCollection.getGameManagement().getWpnCoolMod()+""}, 0, Options.SCREEN_HEIGHT/26*21));
		messages.put(207, new MainMenuMsg(new Color(59, 187, 255), RETURN_TO_MAIN_MENU, 0, Options.SCREEN_HEIGHT/26*23));
	}
	
	private void displayMainMenu(Graphics2D gfx)
	{
		changeMsg = null;
		
		middleScreenString(gfx, messages.get(0));
		
		gfx.setFont(FontManagement.medFontBold);
				
		switch(selection)
		{
			//----- START [MAIN MENU] -----
			case 0: //So the options don't flash when the carrot loops around
			case 1://Story Mode
				break;
			case 2://Arcade Mode
				changeMsg = new MainMenuMsg(Color.RED, messages.get(selection).getMessageWhole(), 
						messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 3:
				break;
			case 5: //So the options don't flash when the carrot loops around
			case 4:
				changeMsg = new MainMenuMsg(Color.RED, messages.get(selection).getMessageWhole(), 
						messages.get(selection).getX(), messages.get(selection).getY());
				break;
			//----- END [MAIN MENU] -----
				
			//----- START [ARCADE MENU] -----
			case 20:
			case 21://Infinity Mode
				changeMsg = new MainMenuMsg(Color.RED, messages.get(selection).getMessageWhole(), 
						messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 22:
				break;
			case 23:
				break;
			case 25:
			case 24:
				changeMsg = new MainMenuMsg(Color.RED, messages.get(selection).getMessageWhole(), 
						messages.get(selection).getX(), messages.get(selection).getY());
				break;
			//----- END [ARCADE MENU] -----
				
			//----- START [INFINITY MODE] -----
			case 200:
			case 201://Start Game
				break;
			case 202://Difficulty
				changeMsg = new MainMenuMsg(new Color[]{Color.RED, arrowColor[0], new Color(255, 77, 0), arrowColor[1]}, 
						new String[]{DIFFICULTY, "< ", messages.get(selection).getMessage()[1], " >"}, messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 203://Rounds between bosses
				changeMsg = new MainMenuMsg((onDifficulty == 5) ? new Color[]{Color.RED, arrowColor[0], new Color(255, 77, 0), arrowColor[1]} : new Color[]{Color.GRAY},
						new String[]{ROUNDS_BETWEEN_BOSSES, "< ", messages.get(selection).getMessage()[1], " >"}, messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 204://Starting Lives
				changeMsg = new MainMenuMsg((onDifficulty == 5) ? new Color[]{Color.RED, arrowColor[0], new Color(255, 77, 0), arrowColor[1]} : new Color[]{Color.GRAY},
						new String[]{STARTING_LIVES, "< ", messages.get(selection).getMessage()[1], " >"}, messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 205://Alien Pack Speed Modifier
				changeMsg = new MainMenuMsg((onDifficulty == 5) ? new Color[]{Color.RED, arrowColor[0], new Color(255, 77, 0), arrowColor[1]} : new Color[]{Color.GRAY},
						new String[]{ALIEN_PACK_SPEED_MOD, "< ", messages.get(selection).getMessage()[1], " >"}, messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 206://Weapon cooldown modifier
				changeMsg = new MainMenuMsg((onDifficulty == 5) ? new Color[]{Color.RED, arrowColor[0], new Color(255, 77, 0), arrowColor[1]} : new Color[]{Color.GRAY},
						new String[]{WEAPON_COOLDOWN_MOD, "< ", messages.get(selection).getMessage()[1], " >"}, messages.get(selection).getX(), messages.get(selection).getY());
				break;
			case 208:
			case 207://Return to arcade mode
				changeMsg = new MainMenuMsg(Color.RED, messages.get(selection).getMessageWhole(),
						messages.get(selection).getX(), messages.get(selection).getY());
				break;
			default:
				throw new RuntimeException("MainMenu: Entered the default case in the displayMainMenu() method");
		}
		
		//Where to put the arrow
		if(changeMsg != null)
		{
			drawArrow(gfx, changeMsg.getColor()[0], changeMsg.getMessageWhole(), changeMsg.getY());
		}
		else
		{
			drawArrow(gfx, messages.get(selection).getColor()[0], messages.get(selection).getMessageWhole(), messages.get(selection).getY());
		}
		
		//Loop through all the menu objects to draw
		for(int i = (selection - selection%10); i < ((selection+9)/10)*10; i++)
		{
			//If the iterator is on the current selection and the changeMsg has been set
			if(i == selection && changeMsg != null)
			{
				middleScreenString(gfx, changeMsg);
				continue;
			}
			
			middleScreenString(gfx, messages.get(i));
		}
	}
	
	private void middleScreenString(Graphics2D gfx, MainMenuMsg msg)
	{	
		if(msg == null)
		{
			return;
		}
		
		for(int i = 0; i < msg.getMessage().length; i++)
		{
			Color oldColor = null;
			Font oldFont = null;
			
			if(i < msg.getColor().length && msg.getColor()[i] != null)
			{
				oldColor = gfx.getColor();
				gfx.setColor(msg.getColor()[i]);
			}
			if(msg.getFont() != null)
			{
				oldFont = gfx.getFont();
				gfx.setFont(msg.getFont());
			}
			
			gfx.drawString(msg.getMessage()[i], 
					(int) (Options.SCREEN_WIDTH/2-(msg.getWholeStringBoundsWidth(gfx)/2)+msg.getXOffset(gfx, i)) + msg.getX(), 
					msg.getY());
			
			if(oldColor != null)
			{
				gfx.setColor(oldColor);
			}
			if(oldFont != null)
			{
				gfx.setFont(oldFont);
			}
		}
	}
	
	private void printSubtitle(Graphics2D gfx)
	{
		gfx.setFont(FontManagement.agencyFBPlain_small);
		gfx.setColor(new Color(255, 209, 0/* ,alpha*/));
		
		//Selection rounded up to the next 10
		switch(((selection+9)/10)*10)
		{
			case 10:
				gfx.drawString(SUBTITLES[0], 
						(int) (Options.SCREEN_WIDTH/2-gfx.getFont().getStringBounds(SUBTITLES[0], gfx.getFontRenderContext()).getWidth()/2),
						(int) (FontManagement.agencyFBPlain_large.getStringBounds(TITLE, gfx.getFontRenderContext()).getHeight()*0.5 + Options.SCREEN_HEIGHT/26*4));
				break;
			case 30:
				gfx.drawString(SUBTITLES[1], 
						(int) (Options.SCREEN_WIDTH/2-gfx.getFont().getStringBounds(SUBTITLES[1], gfx.getFontRenderContext()).getWidth()/2),
						(int) (FontManagement.agencyFBPlain_large.getStringBounds(TITLE, gfx.getFontRenderContext()).getHeight()*0.5 + Options.SCREEN_HEIGHT/26*4));
				break;
			case 210:
				gfx.drawString(SUBTITLES[3], 
						(int) (Options.SCREEN_WIDTH/2-gfx.getFont().getStringBounds(SUBTITLES[3], gfx.getFontRenderContext()).getWidth()/2),
						(int) (FontManagement.agencyFBPlain_large.getStringBounds(TITLE, gfx.getFontRenderContext()).getHeight()*0.5 + Options.SCREEN_HEIGHT/26*4));
				break;
			default:
				//Not a fatal error
				System.err.println("No subtitle available for the selection range under" + ((selection+9)/10)*10);
				break;
		}
	}
	
	private void drawArrow(Graphics2D gfx, Color color, String string, int y)
	{
		Color oldColor = gfx.getColor();
		gfx.setColor(color);
		gfx.drawString(">", (int) ((Options.SCREEN_WIDTH/2-gfx.getFont().getStringBounds(string, gfx.getFontRenderContext()).getWidth()/2) 
				- gfx.getFont().getStringBounds(">", gfx.getFontRenderContext()).getWidth()/2 - Options.SCREEN_WIDTH/32), y);
		gfx.setColor(oldColor);
	}

	private void manageMenu()
	{	
		//-----[START] Input checks -----
		//Down key
		if(KeyInputManagement.downKeyPressed == true && System.currentTimeMillis()-timeouts[0] >= 200 && KeyInputManagement.upKeyPressed == false)
		{
			selection++;
			timeouts[0] = System.currentTimeMillis();
			Audio.playSound(Sfxs.MenuMovement);
		}
		else if((KeyInputManagement.downKeyPressed == false || KeyInputManagement.upKeyPressed == true) && timeouts[0] != 0)
		{
			timeouts[0] = 0;
		}
		
		//Up key
		if(KeyInputManagement.upKeyPressed == true && System.currentTimeMillis()-timeouts[1] >= 200 && KeyInputManagement.downKeyPressed == false)
		{
			selection--;
			timeouts[1] = System.currentTimeMillis();
			Audio.playSound(Sfxs.MenuMovement);
		}
		else if((KeyInputManagement.upKeyPressed == false || KeyInputManagement.downKeyPressed == true) && timeouts[1] != 0)
		{
			timeouts[1] = 0;
		}
		
		//Fire/selection key
		if(KeyInputManagement.fireKeyPressed == true && System.currentTimeMillis()-timeouts[2] >= 200)
		{
			if(checkSelection() == true)
			{
				Audio.playSound(Sfxs.Beep1);
				timeouts[2] = System.currentTimeMillis() + 1000;
			}
			else
			{
				timeouts[2] = System.currentTimeMillis();
			}
		}
		else if(KeyInputManagement.fireKeyPressed == false && timeouts[2] != 0)
		{
			timeouts[2] = 0;
		}
		
		//Back button
		if(KeyInputManagement.escKeyPressed == true && System.currentTimeMillis()-timeouts[3] >= 200)
		{
			goBack();
			timeouts[3] = System.currentTimeMillis();
		}
		else if(KeyInputManagement.escKeyPressed == false && timeouts[3] != 0)
		{
			timeouts[3] = 0;
		}
		
		//Left button
		if(KeyInputManagement.leftKeyPressed == true && System.currentTimeMillis()-timeouts[4] >= 200 && KeyInputManagement.rightKeyPressed == false)
		{
			changeOptionValue(false);
			timeouts[4] = System.currentTimeMillis();
		}
		else if((KeyInputManagement.leftKeyPressed == false || KeyInputManagement.rightKeyPressed == true) && timeouts[4] != 0)
		{
			timeouts[4] = 0;
			arrowColor[0] = Color.YELLOW;
		}
		
		//Right button
		if(KeyInputManagement.rightKeyPressed == true && System.currentTimeMillis()-timeouts[5] >= 200 && KeyInputManagement.leftKeyPressed == false)
		{
			changeOptionValue(true);
			timeouts[5] = System.currentTimeMillis();
		}
		else if((KeyInputManagement.rightKeyPressed == false || KeyInputManagement.leftKeyPressed == true) && timeouts[5] != 0)
		{
			timeouts[5] = 0;
			arrowColor[1] = Color.YELLOW;
		}
		//-----[END] Input checks -----
		
		//-----[START] Wrapper Checks -----
		if(selection == 0 || selection == 5)
		{
			selection = (selection == 0) ? 4 : 1;
		}
		else if(selection == 20 || selection == 25)
		{
			selection = (selection == 20) ? 24 : 21;
		}
		else if(selection == 200 || selection == 208)
		{
			selection = (selection == 200) ? 207 : 201;
		}
		//-----[END] Wrapper Checks -----
		
		//Set difficulty mods
		switch(onDifficulty)
		{
			case 0://Very Easy
				messages.get(202).setMessage(DIFFICULTIES[onDifficulty], 1);//Difficulty
				messages.get(203).setMessage("4", 1);//Rounds between bosses
				messages.get(204).setMessage("5", 1);//Starting lives
				messages.get(205).setMessage("0.7", 1);//Alien pack speed modifier
				messages.get(206).setMessage("1.4",1);//Weapon cooldown modifier
				break;
			case 1://Easy
				messages.get(202).setMessage(DIFFICULTIES[onDifficulty], 1);//Difficulty
				messages.get(203).setMessage("3", 1);//Rounds between bosses
				messages.get(204).setMessage("4", 1);//Starting lives
				messages.get(205).setMessage("0.8", 1);//Alien pack speed modifier
				messages.get(206).setMessage("1.2", 1);//Weapon cooldown modifier
				break;
			default://Default to normal
			case 2://Normal
				messages.get(202).setMessage(DIFFICULTIES[onDifficulty], 1);//Difficulty
				messages.get(203).setMessage("3", 1);//Rounds between bosses
				messages.get(204).setMessage("3", 1);//Starting lives
				messages.get(205).setMessage("1.0", 1);//Alien pack speed modifier
				messages.get(206).setMessage("1.0", 1);//Weapon cooldown modifier
				break;
			case 3://Hard
				messages.get(202).setMessage(DIFFICULTIES[onDifficulty], 1);//Difficulty
				messages.get(203).setMessage("3", 1);//Rounds between bosses
				messages.get(204).setMessage("2", 1);//Starting lives
				messages.get(205).setMessage("1.1", 1);//Alien pack speed modifier
				messages.get(206).setMessage("0.9", 1);//Weapon cooldown modifier
				break;
			case 4://Nightmare
				messages.get(202).setMessage(DIFFICULTIES[onDifficulty], 1);//Difficulty
				messages.get(203).setMessage("3", 1);//Rounds between bosses
				messages.get(204).setMessage("1", 1);//Starting lives
				messages.get(205).setMessage("1.3", 1);//Alien pack speed modifier
				messages.get(206).setMessage("0.8", 1);//Weapon cooldown modifier
				break;
			case 5://Custom
				//If the colors have not been set yet
				if(Arrays.equals(messages.get(203).getColor(), new Color[]{Color.YELLOW, Color.WHITE}) == false
					&& Arrays.equals(messages.get(206).getColor(), new Color[]{Color.YELLOW, Color.WHITE}) == false)
				{
					//Set the colors
					messages.get(203).setColor(new Color[]{Color.YELLOW, Color.WHITE});
					messages.get(204).setColor(new Color[]{Color.YELLOW, Color.WHITE});
					messages.get(205).setColor(new Color[]{Color.YELLOW, Color.WHITE});
					messages.get(206).setColor(new Color[]{Color.YELLOW, Color.WHITE});
					
					//Set the mods to Normal setting
					messages.get(203).setMessage("3", 1);//Rounds between bosses
					messages.get(204).setMessage("3", 1);//Starting lives
					messages.get(205).setMessage("1.0", 1);//Alien pack speed modifier
					messages.get(206).setMessage("1.0", 1);//Weapon cooldown modifier
				}
				break;
		}

		//If the difficulty is not custom and the colors have not yet been reset
		if(onDifficulty != 5 && Arrays.equals(messages.get(203).getColor(), new Color[]{Color.YELLOW, Color.WHITE}) == true
			&& Arrays.equals(messages.get(206).getColor(), new Color[]{Color.YELLOW, Color.WHITE}) == true)
		{
			//Set the colors
			messages.get(203).setColor(new Color[]{Color.GRAY, Color.GRAY});
			messages.get(204).setColor(new Color[]{Color.GRAY, Color.GRAY});
			messages.get(205).setColor(new Color[]{Color.GRAY, Color.GRAY});
			messages.get(206).setColor(new Color[]{Color.GRAY, Color.GRAY});
		}
	}
	
	private boolean checkSelection()
	{
		switch(selection)
		{
			case 2:
				selection = 21;
				return true;
			case 4:
				System.exit(0);
				return true;
			case 21:
				onDifficulty = 2;
				selection = 201;
				return true;
			case 24:
				selection = 1;
				return true;
			case 201:
				setInMainMenu(false);
				Audio.playSound(Sfxs.Beep1);
				ObjectCollection.getGameManagement().startGame((byte) 1, new byte[]{
						Byte.parseByte(messages.get(203).getMessage()[1]),
						Byte.parseByte(messages.get(204).getMessage()[1])}, new float[] {
						(float) (Math.round(Float.parseFloat(messages.get(205).getMessage()[1])*10.0)/10.0),
						(float) (Math.round(Float.parseFloat(messages.get(206).getMessage()[1])*10.0)/10.0)
				});
				return false;//To avoid any weird sounds
			case 207:
				selection = 21;
				return true;
			default:
				Audio.playSound(Sfxs.MenuBadSelect);
				return false;
		}
	}
	
	private void goBack()
	{
		if(selection <= 40 && selection >= 10)
		{
			selection = 1;
		}
		else if(selection <= 210 && selection >= 200)
		{
			selection = 21;
		}
	}
	
	private void changeOptionValue(boolean increase)
	{
		if(selection == 202)
		{
			onDifficulty += (increase == true) ? 1 : -1;//Difficulty
			//if(onDifficulty < 0 || onDifficulty > 5)
			{
				onDifficulty = (byte) ((onDifficulty < 0) ? 5 : onDifficulty%6);
			}
			messages.get(202).setMessage(DIFFICULTIES[onDifficulty], 1);
		}
		else if(onDifficulty == 5 && selection > 200 && selection < 210)//Difficulty is custom
		{
			switch(selection)
			{
				case 203://Rounds between bosses
					if(increase == true && Byte.parseByte(messages.get(203).getMessage()[1]) == 50)//Cap of 50
					{
						break;
					}
					else if(increase == false && Byte.parseByte(messages.get(203).getMessage()[1]) == 1)//Min of 1
					{
						break;
					}
					messages.get(203).setMessage((Byte.parseByte(messages.get(203).getMessage()[1]) + ((increase == true) ? 1 : -1)) + "", 1);
					break;
				case 204://Starting lives
					if(increase == true && Byte.parseByte(messages.get(204).getMessage()[1]) == 20)//Cap of 20
					{
						break;
					}
					else if(increase == false && Byte.parseByte(messages.get(204).getMessage()[1]) == 1)//Min of 1
					{
						break;
					}
					messages.get(204).setMessage((Byte.parseByte(messages.get(204).getMessage()[1]) + ((increase == true) ? 1 : -1)) + "", 1);
					break;
				case 205://Alien pack speed modifier
					if(increase == true && Math.round(Float.parseFloat(messages.get(205).getMessage()[1])*10.0)/10.0 == 10.0)//Cap of 10.0
					{
						break;
					}
					else if(increase == false && Math.round(Float.parseFloat(messages.get(205).getMessage()[1])*10.0)/10.0 == 0.1)//Min of 0.1
					{
						break;
					}
					messages.get(205).setMessage(Math.round((Float.parseFloat(messages.get(205).getMessage()[1]) + ((increase == true) ? 0.1 : -0.1))*10.0)/10.0 + "", 1);
					break;
				case 206://Weapon cooldown modifier
					if(increase == true && Math.round(Float.parseFloat(messages.get(206).getMessage()[1])*10.0)/10.0 == 10.0)//Cap of 10.0
					{
						break;
					}
					else if(increase == false && Math.round(Float.parseFloat(messages.get(206).getMessage()[1])*10.0)/10.0 == 0.1)//Min of 0.1
					{
						break;
					}
					messages.get(206).setMessage(Math.round((Float.parseFloat(messages.get(206).getMessage()[1]) + ((increase == true) ? 0.1 : -0.1))*10.0)/10.0 + "", 1);
					break;
				default:
					return;
			}
		}
		else
		{
			return;
		}
		
		Audio.playSound(Sfxs.MenuMovement);
		arrowColor[(increase == true) ? 1 : 0] = Color.RED;
	}

	@Override
	public void graphicsCall(Graphics2D gfx, boolean resized)
	{
		//If not in the main menu, return out immediately
		if(inMainMenu == false)
		{
			return;
		}
		
		if(resized == true)
		{
			setUpMsgs();
		}
		
		Color foreground = gfx.getColor();
		Font oldFont = gfx.getFont();
		
		printSubtitle(gfx);
		displayMainMenu(gfx);
		
		gfx.setFont(oldFont);
		gfx.setColor(foreground);
	}

	@Override
	public void update()
	{
		//If not in the main menu, return out immediately
		if(inMainMenu == false)
		{
			return;
		}
				
		manageMenu();
	}
	
	public boolean inMainMenu()
	{
		return inMainMenu;
	}
	
	public void setInMainMenu(boolean inMainMenu)
	{
		if(inMainMenu == this.inMainMenu)
		{
			return;
		}
		
		this.inMainMenu = inMainMenu;
		
		//Make sure the main menu music is on
		if(inMainMenu == true)
		{
			ObjectCollection.getBackground();
			setUpMsgs();
			selection = 1;
			ObjectCollection.getRenderer().addGraphicsListener(this);
			ObjectCollection.getRenderer().addUpdateListener(this);
			Audio.openClips(new Tracks[] {Tracks.BGM2});
			Audio.changeTrack(Tracks.BGM2);
		}
		else
		{
			ObjectCollection.getRenderer().removeGraphicsListener(this);
			ObjectCollection.getRenderer().removeUpdateListener(this);
		}
	}
}
