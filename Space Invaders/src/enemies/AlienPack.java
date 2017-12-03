package enemies;

import java.awt.Graphics2D;
import java.util.ArrayList;

import core.Entity;
import utils.ObjectCollection;
import utils.Stats;

public class AlienPack extends Entity
{
	private short aliensInPack = 0;
	private short aliensInRow;
	private short aliensInColumn;
	private float rx[];
	private float px[];
	private float vx;
	private float vxCap = 0.00034f;
	private float vxDelta = 0.000008f;
	private float vxLvlMod = 0.000002f;
	private long timeStamp = 0;
	
	private ArrayList<MarchingAlien> aliens = new ArrayList<MarchingAlien>();
	
	/**The constructor for an AlienPack
	 * 
	 * @param objColl - The object collection
	 * @param aliensInRow - How many aliens should be in each row initially
	 */
	public AlienPack(short aliensInRow) 
	{
		this.aliensInRow = aliensInRow;
		aliensInPack = (short) (aliensInRow * 4);
		aliensInColumn = (short) (aliensInPack/aliensInRow);
		Stats.numberInPack = aliensInPack;
		rx = new float[aliensInRow];
		px = new float[rx.length];
		
		byte row = 1;
		byte columnRelatedToRow = 0;
		for(int i = 0; i < aliensInPack; i++)
		{
			if(i % aliensInRow == 0 && i > 0)
			{
				row++;
			}
			
			columnRelatedToRow = (byte) ((columnRelatedToRow >= aliensInRow) ? 1 : columnRelatedToRow+1);
			
			aliens.add(new MarchingAlien(0.05f, 0.05f, (short) (columnRelatedToRow-1)));
			
			aliens.get(i).setPosition(aliens.get(i).getDimension().width*columnRelatedToRow + aliens.get(i).getDimension().width * columnRelatedToRow/8
					+ Stats.SCREEN_WIDTH/aliensInRow*1.5f/*This part keeps them in the middle of the screen*/, 
					Stats.SCREEN_HEIGHT/ObjectCollection.getGameManagement().getAlienSteps()*row);
			
			aliens.get(i).activate((short) 1);
			
			ObjectCollection.getEntityManagement().addEntity(aliens.get(i));
		}
		
		for(int i = 0; i < rx.length; i++)
		{
			rx[i] = aliens.get(i).getRx();
			px[i] = rx[i]/Stats.SCREEN_WIDTH;
			//System.out.println("px[" + i + "] = " + px[i]);
		}
		
		vx = vxDelta*6 + vxDelta * (Stats.level*0.206f);//Initial vx
		//System.out.println("Initial vx = " + vx + ". vxDelta*4 = " + vxDelta*4.0f);
		
		ObjectCollection.getEntityManagement().addEntity(this);
	}
	
	@Override
	public void move(int tm) 
	{
		if(System.currentTimeMillis() - timeStamp > 500)
		{
			for(int i = 0; i < aliensInRow; i++)
			{
				px[i] = rx[i]/Stats.SCREEN_WIDTH + vx * tm;
				rx[i] = Stats.SCREEN_WIDTH*px[i];
				
				for(int j = 0; j < aliensInColumn; j++)
				{
					aliens.get(aliensInRow*j + i).setRx(rx[i]);
					aliens.get(aliensInRow*j + i).lowerTimeBonus(tm);
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
					vx -= vxDelta + vxLvlMod*Stats.level;//increase velocity
				}
				nudgeDistance = rx[contactAlien.getNumInRow()] + contactAlien.getDimension().width - Stats.SCREEN_WIDTH;
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
					vx += vxDelta + vxLvlMod*Stats.level;//increase velocity
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
			
			for(int i = 0; i < aliensInRow; i++)
			{
				for(int j = 0; j < aliensInColumn; j++)
				{
					aliens.get(aliensInRow*j + i).setRx(rx[i]);
				}
			}
			
			//Move the aliens down a row
			for(int i = 0; i < aliens.size(); i++)
			{
				aliens.get(i).setRy(aliens.get(i).getRy() + Stats.SCREEN_HEIGHT/ObjectCollection.getGameManagement().getAlienSteps());
			}
		}
		
		//System.out.println("vx = " + vx);
	}
	
	public void moveUp(int numOfLeaps)
	{
		for(int i = 0; i < aliens.size(); i++)
		{
			aliens.get(i).setRy(aliens.get(i).getRy() - Stats.SCREEN_HEIGHT/ObjectCollection.getGameManagement().getAlienSteps()*numOfLeaps);
			
			if(aliens.get(i).getActive() == 0)
			{
				aliens.get(i).activate((short) 1);
			}	
		}
		
		vx *= 0.4f;
	}
	
	public short getAliensInPack()
	{
		return aliensInPack;
	}
	
	public ArrayList<MarchingAlien> getAlienPack()
	{
		return aliens;
	}

	@Override
	public void resize(int oldScreenWidth, int oldScreenHeight)
	{
		for(int i = 0; i < rx.length; i++)//adjust rx
		{
			float percentageThroughX = rx[i]/oldScreenWidth;
			rx[i] = Stats.SCREEN_WIDTH * percentageThroughX;
		}
		
		for(int i = 0; i < aliens.size(); i++)//adjust ry
		{
			float percentageThroughY = aliens.get(i).getRy()/oldScreenHeight;
			aliens.get(i).setRy(Stats.SCREEN_HEIGHT * percentageThroughY);
		}
	}

	@Override
	public void draw(Graphics2D gfx, int tm)
	{
		return;
		//DRAW NOTHING
	}

	@Override
	public boolean inCollision(Entity e)
	{
		//TODO pack collision detection goes here
		return false;
	}
}
