package com.github.heartsemma.communitywall.testUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class InetAddressUtils
{
	
	/**
	 * Generates and returns a random IPv4 address that does not start with 192
	 * and is not 255.255.255.255 or 127.0.0.1
	 * 
	 * @return A random 32-bit IPv4 address that does not start with 192 and is
	 *         not 255.255.255.255 or 127.0.0.1
	 */
	public static InetAddress randomAddress()
	{
		InetAddress ia = null;
		
		Random rand = new Random();
		int V4orV6 = ((rand.nextInt(1))+1)*2; //2 or 4
		V4orV6*=V4orV6;// = 4 or 16
		
		byte[] ip = new byte[V4orV6];
		do 
		{
			rand.nextBytes(ip);
			try
			{
				ia = InetAddress.getByAddress(ip);
			} 
			catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		} 
		while (ia!=null && !ia.isMCGlobal());
		
		return ia;
		
	}
}
