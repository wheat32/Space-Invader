package io;

import java.awt.Font;

import utils.Stats;

public class FontManagement
{
	public Font arialNarrow_small = new Font("Arial Narrow", Font.PLAIN, 12);
	public Font font = new Font("Courier New", Font.PLAIN, 16);
	public Font medFont = new Font("Courier New", Font.PLAIN, 24);
	public Font medFontBold = new Font("Courier New", Font.BOLD, 24);
	public Font agencyFBPlain_small = new Font("Agency FB", Font.PLAIN, 64);
	public Font agencyFBPlain_large = new Font("Agency FB", Font.PLAIN, 128);
	
	public FontManagement()
	{
		resetFontSizes();
	}
	
	public void resetFontSizes()
	{
		if(Stats.SCREEN_WIDTH < Stats.SCREEN_HEIGHT) //screen height is larger than the width
		{
			arialNarrow_small = arialNarrow_small.deriveFont(Stats.SCREEN_WIDTH*0.022f);
			font = font.deriveFont(Stats.SCREEN_WIDTH*0.025f);
			medFont = medFont.deriveFont(Stats.SCREEN_WIDTH*0.035f);
			medFontBold = medFontBold.deriveFont(Stats.SCREEN_WIDTH*0.035f);
			agencyFBPlain_small = agencyFBPlain_small.deriveFont(Stats.SCREEN_WIDTH*0.08f);
			agencyFBPlain_large = agencyFBPlain_large.deriveFont(Stats.SCREEN_WIDTH*0.16f);
			//System.out.println("Height is larger");
		}
		else//screen width is larger
		{
			arialNarrow_small = arialNarrow_small.deriveFont(Stats.SCREEN_HEIGHT*0.022f);
			font = font.deriveFont(Stats.SCREEN_HEIGHT*0.025f);
			medFont = medFont.deriveFont(Stats.SCREEN_HEIGHT*0.035f);
			medFontBold = medFontBold.deriveFont(Stats.SCREEN_HEIGHT*0.035f);
			agencyFBPlain_small = agencyFBPlain_small.deriveFont(Stats.SCREEN_HEIGHT*0.08f);
			agencyFBPlain_large = agencyFBPlain_large.deriveFont(Stats.SCREEN_HEIGHT*0.16f);
			//System.out.println("Width is larger");
		}
		//System.out.println("font size: " + font.getSize() + " | medFont size: " + medFont.getSize() + " | agencyFBPlain_large: " + agencyFBPlain_large.getSize());
	}
}
