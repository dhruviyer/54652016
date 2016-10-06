package org.usfirst.frc.team5465.robot.v3.sept24,2016.UDPStuff;
import java.io.*;
import java.net.*;

public class UDPThreadMain {

	public static void main(String[] args) throws UnknownHostException 
	{
		UDPThread a = new UDPThread("UDP Thread", 5465, 1024);
		a.start();
		
		System.out.println("starting shit");
		while(true)
		{
			System.out.println(a.getString());
		}
		

	}

}
