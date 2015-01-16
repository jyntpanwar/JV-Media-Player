package jvmediaplayer;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jlgui.basicplayer.BasicPlayer;
import java.io.*;
import java.net.*;
import javax.swing.text.rtf.*;

public class JVPlayerGUI extends JVPlayer implements ActionListener,ChangeListener,Runnable
{
	JFrame mainFrame;
	JPanel cardPanel;
	JPanel firstp,secondp,thirdp,playlistButtons;
	JPanel buttonp;
	JButton b1,b2,b3,buttonAdd,buttonRemove,buttonClear;
	JProgressBar proBar;
	JSlider volumeSlider;
	JLabel play,next,prev,fastfwd,fastbwd,infoalbum,songStart,songEnd,volumeLevel,musicArt;
	JMenuBar mainMenuBar;
	JMenu menu1, menu2,menu3;
	JMenuItem item1,openFile,openUrl,item4,exit,playPauseMenu,stopMenu,seekForwardMenu,seekBackwardMenu,nextMenu,prevMenu,helpMenu,aboutUs;
	ScrollPane scrollList;
	Font albumfont,volumefont;
	Color cr,cr1,cr2,cr3,cr4;
	JTextField titleField,durationField,albumField,dateField,authorField,genreField,trackField,bitrateField,titleField1,durationField1,albumField1,dateField1,authorField1,genreField1,trackField1,bitrateField1;
	CardLayout clayout;
	JFileChooser open,openf;
	JDialog openStream;
	JTextField urlString;
	@SuppressWarnings("rawtypes")
	JList playList;
	Icon iconplay,iconpause;
	
	URL streamURL;
	Thread progressBarUpdater;
	
