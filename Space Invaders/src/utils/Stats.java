package utils;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Stats//TODO move this into GameModes
{
	public static short majorRelease = 0;
	public static String minorRelease = "pre_3";
	public static short releaseRevisions = 0;
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	public static int level = 0;
	public static long score = 0;
	public static int totalTimesShot = 0;
	public static int totalHits = 0;
	public static int timesShot = 0;
	public static int hits = 0;
	public static int killedAliens = 0;
	public static int numberInPack = 0;
	public static short lives = 0;
	public static long totalTime = 0;
	public static long roundTime = 0;
	
	private static DecimalFormat millisecondsFormat = new DecimalFormat("000");
	private static DecimalFormat secondsFormat = new DecimalFormat("00");
	
	public static String getAccuracy(boolean total)
	{
		DecimalFormat format = new DecimalFormat("##0.##%");
		
		if(total == true)
		{
			if((totalHits*1.0/totalTimesShot*1.0) >= 1)
			{
				return format.format(1);
			}
			else
			{
				return format.format(totalHits*1.0f/totalTimesShot*1.0f);
			}
		}
		else
		{
			if((hits*1.0/timesShot*1.0) >= 1)
			{
				return format.format(1);
			}
			else
			{
				return format.format(hits*1.0f/timesShot*1.0f);
			}
		}
	}
	
	public static void increaseHits()
	{
		totalHits++;
		hits++;
	}
	
	public static String getRoundTimeFormatted()
	{
		return TimeUnit.MILLISECONDS.toMinutes(roundTime) + ":" + 
				secondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(roundTime)%60) + ":" + 
				millisecondsFormat.format(TimeUnit.MILLISECONDS.toMillis(roundTime)%1000);
	}
	
	public static String getTotalTimeFormatted()
	{
		return TimeUnit.MILLISECONDS.toMinutes(totalTime) + ":" + 
				secondsFormat.format(TimeUnit.MILLISECONDS.toSeconds(totalTime)%60) + ":" + 
				millisecondsFormat.format(TimeUnit.MILLISECONDS.toMillis(totalTime)%1000);
	}
}
