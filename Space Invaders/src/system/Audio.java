package system;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio
{
	private static Clip currIntroClip = null;
	private static Clip currLoopClip = null;
	private static ArrayList<Clip> openBGMClips = new ArrayList<Clip>();
	
	private volatile static boolean interruptThread = false;
	private static boolean outOfFocus = false;
	private static float musicVolume = 0.0f;//anywhere between 0-1 for volume
	private static float musicVolumeHolder = musicVolume;
	private static float sfxVolume = 0.86f;
	
	public static enum Tracks
	{
		//Wave music
		BGM1("bgm/bgm1_intro.wav", "bgm/bgm1_loop.wav"),
		BGM2("bgm/bgm2_intro.wav", "bgm/bgm2_loop.wav"),//Menu
		BGM3("bgm/bgm3_intro.wav", "bgm/bgm3_loop.wav"),
		
		//Boss music
		BossBGM1("bgm/bossBGM1.wav", null),
		
		//Victory music
		Victory1("bgm/bigWin1_intro.wav", "bgm/bigWin1_loop.wav"),
		Victory2("bgm/bigWin2_intro.wav", "bgm/bigWin2_loop.wav"),
		
		//Lose music
		Lose1("bgm/lose.wav", null);
		
		public final String introResID;
		public Clip introClip;
		public final String loopResID;
		public Clip loopClip;
		
		private Tracks(String introResID, String loopResID)
		{
			this.introResID = introResID;
			this.loopResID = loopResID;
		}
	}
	
	public static enum Sfxs
	{
		Beep1("sfx/beep1.wav"),
		EnemyShoot1("sfx/enemyShoot1.wav"),
		FriendlyShoot1("sfx/friendlyShoot1.wav"),
		GunOverheat("sfx/gunOverheat.wav"),
		Hit("sfx/hit.wav"),
		MenuBadSelect("sfx/menuBadSelect.wav"),
		MenuMovement("sfx/menuMovement.wav"),
		SeismicBoom("sfx/seismicBoom.wav"),
		ShieldRicochet("sfx/shieldRicochet.wav");
		
		public final String sfxResID;
		public final Clip sfxClip;
		
		private Sfxs(String sfxResID)
		{
			this.sfxResID = sfxResID;
			Clip clip = null;
			
			try
			{
				URL url = Audio.class.getClassLoader().getResource(sfxResID);
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				AudioFormat format = audioInputStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				if(!AudioSystem.isLineSupported(info))
				{
					new LineUnavailableException("Line is not supported!");
				}
				
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioInputStream);
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue((float) (Math.log(sfxVolume)/Math.log(10.0f))*20.0f);
				//Formula for linear to DB is Math.log(linearVal)/Math.log(10.0f))*20.0f
			}
			catch(NullPointerException e)
			{
				System.err.println(sfxResID + " does not exist.");
				e.printStackTrace();
			}
			catch(IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch(UnsupportedAudioFileException e)
			{
				e.printStackTrace();
			}
			catch(LineUnavailableException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			sfxClip = clip;
		}
	}
	
	public static void openClips(Tracks[] tracksToOpen)
	{
		//Close all of the last clips
		for(Clip clip : openBGMClips) 
		{
			clip.close();
		}
		openBGMClips.clear();
		
		//Open these clips and add them to the open clips arraylist
		for(Tracks track : tracksToOpen)
		{
			try
			{
				URL url = Audio.class.getClassLoader().getResource(track.introResID);
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				AudioFormat format = audioInputStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				if(!AudioSystem.isLineSupported(info))
				{
					new LineUnavailableException("Line is not supported!");
				}
				
				track.introClip = (Clip) AudioSystem.getLine(info);
				track.introClip.open(audioInputStream);
				FloatControl volume = (FloatControl) track.introClip.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(musicVolume);
				track.introClip.start();
				track.introClip.stop();
				track.introClip.setMicrosecondPosition(0);

				openBGMClips.add(track.introClip);
				
				//If there is a loop
				if(track.loopResID != null)
				{
					url = Audio.class.getClassLoader().getResource(track.loopResID);
					audioInputStream = AudioSystem.getAudioInputStream(url);
					format = audioInputStream.getFormat();
					info = new DataLine.Info(Clip.class, format);
					
					if(!AudioSystem.isLineSupported(info))
					{
						new LineUnavailableException("Line is not supported!");
					}
					
					track.loopClip = (Clip) AudioSystem.getLine(info);
					track.loopClip.open(audioInputStream);
					volume = (FloatControl) track.loopClip.getControl(FloatControl.Type.MASTER_GAIN);
					volume.setValue((float) (Math.log(musicVolume)/Math.log(10.0f))*20.0f);
					track.loopClip.start();
					track.loopClip.stop();
					track.loopClip.setMicrosecondPosition(0);

					openBGMClips.add(track.loopClip);
				}
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			catch (UnsupportedAudioFileException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	public static void playSound(Sfxs sfx)
	{
		try
		{	
			Clip clip = sfx.sfxClip;
			clip.setMicrosecondPosition(0);
			clip.addLineListener(Audio.lineListener);	
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) (Math.log(sfxVolume)/Math.log(10.0f))*20.0f);
			clip.start();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void changeTrack(Tracks track)
	{
		//Return out if the requested track is already playing
		if(track.introClip == currIntroClip && track.loopClip == currLoopClip)
		{
			return;
		}
		
		interruptThread = true;
		
		if(currIntroClip != null)
		{
			currIntroClip.stop();
			currIntroClip.setFramePosition(0);
		}
		if(currLoopClip != null)
		{
			currLoopClip.stop();
			currLoopClip.setFramePosition(0);
		}
		
		try
		{
			currIntroClip = track.introClip;
			currIntroClip.addLineListener(lineListener);
			FloatControl volume = (FloatControl) currIntroClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) (Math.log(musicVolume)/Math.log(10.0f))*20.0f);
			currIntroClip.setFramePosition(0);
			currIntroClip.start();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//Operates the currLoopClip
		if(track.loopClip != null)
		{		
			try
			{
				currLoopClip = track.loopClip;
				currLoopClip.addLineListener(lineListener);	
			}
			catch(IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			Runnable runnable = new Runnable()
			{	
				@Override
				public void run()
				{
					FloatControl volume = (FloatControl) currLoopClip.getControl(FloatControl.Type.MASTER_GAIN);
					volume.setValue((float) (Math.log(0)/Math.log(10.0f))*20.0f);
					currLoopClip.start();
					currLoopClip.loop(Integer.MAX_VALUE);
					
					//System.out.println("Before loop: " + track.introResID);
					while((currIntroClip.getFramePosition() + 1200 <=  currIntroClip.getFrameLength() || currIntroClip.getFramePosition() <= 100) 
							&& currIntroClip.isRunning() == true && interruptThread == false)
					{
						Thread.yield();
					}
					
					if(interruptThread == true)
					{
						//System.out.println("Audio: Killing thread.");
						return;
					}
					
					//System.out.println(currIntroClip.getFramePosition());
					//System.out.println("Audio: After loop: " + track.introResID);
					volume.setValue((float) (Math.log(musicVolume)/Math.log(10.0f))*20.0f);
					currLoopClip.setFramePosition(0);
				}
			};
			interruptThread = false;
			new Thread(runnable).start();
			//System.out.println("Audio: Starting new thread");
		}
		else
		{
			currLoopClip = null;
			currIntroClip.loop(Integer.MAX_VALUE);
		}
	}
	
	public static void outOfFocus()
	{
		if(outOfFocus == true)
		{
			return;
		}
		
		outOfFocus = true;
		
		musicVolumeHolder -= 10;
		
		if(musicVolumeHolder < -80)
		{
			musicVolume = -80;
		}
		else
		{
			musicVolume = musicVolumeHolder;
		}
		
		if(currIntroClip != null)
		{
			FloatControl volume = (FloatControl) currIntroClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
		else if(currLoopClip != null)
		{
			FloatControl volume = (FloatControl) currLoopClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
	}
	
	public static void returnFocus()
	{
		if(outOfFocus == false)
		{
			return;
		}
		
		outOfFocus = false;
		
		musicVolumeHolder += 10;

		if(musicVolumeHolder < -80)
		{
			musicVolume = -80;
		}
		else
		{
			musicVolume = musicVolumeHolder;
		}
		
		if(currIntroClip != null)
		{
			FloatControl volume = (FloatControl) currIntroClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
		else if(currLoopClip != null)
		{
			FloatControl volume = (FloatControl) currLoopClip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
	}
	
	private static LineListener lineListener = new LineListener()
	{	
		@Override
		public void update(LineEvent event)
		{
			// TODO Auto-generated method stub
			
		}
	};
}
