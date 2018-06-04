package io;

import java.awt.Font;

import system.Options;

public class FontManagement
{
	public static Font arialNarrow_small = new Font("Arial Narrow", Font.PLAIN, 12);
	public static Font font = new Font("Courier New", Font.PLAIN, 16);
	public static Font medFont = new Font("Courier New", Font.PLAIN, 24);
	public static Font medFontBold = new Font("Courier New", Font.BOLD, 24);
	public static Font agencyFBPlain_small = new Font("Agency FB", Font.PLAIN, 64);
	public static Font agencyFBPlain_large = new Font("Agency FB", Font.PLAIN, 128);
	
	public static void resetFontSizes()
	{
		if(Options.SCREEN_WIDTH < Options.SCREEN_HEIGHT) //screen height is larger than the width
		{
			arialNarrow_small = arialNarrow_small.deriveFont(Options.SCREEN_WIDTH*0.022f);
			font = font.deriveFont(Options.SCREEN_WIDTH*0.025f);
			medFont = medFont.deriveFont(Options.SCREEN_WIDTH*0.035f);
			medFontBold = medFontBold.deriveFont(Options.SCREEN_WIDTH*0.035f);
			agencyFBPlain_small = agencyFBPlain_small.deriveFont(Options.SCREEN_WIDTH*0.08f);
			agencyFBPlain_large = agencyFBPlain_large.deriveFont(Options.SCREEN_WIDTH*0.16f);
			//System.out.println("Height is larger");
		}
		else//screen width is larger
		{
			arialNarrow_small = arialNarrow_small.deriveFont(Options.SCREEN_HEIGHT*0.022f);
			font = font.deriveFont(Options.SCREEN_HEIGHT*0.025f);
			medFont = medFont.deriveFont(Options.SCREEN_HEIGHT*0.035f);
			medFontBold = medFontBold.deriveFont(Options.SCREEN_HEIGHT*0.035f);
			agencyFBPlain_small = agencyFBPlain_small.deriveFont(Options.SCREEN_HEIGHT*0.08f);
			agencyFBPlain_large = agencyFBPlain_large.deriveFont(Options.SCREEN_HEIGHT*0.16f);
			//System.out.println("Width is larger");
		}
		//System.out.println("font size: " + font.getSize() + " | medFont size: " + medFont.getSize() + " | agencyFBPlain_large: " + agencyFBPlain_large.getSize());
	}
}
