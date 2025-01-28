package SurlyPackage;

import java.io.*;
import java.net.URL;
import java.applet.*;
import java.util.*;
import javax.sound.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import SurlyPackage.Parser;

public class Main {

	public static void main(String[] args) {
		////////////////Get surly files/////////////////// 
		
		File Start = new File("Sounds/StartSound`.WAV");
		File Error = new File("Sounds/Error`.WAV");
		File Success = new File("Sounds/Success2`.WAV");
		File Theme = new File("Sounds/Theme`.WAV");
		
		String name = "Surly";
		Parser parse = new Parser();
		Database DB = new Database(name);
		
		try {
		File inputFile = new File(args[0]);
		Scanner scanner = new Scanner(inputFile);
		 	while(scanner.hasNext())
		 	{
		 		String line = scanner.nextLine();
		 		parse.Parse(line,DB);
		 	}
		 	scanner.close();
 
		} 
		catch(FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		AsciiArt a = new AsciiArt();
		System.out.println(a.title);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("\nWelcome to Surly II!");
		PlaySound(Start);
		LoopSound(Theme);
		boolean loop = true;
		while(loop) //input loop
		{
			System.out.println("\n\nPlease select an option: \n \t 1) Run Frequently Ran Queries \n \t 2) Enter Manual Query \n \t 0) Quit \n\t Type 'HELP' for help menu \n->");
			
			String n = sc.nextLine();
			n = n.toUpperCase();
			switch (n)
			{
			case "1":
				System.out.println("POPULAR QUERIES \n1) Show names and location of all staff \n2) Show courses equal to CSCI241 OR CSCI305 AND prereq = CSCI241 \n3) Join course and prereq based on CNUM");
				String m = sc.nextLine();
				switch (m)
				{
				case "1":
				{
					parse.Parse("P = PROJECT NAME, CAMPUSADDR FROM STAFF", DB);
					PlaySound(Success);
					break;
				}
				case "2":
				{
					parse.Parse("S = SELECT PREREQ WHERE CID = CSCI241 OR CID = CSCI305 AND PNUM = CSCI241", DB);
					PlaySound(Success);
					break;
				}
				case "3":
				{
					parse.Parse("J = JOIN COURSE, PREREQ ON CNUM = PNUM", DB);
					PlaySound(Success);
					break;
				}
				
				default:
				{
					System.out.println("Non valid entry");
					PlaySound(Error);
					break;
				}
				}
				break;
			case "2":
				System.out.print("Insert Query Here -->");
				String command = sc.nextLine();
				command = command.toUpperCase();
				if(command.equals("EXECUTE ORDER 66") || command.equals("E66"))
					{
						System.out.println("It is done Lord Vader!");
						command = "DELETE STAFF WHERE EXTENSION = 0501 AND NAME != SKYWALKER";
					}
				parse.Parse(command, DB);
				PlaySound(Success);
				break;
				
			case "0":
				System.out.println("Are you sure? ->");
				String input = sc.nextLine();
				input = input.toUpperCase();
				
				switch (input)
				{
					case "YES":
					case "Y":
						loop = false;
						System.out.println("Bye Bye!");
						break;
					default:
						break;
				}

				break;
				
			case "H":
			case "HELP":
                System.out.println("Help Menu");
                System.out.println("Destroy Syntax: DESTROY relationname;");
                System.out.println("\t ex. D = DESTROY COURSE");
                System.out.println("Delete Where Syntax: DELETE relationname WHERE <conditions>;");
                System.out.println("\t ex. D = DELETE COURSE WHERE CNUM = CSCI241");
                System.out.println("Select Where Syntax: SELECT relationname WHERE <conditions>;");
                System.out.println("\t ex. S = SELECT COURSE WHERE CNUM = CSCI241");
                System.out.println("Project Syntax: PROJECT <attribute names> FROM relationname;");
                System.out.println("\t ex. P = PROJECT CREDITS, CNUM FROM COURSE");
                System.out.println("Join Syntax: JOIN relationAname, relationBname on <join condition>;");
                System.out.println("\t ex. J = JOIN COURSE, PREREQ ON CNUM = PNUM;");
                break;
				
			default:
				System.out.println("Please type 'Help' if you need assistance!");
			}
		}
		
		sc.close();
	}

	static void PlaySound(File Sound)
	{
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Sound));
			clip.start();
				
			Thread.sleep(clip.getMicrosecondLength()/1000);
		}catch(Exception e)
		{
			
		}
	}
	
	static void LoopSound(File Sound)
	{
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(Sound));
			clip.loop(10);
				
			//Thread.sleep(clip.getMicrosecondLength()/1000);
		}catch(Exception e)
		{
			
		}
	}
}
