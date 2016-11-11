package org.usfirst.frc.team5465.robot;

import java.io.*;
import java.net.*;

public class UDPThread extends Thread
{
	private String name;
	private DatagramSocket serverSocket;
	private byte[] receiveData;
	private String receiveSentence;
	private boolean interrupt;
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
			System.out.println("UDP Server can't be made :( ");
		}
		receiveData = new byte[buffersize];
		receiveSentence = "";
		interrupt = false;
	}
	
	public InetAddress getIP()
	{
		return serverSocket.getInetAddress();
	}
	
	public void run()
	{
		System.out.println("Starting Thread");
		while(!interrupt)
        {
		
           DatagramPacket receivePacket = new DatagramPacket(receiveData, 0,receiveData.length);
          
           try 
           {
        	   serverSocket.receive(receivePacket);
           } 
           catch (IOException e) 
           {
        	   System.out.println("hello boiii");
        	   e.printStackTrace();
           }
           
           receiveSentence = new String(receivePacket.getData());
          
        }
	}
	
	public String getString()
	{
		return receiveSentence;
	}
	
	public void updateInterrupt()
	{
		interrupt = true;
	}

}
