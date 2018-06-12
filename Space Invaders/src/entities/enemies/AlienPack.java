package entities.enemies;

import java.util.ArrayList;

import entities.Entity;
import entities.EntityTags.EntityFaction;
import gameModes.GameMode;
import system.Options;
import system.Time;
import utils.EntityManagement;
import utils.ObjectCollection;

public class AlienPack extends Entity
{
	private short aliensInRow;
	private short aliensInColumn;
	private float rx[];
	private float px[];
	private float vx;
	private float vxCap = 0.00034f;
	private float vxDelta = 0.000008f;
	private float vxLvlMod = 0.000002f;
	private long timeStamp = 0;
	
	private ArrayList<Entity> aliens = new ArrayList<Entity>();
	private short aliensAlive;
	private GameMode gameMode;
	
	public AlienPack(int numberInPack) 
	{
		super(EntityFaction.ALIEN);
		super.setHealth(numberInPack);//The health for this represents how many aliens are in the pack
		gameMode = ObjectCollection.getGameManagement().getGameMode();
		aliensInRow = (short) (numberInPack/4);
		aliensInColumn = (short) (numberInPack/aliensInRow);
		rx = new float[aliensInRow];
		px = new float[rx.length];
		
		MarchingAlien marchingAlien;
		byte row = 1;
		byte columnRelatedToRow = 0;
		
		for(int i = 0; i < numberInPack; i++)
		{
			if(i % aliensInRow == 0 && i > 0)
			{
				row++;
			}
			
			columnRelatedToRow = (byte) ((columnRelatedToRow >= aliensInRow) ? 1 : columnRelatedToRow+1);
			
			marchingAlien = new MarchingAlien(0.05f, 0.05f, (short) (columnRelatedToRow-1));
			marchingAlien.setAlienPack(this);
			
			marchingAlien.setPosition(marchingAlien.getDimension().width*columnRelatedToRow + marchingAlien.getDimension().width * columnRelatedToRow/8
					+ Options.SCREEN_WIDTH/aliensInRow*1.5f/*This part keeps them in the middle of the screen*/, 
					Options.SCREEN_HEIGHT/ObjectCollection.getGameManagement().getAlienSteps()*row);
			
			marchingAlien.activate((short) 1);
			marchingAlien.setHealth(1);
			aliens.add(marchingAlien);
		}
		
		for(int i = 0; i < rx.length; i++)
		{
			rx[i] = aliens.get(i).getRx();
			px[i] = rx[i]/Options.SCREEN_WIDTH;
			//System.out.println("px[" + i + "] = " + px[i]);
		}
		
		vx = vxDelta*6 + vxDelta * (gameMode.level*0.206f);//Initial vx
		//System.out.println("Initial vx = " + vx + ". vxDelta*4 = " + vxDelta*4.0f);
		
		aliensAlive = (short) aliens.size();
		
		EntityManagement.addEntities(aliens);
	}
	
	@Override
	public void move() 
	{
		if(System.currentTimeMillis() - timeStamp > 500)
		{
			for(int i = 0; i < aliensInRow; i++)
			{
				px[i] = rx[i]/Options.SCREEN_WIDTH + vx * Time.deltaTime();
				rx[i] = Options.SCREEN_WIDTH*px[i];
			}
			
			for(Entity alien : aliens)
			{
				if(alien instanceof MarchingAlien)
				{
					MarchingAlien ma = (MarchingAlien) alien;
					ma.setRx(rx[ma.getNumInRow()]);
				}
			}
			
			//System.out.println("px[0] = " + px[0] + " | rx[0] = " + rx[0] + " | vx = " + vx);
		}
	}
	
	public void moveDown(MarchingAlien contactAlien)
	{
		if(System.currentTimeMillis() - timeStamp > 550)
		{
			//System.out.println("Time at contact: " + Stats.totalTime);
			timeStamp = System.currentTimeMillis();
			
			float nudgeDistance = 0;
			
			if(vx > 0)//less than 0 (traveling right)
			{
				vx = Math.abs(vx) * -1;
				
				if(vx <= vxCap * -1)
				{
					System.out.println("cap reached <. vx = " + vx);
					vx = vxCap * -1;
				}
				else
				{
					vx -= vxDelta + vxLvlMod*gameMode.level;//increase velocity
				}
				nudgeDistance = rx[contactAlien.getNumInRow()] + contactAlien.getDimension().width - Options.SCREEN_WIDTH;
				//System.out.println("contact alien: num in row: " + contactAlien.getNumInRow() + " - rx: " + rx[contactAlien.getNumInRow()]);
				//System.out.println("     alien width: " + contactAlien.getDimension().width);
			}
			else//greater than (or equal to) 0 (traveling left)
			{
				vx = Math.abs(vx);
				
				if(vx >= vxCap)
				{
					System.out.println("cap reached >. vx = " + vx);
					vx = vxCap;
				}
				else
				{
					vx += vxDelta + vxLvlMod*gameMode.level;//increase velocity
				}
				nudgeDistance = rx[contactAlien.getNumInRow()];
			}
			//System.out.println("Nudge distance: " + nudgeDistance);
			
			for(int i = 0; i < rx.length; i++)//change the positions of the columns
			{
				if(vx < 0)//before moving left
				{
					rx[i] -= nudgeDistance;
				}
				else//before moving right
				{
					rx[i] -= nudgeDistance;
				}
			}
			
			for(Entity alien : aliens)
			{
				if(alien instanceof MarchingAlien)
				{
					MarchingAlien ma = (MarchingAlien) alien;
					ma.setRx(rx[ma.getNumInRow()]);
				}
			}
			
			//Move the aliens down a row
			for(int i = 0; i < aliens.size(); i++)
			{
				aliens.get(i).setRy(aliens.get(i).getRy() + Options.SCREEN_HEIGHT/ObjectCollection.getGameManagement().getAlienSteps());
			}
		}
		
		//System.out.println("vx = " + vx);
	}
	
	public void moveUp(int numOfLeaps)
	{
		for(int i = 0; i < aliens.size(); i++)
		{
			aliens.get(i).setRy(aliens.get(i).getRy() - Options.SCREEN_HEIGHT/ObjectCollection.getGameManagement().getAlienSteps()*numOfLeaps);
			
			if(aliens.get(i).getActive() == 0)
			{
				aliens.get(i).activate((short) 1);
			}	
		}
		
		vx *= 0.4f;
	}
	
	public ArrayList<Entity> getAlienPack()
	{
		return aliens;
	}
	
	public void killAlien(Entity e)
	{
		aliensAlive--;
		aliens.remove(e);
		EntityManagement.removeEntity(e);
		//TODO replace shot alien with null
	}
	
	public short getAliensAlive()
	{
		return aliensAlive;
	}

	/*@Override//TODO make sure this isn't needed
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		for(int i = 0; i < rx.length; i++)//adjust rx
		{
			float percentageThroughX = rx[i]/oldScreenWidth;
			rx[i] = Options.SCREEN_WIDTH * percentageThroughX;
		}
		
		for(int i = 0; i < aliens.size(); i++)//adjust ry
		{
			float percentageThroughY = aliens.get(i).getRy()/oldScreenHeight;
			aliens.get(i).setRy(Options.SCREEN_HEIGHT * percentageThroughY);
		}
	}*/


	@Override
	public boolean inCollision()
	{
		return false;
	}
}
