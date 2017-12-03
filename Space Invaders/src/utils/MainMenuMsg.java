package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class MainMenuMsg
{
	private Color[] color;
	private String[] message;
	private Font font;
	private int x;
	private int y;
	
	public MainMenuMsg(Color[] color, String[] message, int x, int y)
	{
		this.color = color;
		this.message = message;
		this.x = x;
		this.y = y;
	}
	
	public MainMenuMsg(Color color, String message, Font font, int x, int y)
	{
		this.color = new Color[1];
		this.message = new String[1];
		this.color[0] = color;
		this.message[0] = message;
		this.font = font;
		this.x = x;
		this.y = y;
	}
	
	public MainMenuMsg(Color color, String message, int x, int y)
	{
		this.color = new Color[1];
		this.message = new String[1];
		
		this.color[0] = color;
		this.message[0] = message;
		this.x = x;
		this.y = y;
	}
	
	public MainMenuMsg(Color[] color, String[] message)
	{
		this.color = color;
		this.message = message;	
		x = 0;
		y = 0;
	}
	
	public MainMenuMsg(Color color, String message)
	{
		this.color = new Color[1];
		this.message = new String[1];
		
		this.color[0] = color;
		this.message[0] = message;	
		x = 0;
		y = 0;
	}

	public Color[] getColor()
	{
		return color;
	}

	public void setColor(Color[] color)
	{
		this.color = color;
	}
	
	public void setColor(Color color, int index)
	{
		if(this.color.length >= index+1 && this.color[index] != null)
		{
			this.color[index] = color;
		}
	}

	public String[] getMessage()
	{
		return message;
	}
	
	public String getMessageWhole()
	{
		StringBuilder sb = new StringBuilder();
		
		for(String str : message)
		{
			sb.append(str);
		}
		
		return sb.toString();
	}

	public void setMessage(String[] message)
	{
		this.message = message;
	}
	
	public void setMessage(String message, int index)
	{
		if(this.message.length >= index+1 && this.message[index] != null)
		{
			this.message[index] = message;
		}
	}
	
	public Font getFont()
	{
		return font;
	}
	
	public void setFont(Font font)
	{
		this.font = font;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
	
	public int getStringBoundsWidth(Graphics2D gfx, int i)
	{
		return (int) ((font == null) ? gfx.getFont() : font).getStringBounds(message[i], gfx.getFontRenderContext()).getWidth();
	}
	
	public int getStringBoundsHeight(Graphics2D gfx, int i)
	{
		return (int) ((font == null) ? gfx.getFont() : font).getStringBounds(message[i], gfx.getFontRenderContext()).getHeight();
	}
	
	public Rectangle2D getStringBounds(Graphics2D gfx, int i)
	{
		return ((font == null) ? gfx.getFont() : font).getStringBounds(message[i], gfx.getFontRenderContext());
	}
	
	public int getWholeStringBoundsWidth(Graphics2D gfx)
	{
		return (int) ((font == null) ? gfx.getFont() : font).getStringBounds(getMessageWhole(), gfx.getFontRenderContext()).getWidth();
	}
	
	public int getWholeStringBoundsHeight(Graphics2D gfx)
	{
		return (int) ((font == null) ? gfx.getFont() : font).getStringBounds(getMessageWhole(), gfx.getFontRenderContext()).getHeight();
	}
	
	public int getXOffset(Graphics2D gfx, int i)
	{
		int offset = 0;
		
		for(int j = 0; j < i; j++)
		{
			offset += getStringBoundsWidth(gfx, j);
		}
		
		return offset;
	}
}
