package org.usfirst.frc.team5465.robot;
import java.io.*;
import java.net.*;

public class UDPThreadMain 
{
	static double focalLength = 341.7075686984592;
	static double KNOWN_WIDTH = 18;
	public static void main(String[] args) throws UnknownHostException 
	{
		//10.15.17.127 techshop local host address
		//10.143.196.142 asu 2 local host address
		UDPThread a = new UDPThread("UDP Thread", 5465, 1024);
		a.start();
		
		System.out.println("starting the server");
		
		//double[] center = new double[2];
		//double distance = 0;
		
		while(true)
		{
			if(a.getString().contains("D"))
			{
				a.updateInterrupt();
				break;
			}
			
			else System.out.println(a.getString());
		}
		
		System.out.println("Done");
		a.stop();
	}
	
	public static double getDistanceData(double a)
	{
		return (KNOWN_WIDTH * focalLength) / a;
		
	}
}
