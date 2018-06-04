package core;

import system.Audio;
import system.PrefsHandler;
import system.Time;
import system.Audio.Tracks;
import utils.ObjectCollection;

public class Main
{
	public static void main(String[] args) 
	{
		new PrefsHandler().importPrefs();
		new Time();//this sets up the time
		Audio.openClips(new Tracks[] {Tracks.BGM2});
		ObjectCollection.getRenderer().start();
		ObjectCollection.getMainMenu().setInMainMenu(true);
	}
}
