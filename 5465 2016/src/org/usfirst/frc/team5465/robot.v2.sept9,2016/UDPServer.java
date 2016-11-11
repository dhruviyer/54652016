package org.usfirst.frc.team5465.robot;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
	DatagramSocket sock = null;
	byte[] buffer = null;
	DatagramPacket incoming;
	final int PORT = 5466;
	String data;
	
	public UDPServer(){
		try
        {
            while(true)
            {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                data = new String(data, 0, incoming.getLength());
                System.out.println("Data is: "+data);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
	}
	
	public void createUDP()
	{
		sock = new DatagramSocket(PORT);  
		buffer = new byte[65536];
		incoming = new DatagramPacket(buffer, buffer.length);
	}
}
