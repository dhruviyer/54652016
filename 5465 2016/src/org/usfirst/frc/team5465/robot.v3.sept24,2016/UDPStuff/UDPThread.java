package org.usfirst.frc.team5465.robot.v3.sept24,2016.UDPStuff;

import java.io.*;
import java.net.*;

public class UDPThread extends Thread
{
	private String name;
	private DatagramSocket serverSocket;
	private byte[] receiveData;
	private String receiveSentence;
	private boolean keepgoing;
	
	public UDPThread(String name1, int port, int buffersize) 
	{
		super(name1);
		
		try 
		{
			serverSocket = new DatagramSocket(port);
			System.out.println("Starting Server");
		} 
		catch (SocketException e)
		{
			System.out.println("UDP Server can't be made");
		}
		receiveData = new byte[buffersize];
		receiveSentence = "";
	}
	
	public void run()
	{
		System.out.println("Starting Thread");
		while(true)
        {
		
           DatagramPacket receivePacket = new DatagramPacket(receiveData, 0,receiveData.length);
          
           try 
           {
        	   serverSocket.receive(receivePacket);
			
           } 
           catch (IOException e) 
           {
        	   System.out.println("hello bub");
        	   e.printStackTrace();
           }
           
           receiveSentence = new String(receivePacket.getData());
          
        }
	}
	
	public String getString()
	{
		return receiveSentence;
	}

}
