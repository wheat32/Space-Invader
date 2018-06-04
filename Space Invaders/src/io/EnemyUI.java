package io;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entities.bosses.Boss;
import entities.bosses.BossAlien;

public class EnemyUI
{
	private long timeStamps[] = {0, 0};
	private int HPStamp = 0;
	private int shieldStamp = 0;
	private short HPBarAlpha = 0;
	private short shieldBarAlpha = 0;
	
	private Boss boss;
	
	public EnemyUI(Boss boss)
	{
		this.boss = boss;
	}
	
	public void displayBossUI(Graphics2D gfx, long elapsedTime)
	{		
		doHPAlphaMath(elapsedTime);
		
		//If boss is an instance of a boss which has a shield
		if(boss.hasShield() == true)
		{
			doShieldAlphaMath(elapsedTime);
		}
		
		//If they're both 0, return out
		if(HPBarAlpha <= 0 && shieldBarAlpha <= 0)
		{
			return;
		}
		
		Color foreground = gfx.getColor();
		
		if(HPBarAlpha > 0)
		{
			drawHPBar(gfx);
		}
		
		//If the boss is has a shield and the alpha is above 0
		if(boss.hasShield() == true && shieldBarAlpha > 0)
		{
			drawShieldBar(gfx);
		}
		
		gfx.setColor(foreground);
	}
	
	private void doHPAlphaMath(long elapsedTime)
	{
		//Check if the boss bar can fade
		if(boss.getCurrentHealth() != HPStamp)
		{
			HPStamp = boss.getCurrentHealth();
			timeStamps[0] = System.currentTimeMillis();
			
			HPBarAlpha = 220;
		}
		else if(elapsedTime == 0)
		{
			HPStamp = 0;
		}
		else if(System.currentTimeMillis() - timeStamps[0] >= 2000 && HPBarAlpha > 0)
		{
			HPBarAlpha -= elapsedTime/10;
		}
	}
	
	private void doShieldAlphaMath(long elapsedTime)
	{
		//Check if the shield bar can fade
		if(((BossAlien) boss).getShield().getCurrentHealth() != shieldStamp)
		{
			shieldStamp = (int) ((BossAlien) boss).getShield().getCurrentHealth();
			timeStamps[1] = System.currentTimeMillis();
			
			shieldBarAlpha = 220;
		}
		else if(elapsedTime == 0)
		{
			shieldStamp = 0;
		}
		else if(System.currentTimeMillis() - timeStamps[1] >= 2000 && shieldBarAlpha > 0)
		{
			shieldBarAlpha -= elapsedTime/10;
		}
		
		if(((BossAlien) boss).getShield().isRecharging() == true)
		{
			shieldBarAlpha *= 0.6f;//Slowly lower the alpha
		}
	}
	
	private void drawHPBar(Graphics2D gfx)
	{
		gfx.setColor(new Color(255, 0, 0, HPBarAlpha));//red
		Rectangle rect = new Rectangle((int) (boss.getRx() + boss.getDimension().width*0.55f), 
				(int) (boss.getRy() + boss.getDimension().height*0.4f),
				(int) (boss.getDimension().width*0.44f), (int) (boss.getDimension().height*0.06f));
		gfx.draw(rect);
		gfx.setColor(new Color(146, 146, 146, (int) (HPBarAlpha*0.7)));//gray//TODO
		gfx.fillRect(rect.getBounds().x+1,
				rect.getBounds().y+1,
				rect.getBounds().width-1,
				rect.getBounds().height-1);
		gfx.setColor(new Color(255, 255, 0, HPBarAlpha));//yellow
		gfx.fillRect(rect.getBounds().x+1,
				rect.getBounds().y+1,
				(int) ((rect.getBounds().width-1)*((boss.getCurrentHealth()*1.0)/(boss.getMaxHealth()*1.0))),
				(rect.getBounds().height-1));
	}
	
	private void drawShieldBar(Graphics2D gfx)
	{
		gfx.setColor(new Color(255, 0, 0, shieldBarAlpha));//red
		Rectangle rect = new Rectangle((int) (boss.getRx() + boss.getDimension().width*0.55f), //x
				(int) ((boss.getRy() + boss.getDimension().height*0.4f)*2),//y
				(int) (boss.getDimension().width*0.44f), //width
				(int) (boss.getDimension().height*0.06f));//height
		gfx.draw(rect);
		gfx.setColor(new Color(146, 146, 146, (int) (shieldBarAlpha*0.7)));//gray//TODO
		gfx.fillRect(rect.getBounds().x+1,
				rect.getBounds().y+1,
				rect.getBounds().width-1,
				rect.getBounds().height-1);
		gfx.setColor(new Color(111, 0, 255, shieldBarAlpha));//purple
		gfx.fillRect(rect.getBounds().x+1,
				rect.getBounds().y+1,
				(int) ((rect.getBounds().width-1)*((((BossAlien) boss).getShield().getCurrentHealth()*1.0)/(((BossAlien) boss).getShield().getMaxHealth()*1.0))),
				rect.getBounds().height-1);
	}
}