	RTFEditorKit aboutjv,aboutus,abouthelp;
	FileInputStream fileaboutjv,fileaboutus,fileabouthelp;
	JEditorPane editorPanejv,editorPaneus,editorPanehelp;
	ScrollPane scrolljv,scrollus,scrollhelp;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	JVPlayerGUI()
	{
		super();
		
		
		try{
			
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e)
		{
			System.out.println("Cant get look n feel");
		}
		mainFrame = new JFrame("JV Media Player");
		mainFrame.setLayout(new BorderLayout());
		
		clayout=new CardLayout();
		cardPanel=new JPanel();
		cardPanel.setLayout(clayout);
		
		
		//setting background for first panel
		firstp=new JPanel();
		secondp=new JPanel();
		thirdp=new JPanel();
		playlistButtons=new JPanel();
		
				
		//adding colors objects
		
		cr=new Color(84,250,204);
		cr1=new Color(255,198,117);
		cr2=new Color(213,84,250);
		cr3=new Color(211,186,86);
		cr4=new Color(182,211,86);
		firstp.setBackground(cr);
		secondp.setBackground(cr1);
		thirdp.setBackground(cr2);
		
		
		
		//adding media controls to the first panel ,fourth panel for controls fifth for progress bar sixth panel for info
		
		prev=new JLabel(new ImageIcon(getClass().getResource("/prev.png")));
		play=new JLabel(new ImageIcon(getClass().getResource("/play.png")));
		next=new JLabel(new ImageIcon(getClass().getResource("/next.png")));
		fastfwd=new JLabel(new ImageIcon(getClass().getResource("/fastfwd.png")));
		fastbwd=new JLabel(new ImageIcon(getClass().getResource("/fastbwd.png")));
		firstp.setLayout(null);
		
		//adding buttons to playlistbuttons and panels to the second panel
		secondp.setLayout(new BorderLayout());
		scrollList=new ScrollPane();
		buttonAdd=new JButton("Add File");
		buttonRemove=new JButton("Remove file");
		buttonClear=new JButton("Clear Playlist");
		playlistButtons.setLayout(new GridLayout(1,3));
		playlistButtons.setOpaque(false);
		playlistButtons.add(buttonAdd);
		playlistButtons.add(buttonRemove);
		playlistButtons.add(buttonClear);
		scrollList.setVisible(true);
		secondp.add(playlistButtons,BorderLayout.NORTH);
		secondp.add(scrollList,BorderLayout.CENTER);
		scrollList.setBackground(cr2);
		
		
		//adding elements to the first panel
		proBar=new JProgressBar();
		songStart=new JLabel("0.00");
		songEnd=new JLabel("0.00");
		volumeLevel=new JLabel("Volume");
		volumeSlider=new JSlider(0,10,10);
		volumeSlider.addChangeListener(this);
		infoalbum=new JLabel("JV Media Player");
		musicArt=new JLabel(new ImageIcon(getClass().getResource("/musicArt.png")));
		
		
		//font implementation
		albumfont=new Font("Segoe UI",Font.PLAIN,30);
		volumefont=new Font("Segoe UI",Font.PLAIN,13);
		
		infoalbum.setFont(albumfont);
		songStart.setFont(volumefont);
		songEnd.setFont(volumefont);
		volumeLevel.setFont(volumefont);
		
		volumeSlider.setOpaque(false);
		
		titleField=new JTextField("Title         :",65);
		durationField=new JTextField("Duration  :",65);
		albumField=new JTextField("Album     :",65);
		dateField=new JTextField("Date        : ",65);
		authorField=new JTextField("Author     : ",65);
		bitrateField=new JTextField("Bitrate     : ",65);
		genreField=new JTextField("Genre      : ",65);
		trackField=new JTextField("Track       : ",65);
		
		titleField.setOpaque(false);
		durationField.setOpaque(false);
		authorField.setOpaque(false);
		trackField.setOpaque(false);
		dateField.setOpaque(false);
		albumField.setOpaque(false);
		bitrateField.setOpaque(false);
		genreField.setOpaque(false);

		authorField.setFont(volumefont);
		durationField.setFont(volumefont);
		titleField.setFont(volumefont);
		albumField.setFont(volumefont);
		dateField.setFont(volumefont);
		bitrateField.setFont(volumefont);
		genreField.setFont(volumefont);
		trackField.setFont(volumefont);
		
		titleField.setBorder(null);
		durationField.setBorder(null);
		albumField.setBorder(null);
		dateField.setBorder(null);
		authorField.setBorder(null);
		bitrateField.setBorder(null);
		genreField.setBorder(null);
		trackField.setBorder(null);

		titleField.setEditable(false);
		trackField.setEditable(false);
		authorField.setEditable(false);
		albumField.setEditable(false);
		dateField.setEditable(false);
		durationField.setEditable(false);
		bitrateField.setEditable(false);
		genreField.setEditable(false);
		
		titleField.setBounds(360, 50, 140, 25);
		trackField.setBounds(360, 75, 140, 25);
		authorField.setBounds(360,100, 140, 25);
		genreField.setBounds(360, 125, 140, 25);
		albumField.setBounds(360, 150, 140, 25);
		durationField.setBounds(360,175, 140, 25);
		dateField.setBounds(360,200,140, 25);
		bitrateField.setBounds(360,225, 140, 25);
		
		
		fastfwd.setBounds(480, 340, 120, 120);
		fastbwd.setBounds(220, 340, 120, 120);
		next.setBounds(610, 340, 120, 120);
		play.setBounds(350, 340, 120, 120);
		prev.setBounds(90, 340, 120, 120);
		songStart.setBounds(170, 270,60, 25);
		proBar.setBounds(215,270,385,25);
		songEnd.setBounds(610, 270, 60, 25);
		volumeSlider.setBounds(660, 270, 120, 25);
		volumeLevel.setBounds(698, 240, 80, 25);
		infoalbum.setBounds(310, 0,580, 40);
		musicArt.setBounds(80, 5, 300, 300);
		//title.setBounds(150, 20, 60, 30);
		firstp.add(next);
		firstp.add(play);
		firstp.add(prev);
		firstp.add(songStart);
		firstp.add(proBar);
		firstp.add(songEnd);
		firstp.add(volumeSlider);
		firstp.add(infoalbum);
		firstp.add(musicArt);
		firstp.add(volumeLevel);
		firstp.add(fastfwd);
		firstp.add(fastbwd);
		firstp.add(titleField);
		firstp.add(durationField);
		firstp.add(albumField);
		firstp.add(authorField);
		firstp.add(dateField);
		firstp.add(bitrateField);
		firstp.add(genreField);
		firstp.add(trackField);
		
		iconplay=new ImageIcon(getClass().getResource("/play.png"));
		iconpause=new ImageIcon(getClass().getResource("/pause.png"));
		
		//mouse listener to the JLabels play pause next previous and buttons for focus 
		play.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				
				if((player.getStatus() == BasicPlayer.OPENED)||(player.getStatus() == BasicPlayer.STOPPED))
				{
					play.setIcon(iconpause);
					playNPause();
					System.out.println("Now Playing");
				}
				else if(player.getStatus() == BasicPlayer.PLAYING)
				{
					play.setIcon(iconplay);
					playNPause();
				}
				else if(player.getStatus() == BasicPlayer.PAUSED)
				{
					play.setIcon(iconpause);
					playNPause();
				}
			}
		});
		next.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				next();
				System.out.print("next music");
			}
			
		});
		
		prev.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				previous();
				System.out.print("previous music");
			}
			
		});
		
		fastfwd.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				forward();
				System.out.print("previous music");
			}
			
		});
		
		fastbwd.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				backward();
				System.out.print("previous music");
			}
			
		});
		
			
		
		//adding background to the buttons 
		b1=new JButton("player");
		b2=new JButton("Playlist");
		b3=new JButton("aboutjv");
		b1=new JButton(new ImageIcon(getClass().getResource("/buttonmedh.png")));
		b2=new JButton(new ImageIcon(getClass().getResource("/buttonplist.png")));
		b3=new JButton(new ImageIcon(getClass().getResource("/buttonabout.png")));
		b1.setBorder(null);
		b2.setBorder(null);
		b3.setBorder(null);
		
		//adding buttons to the button panel
		buttonp=new JPanel();
		buttonp.setOpaque(false);
		
		//action listener on buttons
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		buttonAdd.addActionListener(this);
		buttonRemove.addActionListener(this);
		buttonClear.addActionListener(this);
		
		//adding buttons and layout
		buttonp.setLayout(new GridLayout(1,5));
		buttonp.add(b1);
		buttonp.add(b2);
		buttonp.add(b3);
		mainFrame.add(buttonp,BorderLayout.SOUTH);
		
		//adding the menu bar
		mainMenuBar = new JMenuBar();
		mainFrame.add(mainMenuBar,BorderLayout.NORTH);
		menu1 = new JMenu("File");
		menu2 = new JMenu("Audio");
		menu3=  new JMenu("Help");
		openFile=new JMenuItem("Open File(s)");
		openUrl=new JMenuItem("Open URL");
		exit=new JMenuItem("exit");
		playPauseMenu=new JMenuItem("Play / Pause");
		stopMenu=new JMenuItem("Stop");
		seekForwardMenu=new JMenuItem("Seek Forward  +10(s)");
		seekBackwardMenu=new JMenuItem("Seek Backward  +10(s)");
		nextMenu=new JMenuItem("Next Song");
		prevMenu=new JMenuItem("Previous Song");
		helpMenu=new JMenuItem("Help");
		aboutUs=new JMenuItem("About us");
		
		openFile.addActionListener(this);
		openUrl.addActionListener(this);
		exit.addActionListener(this);
		playPauseMenu.addActionListener(this);
		stopMenu.addActionListener(this);
		seekForwardMenu.addActionListener(this);
		seekBackwardMenu.addActionListener(this);
		nextMenu.addActionListener(this);
		prevMenu.addActionListener(this);
		helpMenu.addActionListener(this);
		aboutUs.addActionListener(this);
		menu1.add(openFile);
		menu1.add(openUrl);
		menu1.add(exit);
		menu2.add(playPauseMenu);
		menu2.add(stopMenu);
		menu2.add(seekForwardMenu);
		menu2.add(seekBackwardMenu);
		menu2.add(nextMenu);
		menu2.add(prevMenu);
		menu3.add(helpMenu);
		menu3.add(aboutUs);
		mainMenuBar.add(menu1);
		mainMenuBar.add(menu2);
		mainMenuBar.add(menu3);
		
		//adding the panel to the cardlayout
		mainFrame.add(cardPanel,BorderLayout.CENTER);
		cardPanel.add(firstp,"f1");
		cardPanel.add(secondp,"f2");
		cardPanel.add(thirdp,"f3");
		
		playList = new JList(songList.toArray());
		playList.setBackground(cr1);
		playList.setLayout(new FlowLayout());
		
		playList.addMouseListener(new MouseAdapter() {
			@Override
		    public void mouseClicked(MouseEvent evt)
			{
				//event occurs on Double Click
		        if (evt.getClickCount() == 2)
		        {
		        	String selectedSong = playList.getSelectedValue().toString();
		        	playSelected(selectedSong);
		        	play.setIcon(iconpause);
		        }
		    }
		});
		
		
		//add playlist to scroll pane
		scrollList.add(playList);
		
		open = new JFileChooser();
		openf=new JFileChooser();
		FileNameExtensionFilter fnef = new FileNameExtensionFilter("MP3 Audio","mp3");
		open.setFileFilter(fnef);
		openf.setFileFilter(fnef);
		open.addActionListener(this);
		openf.addActionListener(this);
		openf.setMultiSelectionEnabled(true);
		
		songInfo();
		
		aboutjv=new  RTFEditorKit();
		aboutus=new RTFEditorKit();
		abouthelp=new RTFEditorKit();
		
		editorPanejv=new JEditorPane();
		editorPaneus=new JEditorPane();
		editorPanehelp=new JEditorPane();
		 
		
		editorPanejv.setEditable(false);
		editorPanejv.setBackground(cr2);
			
		editorPaneus.setEditable(false);
		editorPaneus.setBackground(cr3);

		editorPanehelp.setEditable(false);
		editorPanehelp.setBackground(cr4);
		
		editorPanejv.setEditorKit(aboutjv);
		editorPaneus.setEditorKit(aboutus);
		editorPanehelp.setEditorKit(abouthelp);
		editorPanejv.setAutoscrolls(true);
						
		
			
			try
			{
				aboutjv.read(getClass().getResource("/aboutjv.rtf").openStream(), editorPanejv.getDocument(),100);
				aboutus.read(getClass().getResource("/aboutus.rtf").openStream(),editorPaneus.getDocument(),50);
				abouthelp.read(getClass().getResource("/abouthelp.rtf").openStream(), editorPanehelp.getDocument(), 50);
			}
			catch(Exception e)
			{
				System.out.println("Exception occured : "+e);
			}
			
			
			scrolljv=new ScrollPane();
			scrollus=new ScrollPane();
			scrollhelp=new ScrollPane();
		
			thirdp.setLayout(new BorderLayout());
			
			scrolljv.add(editorPanejv);
			thirdp.add(scrolljv,BorderLayout.CENTER);
			scrollus.add(editorPaneus);
			scrollhelp.add(editorPanehelp);
			
			
			
			
		//jframe setting
		mainFrame.setSize(800,600);
		mainFrame.setSize(800,600);
		mainFrame.setResizable(false);
		mainFrame.repaint();
		mainFrame.setVisible(true);
		mainFrame.setTitle("JV Media Player");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Thread updating the Player GUI
		progressBarUpdater = new Thread(this,"update progress bar");
		progressBarUpdater.setPriority(Thread.MIN_PRIORITY);
		progressBarUpdater.start();
		
			
	}//constructor closed
	
	public void run()
	{
		while(true)
		{
			songStart.setText(currentMinutes+":"+currentSeconds);
			songEnd.setText(totalMinutes+":"+totalSeconds);
			proBar.setMaximum(duration);
			proBar.setValue(position);
			changeSongInfo();
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}
	
	public void changeSongInfo()
	{
		titleField1.setText(title);
		albumField1.setText(album);
		trackField1.setText(track);
		authorField1.setText(author);
		dateField1.setText(date);
		bitrateField1.setText(Integer.toString(bitrate/1000)+" kbps");
		genreField1.setText(genre);
		durationField1.setText(totalMinutes+":"+totalSeconds);
		firstp.repaint();
	}
	
	//code to song info display
	public void songInfo()
	{
		
		
		
		titleField1=new JTextField("",200);
		durationField1=new JTextField("",200);
		albumField1=new JTextField("",200);
		dateField1=new JTextField("",200);
		authorField1=new JTextField("",200);
		bitrateField1=new JTextField("",200);
		genreField1=new JTextField("",200);
		trackField1=new JTextField("",200);
		
		
		
		
		authorField1.setFont(volumefont);
		durationField1.setFont(volumefont);
		titleField1.setFont(volumefont);
		albumField1.setFont(volumefont);
		dateField1.setFont(volumefont);
		bitrateField1.setFont(volumefont);
		genreField1.setFont(volumefont);
		trackField1.setFont(volumefont);
		
		
		
		titleField1.setOpaque(false);
		durationField1.setOpaque(false);
		authorField1.setOpaque(false);
		trackField1.setOpaque(false);
		dateField1.setOpaque(false);
		albumField1.setOpaque(false);
		bitrateField1.setOpaque(false);
		genreField1.setOpaque(false);
		
		
		titleField1.setBorder(null);
		durationField1.setBorder(null);
		albumField1.setBorder(null);
		dateField1.setBorder(null);
		authorField1.setBorder(null);
		bitrateField1.setBorder(null);
		genreField1.setBorder(null);
		trackField1.setBorder(null);
		
		
		
		titleField1.setEditable(false);
		trackField1.setEditable(false);
		authorField1.setEditable(false);
		albumField1.setEditable(false);
		dateField1.setEditable(false);
		durationField1.setEditable(false);
		bitrateField1.setEditable(false);
		genreField1.setEditable(false);
		
		
		
		titleField1.setBounds(450, 50, 400, 25);
		trackField1.setBounds(450, 75, 400, 25);
		authorField1.setBounds(450,100, 400, 25);
		genreField1.setBounds(450, 125, 400, 25);
		albumField1.setBounds(450, 150, 400, 25);
		durationField1.setBounds(450,175, 400, 25);
		dateField1.setBounds(450,200,400, 25);
		bitrateField1.setBounds(450,225, 400, 25);
			
			
			firstp.add(titleField1);
			firstp.add(durationField1);
			firstp.add(albumField1);
			firstp.add(authorField1);
			firstp.add(dateField1);
			firstp.add(bitrateField1);
			firstp.add(genreField1);
			firstp.add(trackField1);
				
	}
	
	//code to change the buttons focus
	public void iconChanged()
	{
		b1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if(e.getSource()==b1)
				{
					Icon iconplayer=new ImageIcon(getClass().getResource("/buttonmedh.png"));
					Icon iconplaylist=new ImageIcon(getClass().getResource("/buttonplist.png"));
					Icon iconabout=new ImageIcon(getClass().getResource("/buttonabout.png"));
					b1.setIcon(iconplayer);
					b2.setIcon(iconplaylist);
					b3.setIcon(iconabout);
				}
				
			}
		});
		b2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if(e.getSource()==b2)
				{
					Icon iconplayer=new ImageIcon(getClass().getResource("/buttonmed.png"));
					Icon iconplaylist=new ImageIcon(getClass().getResource("/buttonplisth.png"));
					Icon iconabout=new ImageIcon(getClass().getResource("/buttonabout.png"));
					b1.setIcon(iconplayer);
					b2.setIcon(iconplaylist);
					b3.setIcon(iconabout);
				}
				
			}
		});
		b3.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if(e.getSource()==b3)
				{
					Icon iconplayer=new ImageIcon(getClass().getResource("/buttonmed.png"));
					Icon iconplaylist=new ImageIcon(getClass().getResource("/buttonplist.png"));
					Icon iconabout=new ImageIcon(getClass().getResource("/buttonabouth.png"));
					b1.setIcon(iconplayer);
					b2.setIcon(iconplaylist);
					b3.setIcon(iconabout);
				}
				
			}
		});
	}
	
	public void dialogStream()
	{
		try
		{
			
			openStream=new JDialog();
			
			openStream.setLayout(new FlowLayout());
			openStream.setTitle("JV Online Music Stream");
			JLabel labels=new JLabel("Enter URL - BETA feature may not work");
			JButton addurl=new JButton("Add url");
			urlString=new JTextField(100);
			openStream.setMinimumSize(new Dimension(1024,73));
			openStream.setMaximumSize(new Dimension(1024,73));
			urlString.setText("Enter url for Stream Playback");
			urlString.setEditable(true);
			openStream.setBackground(Color.BLACK);
			openStream.add(labels);
			openStream.add(urlString);
			openStream.add(addurl);
			openStream.setSize(450,300);
			openStream.setFocusable(true);
			openStream.setLocationRelativeTo(mainFrame.getRootPane());
			openStream.setVisible(true);
			openStream.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			addurl.addMouseListener(new MouseAdapter()
			{
				public void mousePressed(MouseEvent e)
				{
					try
					{
						streamURL = new URL(urlString.getText());
						openURL(streamURL);
					}
					catch(Exception ue)
					{
						System.out.println("please enter a valid url...!!");
					}
				}
			});
		}
		catch(Exception e)
		{
			System.out.print("error occured while getting the url"+e);
		}
		
	}
	
	public void dialogHelp()
	{
		try
		{
			
			JDialog help=new JDialog();
			help.setLayout(new BorderLayout());
			help.setTitle("JV Help");
			help.add(scrollhelp,BorderLayout.CENTER);
			scrollhelp.add(editorPanehelp);
			help.getContentPane().add(scrollhelp,BorderLayout.CENTER);
			help.setSize(500, 400);			
			help.setLocationRelativeTo(mainFrame.getRootPane());
			help.setVisible(true);
			help.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		catch(Exception e)
		{
			System.out.print("unexpected error occured "+e);
		}
		
	}
	public void dialogAbout()
	{
		try
		{
			
			JDialog about=new JDialog();
			about.setLayout(new BorderLayout());
			about.setTitle("JV about");
			about.add(scrollus,BorderLayout.CENTER);
			scrollus.add(editorPaneus);
			about.getContentPane().add(scrollus,BorderLayout.CENTER);
			about.setSize(600, 400);			
			about.setLocationRelativeTo(mainFrame.getRootPane());
			about.setVisible(true);
			about.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
		catch(Exception e)
		{
			System.out.print("unexpected error occured "+e);
		}
	}
		
		

		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e)
		{
			
			if(e.getSource()==openFile)
			{
				openf.showOpenDialog(mainFrame.getParent());
				inputs = openf.getSelectedFiles();
				createPlaylist();
				playList.setListData(songList.toArray());
				playList.repaint();
				scrollList.repaint();
			}
			if(e.getSource()==openUrl)
			{
				
				dialogStream();
				
			}

			if(e.getSource()==exit)
			{
				
				System.exit(JFrame.EXIT_ON_CLOSE);
				
			}
			if(e.getSource()==playPauseMenu)
			{
				if((player.getStatus() == BasicPlayer.OPENED)||(player.getStatus() == BasicPlayer.STOPPED))
				{
					play.setIcon(iconpause);
					playNPause();
					System.out.println("Now Playing");
				}
				else if(player.getStatus() == BasicPlayer.PLAYING)
				{
					play.setIcon(iconplay);
					playNPause();
				}
				else if(player.getStatus() == BasicPlayer.PAUSED)
				{
					play.setIcon(iconpause);
					playNPause();
				}
			}
			if(e.getSource()==stopMenu)
			{
				stop();				
			}
			if(e.getSource() == nextMenu)
			{
				next();
			}
			if(e.getSource() == prevMenu)
			{
				previous();
			}
			if(e.getSource() == seekForwardMenu)
			{
				forward();
			}
			if(e.getSource() == seekBackwardMenu)
			{
				backward();
			}
			if(e.getSource()==helpMenu)
			{
				
				dialogHelp();
				
			}
			if(e.getSource()==aboutUs)
			{
				
				dialogAbout();
				
			}
			
			if(e.getSource()==b1)
			{
				mainMenuBar.setVisible(true);
				iconChanged();
				clayout.show(cardPanel, "f1");
			}
			else if(e.getSource()==b2)
			{
				mainMenuBar.setVisible(false);
				iconChanged();
				clayout.show(cardPanel, "f2");
			}
			else if(e.getSource()==b3)
			{
				mainMenuBar.setVisible(false);
				iconChanged();
				clayout.show(cardPanel, "f3");
			}
			else if(e.getSource()==buttonAdd)
			{
				openf.showOpenDialog(mainFrame.getParent());
				inputs = openf.getSelectedFiles();
				createPlaylist();
				playList.setListData(songList.toArray());
				playList.repaint();
				scrollList.repaint();
				
				System.out.print((inputs.length)+" file(s) added");
			}
			else if(e.getSource()==buttonRemove)
			{
				removeSelected(playList.getSelectedValue().toString());
				playList.setListData(songList.toArray());
				playList.repaint();
				scrollList.repaint();
				System.out.print("removed file");
			}
			else if(e.getSource()==buttonClear)
			{
				clearPlaylist();
				playList.setListData(songList.toArray());
				playList.repaint();
				scrollList.repaint();
				System.out.print("cleared playlist");
			}
		}
		
		//Controls volume 
		public void stateChanged(ChangeEvent e)
		{
			try
			{
				control.setGain(((double)volumeSlider.getValue())/10);
			}
			catch(Exception ve)
			{
				System.out.println(ve);
			}
		}
	}

