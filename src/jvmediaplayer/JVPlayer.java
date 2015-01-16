package jvmediaplayer;


import javazoom.jlgui.basicplayer.*;

import java.util.*;
import java.io.*;
import java.util.Map;
import java.net.*;


public class JVPlayer implements BasicPlayerListener {

	BasicPlayer player;
	BasicPlayerEvent bpe;
    File[] inputs;
    ArrayList<File> fileList;
    ArrayList<String> songList;
  
   	BasicController control;
   	
   	int songSequence = 0;
   	
   	int duration = 0;
   	int position = 0;
   	long currentByte;
   	long mp3FrameSize;
   	int currentMinutes,currentSeconds;
   	int totalMinutes,totalSeconds;
   	
   	String title;
   	String album;
   	String author;
   	String date;
   	String genre;
   	String track;
   	int bitrate;

    public JVPlayer()
    {
    	fileList = new ArrayList<File>();	
		songList = new ArrayList<String>();
        createPlayer();     
     }
    
    //Creates a new player

	void createPlayer()
	{
		try
		{
			player = new BasicPlayer();
			control = (BasicController)player;
			player.addBasicPlayerListener(this);
			if(fileList.isEmpty() == false)
			{
				control.open(fileList.get(songSequence));
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	//Implements play , pause and resume functionality
	
	public void playNPause()
	{
		try
		{
			if((player.getStatus() == BasicPlayer.OPENED)||(player.getStatus() == BasicPlayer.STOPPED))
			{
				control.play();
				//play.setText("Pause");
			}
			else if(player.getStatus() == BasicPlayer.PLAYING)
			{
				control.pause();
				//play.setText("Play");
			}
			else if(player.getStatus() == BasicPlayer.PAUSED)
			{
				//play.setText("Pause");
				control.resume();
			}
			
		}
		catch(Exception pe)
		{
			System.out.println(pe);
		}
	}
	
	//Stops current track and closes the line
	
	public void stop()
	{
		try
		{
			control.stop();
		}
		catch(Exception se)
		{
			se.printStackTrace();
		}
	}
	
	//skips to next song
	
	public void next()
	{
		try
		{
			songSequence++;
			if(songSequence<fileList.size())
			{
				control.stop();
				control.open(fileList.get(songSequence));
				control.play();
			}
			
			else
			{
				System.out.println("End of playlist");
				songSequence--;
			}
		}
		catch(Exception ne)
		{
			ne.printStackTrace();
		}
	}
	
	//skip to previous song
	
	public void previous()
	{
		try
		{
			songSequence--;
			if(songSequence >= 0)
			{
				control.stop();
				control.open(fileList.get(songSequence));
				control.play();
			}
			
			else
			{
				System.out.println("End of playlist");
				songSequence++;
			}
		}
		catch(Exception ne)
		{
			ne.printStackTrace();
		}
	}
	
	//To seek the track forward
	
	public void forward()
	{
		try
		{
			control.seek(currentByte+(mp3FrameSize*400));
		}
		catch(Exception fe)
		{
			fe.printStackTrace();
		}
	}
	
	//To seek the track backward
	
	public void backward()
	{
		try
		{
			control.seek(currentByte-(mp3FrameSize*400));
		}
		catch(Exception be)
		{
			be.printStackTrace();
		}
	}
	
	//plays the track selected by the user in the playlist
	
	public void playSelected(String ss)
	{
		try
     	{
     		//selects the song from list
     		String v = ss;
     		control.stop();
     		int index = 0;
     		//searching for the file whose name resembles the selected song
     		for(int i = 0; i<fileList.size(); i++)
     		{
     			File f = fileList.get(i);
     			if(f.getName().equals(v))
     			{
     				index = i;
     				break;
     			}
     		}
     		//updates song sequence
     		songSequence = index;
     		control.open(fileList.get(index));
     		control.play();
     	}
     	catch(Exception vce)
     	{
     		System.out.println(vce);
     	}
	}
	
	public void removeSelected(String selectedSong)
	{
		int index = 0;
		//searching for file which is needed to be removed from playlist
		for(int i = 0; i<fileList.size(); i++)
 		{
 			File f = fileList.get(i);
 			if(f.getName().equals(selectedSong))
 			{
 				index = i;
 				break;
 			}
 		}
		songList.remove(index);
		fileList.remove(index);
	}
	
	//creates and updates current playlist
	
	public void createPlaylist()
	{
		for(int i = 0; i < inputs.length; i++)
		{
			songList.add(inputs[i].getName());
			fileList.add(inputs[i]);
			System.out.println(songList);
		}
		if(player.getStatus() == BasicPlayer.UNKNOWN)
		{
			try
			{
				control.open(fileList.get(0));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//clears current playlist
	
	public void clearPlaylist()
	{
		fileList.clear();
		songList.clear();
		inputs = null;
		songSequence = 0;
	}
	
	//Special case for online streaming ... Beta Feature .. may not work
	public void openURL(URL url)
	{
		try
		{
			control.stop();
			clearPlaylist();	
			control.open(url);
			control.play();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
    
	 @SuppressWarnings("rawtypes")
	public void opened(Object stream, Map properties)
	  {
	    // Pay attention to properties. It's useful to get duration, 
	    // bitrate, channels, even tag such as ID3v2.
	   
	    duration = (int)((long)properties.get("duration"))/1000000;
	    totalMinutes = (int)(duration/60);
	    totalSeconds = duration - (totalMinutes*60);
	    title = (String)properties.get("title");
	    album = (String)properties.get("album");
	   	author = (String)properties.get("author");;
	   	date = (String)properties.get("date");;
	   	genre = (String)properties.get("mp3.id3tag.genre");;
	   	track = (String)properties.get("mp3.id3tag.track");;
	   	bitrate = (int)properties.get("mp3.bitrate.nominal.bps");;

	  }

	  /**
	   * Progress callback while playing.
	   * 
	   * This method is called severals time per seconds while playing.
	   * properties map includes audio format features such as
	   * instant bitrate, microseconds position, current frame number, ... 
	   * 
	   * @param bytesread from encoded stream.
	   * @param microseconds elapsed (<b>reseted after a seek !</b>).
	   * @param pcmdata PCM samples.
	   * @param properties audio stream parameters.
	  */
	  @SuppressWarnings("rawtypes")
	public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties)
	  {
	    // Pay attention to properties. It depends on underlying JavaSound SPI
	    // MP3SPI provides mp3.equalizer.
	 
	    position = (int)((long)properties.get("mp3.position.microseconds"))/1000000; 
		currentByte = (long)properties.get("mp3.position.byte");
		mp3FrameSize = (int)properties.get("mp3.frame.size.bytes");
		currentMinutes = (int)(position/60);
		currentSeconds = position - currentMinutes*60;
	  }

	  /**
	   * Notification callback for basicplayer events such as opened, eom ...
	   * 
	   * @param event
	   */
	  public void stateUpdated(BasicPlayerEvent event)
	  {
	    // Notification of BasicPlayer states (opened, playing, end of media, ...)
		  System.out.println("stateUpdated : "+event.toString());
		  if(event.getCode() == BasicPlayerEvent.EOM)
		  {
			  stop();
			  next();
		  }
	  }

	  /**
	   * A handle to the BasicPlayer, plugins may control the player through
	   * the controller (play, stop, ...)
	   * @param controller : a handle to the player
	   */ 
	  public void setController(BasicController controller)
	  {
	    System.out.println("setController : "+controller);
	  }
}
