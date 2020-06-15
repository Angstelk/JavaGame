import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.File;
import java.io.*;
import java.nio.file.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class Menu extends JFrame implements ActionListener 
{
	public String name;
	static Long currentFrame; 
	static Clip clip; 
	
	// current status of clip 
        static	String status; 
	
        static 	AudioInputStream audioInputStream; 
	
	static String filePath; 


public static void main(String[] args)
		throws UnsupportedAudioFileException, 
		IOException, LineUnavailableException{ 
	     	try{
		// create AudioInputStream object 
		audioInputStream = 
				AudioSystem.getAudioInputStream(new File("Round.wav").getAbsoluteFile()); 
		
		// create clip reference 
		clip = AudioSystem.getClip(); 
		
		// open audioInputStream to the clip 
		clip.open(audioInputStream); 
		
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
		}
		catch(UnsupportedAudioFileException e){
			    throw new UnsupportedAudioFileException("Invalid operation for sorted list.");

		   }


		try
		{ 
			filePath = "Round.wav"; 
			SimpleAudioPlayer audioPlayer = 
							new SimpleAudioPlayer(); 
			
			audioPlayer.play(); 
			
		} 
		
		catch (Exception ex) 
		{ 
			System.out.println("Error with playing sound."); 
			ex.printStackTrace(); 
		
		} 
    JFrame frame = new JFrame("Kaprysny Ptaszek");
    Menu menu = new Menu();
    JButton b1 = new JButton();
    b1.addActionListener(menu);
    b1.setActionCommand("NGame");

    JButton b2 = new JButton();
    b2.addActionListener(menu);
    b2.setActionCommand("Score");
     
    JButton b3 = new JButton();
    b3.addActionListener(menu);
    b3.setActionCommand("Exit");
    
    b1.setPreferredSize(new Dimension(100,100));
    b1.setVisible(true);
    b1.setText("New Game");
  
   b1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String name = JOptionPane.showInputDialog(frame,
                        "Your nick : ", null);

    		System.out.println(name);
		game.Game.run(name);
            }
        });	




    b2.setPreferredSize(new Dimension(100,100));
    b2.setVisible(true);
    b2.setText("Score");

   b2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String name = JOptionPane.showInputDialog(frame,
                        "Pass nick of player that  max score you want to know : ", null);

    		System.out.println(" Max score of " + name +" is :");
		try{
		printSC(name);
		 }
		catch (Exception ex) 
		{ 
			System.out.println("Cant find player"); 
		
		} 
        }} );	

    b3.setPreferredSize(new Dimension(100,100));
    b3.setVisible(true);
    b3.setText("Exit");

    Container C = frame.getContentPane();
    C.setLayout(new GridLayout(3, 1));
    C.add(b3);	
    C.add(b2);
    C.add(b1);
    C.setSize(500,500);
    C.setVisible(true);	    
    C.setBackground(Color.RED);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    
	
    frame.setPreferredSize(new Dimension(400,400));
    frame.add(b1);
    frame.add(b2);
    frame.add(b3);

    frame.setVisible(true);
    frame.pack();
	while(true){}
}

public static void printSC(String name ) throws IOException, FileNotFoundException  
{

		File file = new File(name);

  		BufferedReader br = new BufferedReader(new FileReader(file));
  		String st;
  		while ((st = br.readLine()) != null)
    		System.out.println(st);
}

 @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);

    }
 @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("NGame")) {

        }
       	else if (command.equals("Score")) {
        }

       	else if (command.equals("Exit")) {
	    System.exit(0);
        }
    }

}	
