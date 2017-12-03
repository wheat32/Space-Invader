package system;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

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

import utils.ConstantValues;

public class Audio implements LineListener, ConstantValues
{
	private Clip BGM;
	private Clip BGM2;
	private TrackNames BGMName;
	
	private boolean outOfFocus = false;
	private short musicVolume = -80;//anywhere between -80-5 for the volume
	private short musicVolumeHolder = musicVolume;
	private short sfxVolume = -10;
	
	private final String[] trackResIDs = {"bgm/bgm1_intro.wav", "bgm/bgm1_loop.wav", "bgm/bgm2_intro.wav", "bgm/bgm2_loop.wav", "bgm/bigWin1_intro.wav",
			"bgm/bigWin1_loop.wav", "bgm/bigWin2_intro.wav", "bgm/bigWin2_loop.wav", "bgm/bossBGM1.wav", "bgm/lose.wav"};
	private HashMap<TrackNames, Clip> tracks = new HashMap<TrackNames, Clip>();
	
	private final String[] sfxResIDs = {"sfx/beep1.wav", "sfx/enemyShoot.wav", "sfx/friendlyShoot.wav", "sfx/gunOverheat.wav", "sfx/hit.wav",
			"sfx/menuBadSelect.wav", "sfx/menuMovement.wav", "sfx/seismicBoom.wav", "sfx/shieldRicochet.wav"};
	private HashMap<SfxNames, Clip> sfxs = new HashMap<SfxNames, Clip>();
	
	public Audio()
	{
		//Open all the clips initially
		try
		{
			for(int i = 0; i < TrackNames.values().length; i++)
			{
				URL url = this.getClass().getClassLoader().getResource(trackResIDs[i]);
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				AudioFormat format = audioInputStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				if(!AudioSystem.isLineSupported(info))
				{
					new LineUnavailableException("Line is not supported!");
				}
				
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioInputStream);
				FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(musicVolume);
				clip.start();
				clip.stop();
				clip.setFramePosition(0);
				
				tracks.put(TrackNames.values()[i], clip);
			}
			
			for(int i = 0; i < SfxNames.values().length; i++)
			{
				URL url = this.getClass().getClassLoader().getResource(sfxResIDs[i]);
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
				AudioFormat format = audioInputStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				if(!AudioSystem.isLineSupported(info))
				{
					new LineUnavailableException("Line is not supported!");
				}
				
				Clip clip = (Clip) AudioSystem.getLine(info);
				clip.open(audioInputStream);
				
				sfxs.put(SfxNames.values()[i], clip);
			}
			
			BGM = null;
			BGM2 = null;
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
	}
	
	public void playSound(SfxNames sfxName)
	{
		try
		{	
			Clip clip = sfxs.get(sfxName);
			clip.setMicrosecondPosition(0);
			clip.addLineListener(this);	
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(sfxVolume);
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
	
	public void changeTrack(TrackNames introName, TrackNames loopName)
	{
		if(BGMName != null && BGMName.equals(introName) == true)
		{
			return;
		}
		
		BGMName = introName;
		if(BGM != null)
		{
			BGM.stop();
			BGM.setFramePosition(0);
		}
		if(BGM2 != null)
		{
			BGM2.stop();
			BGM2.setFramePosition(0);
		}
		
		try
		{
			BGM = tracks.get(introName);
			BGM.addLineListener(this);
			FloatControl volume = (FloatControl) BGM.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
			BGM.setFramePosition(0);
			BGM.start();
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//Operates the loop
		if(loopName != null)
		{		
			try
			{
				BGM2 = tracks.get(loopName);
				BGM2.addLineListener(this);	
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
					while(BGM.getFrameLength() >= BGM.getFramePosition() + 1200 && BGM.isRunning() == true)
					{
						Thread.yield();
					}
					//System.out.println(BGM.getFramePosition());
					if(BGMName == introName)
					{
						FloatControl volume = (FloatControl) BGM2.getControl(FloatControl.Type.MASTER_GAIN);
						volume.setValue(musicVolume);
						BGM2.setFramePosition(0);
						BGM2.start();
						BGM2.loop(Integer.MAX_VALUE);
						BGM.stop();
					}
				}
			};
			new Thread(runnable).start();
		}
		else
		{
			BGM2 = null;
			BGM.loop(Integer.MAX_VALUE);
		}
	}
	
	public TrackNames getCurrBGM()
	{
		return BGMName;
	}
	
	public void outOfFocus()
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
		
		if(BGM != null)
		{
			FloatControl volume = (FloatControl) BGM.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
		else if(BGM2 != null)
		{
			FloatControl volume = (FloatControl) BGM2.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
	}
	
	public void returnFocus()
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
		
		if(BGM != null)
		{
			FloatControl volume = (FloatControl) BGM.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
		else if(BGM2 != null)
		{
			FloatControl volume = (FloatControl) BGM2.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(musicVolume);
		}
	}
	
	@Override
	public void update(LineEvent event) 
	{
		LineEvent.Type type = event.getType();
		if(type == LineEvent.Type.START)
		{
			
		}
		if(type == LineEvent.Type.STOP)
		{
			
		}
	}
}
