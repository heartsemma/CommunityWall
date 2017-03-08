package com.github.heartsemma.communitywall.testUtils;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;

import org.spongepowered.api.network.PlayerConnection;

public class MockUtils
{
	/**
	 * Creates a partial mock of a connection that can return an ip address.
	 * 
	 * @param ipString
	 *            The IP to return.
	 * 
	 * @return A PlayerConnection object that can return a Host Address of the
	 *         ipString but nothing else.
	 */
	public static PlayerConnection newPlayerConnection(InetAddress ia)
	{
		PlayerConnection playerConnection = mock(PlayerConnection.class);
		
		Random random = new Random();
		
		InetSocketAddress socketAddress = new InetSocketAddress(ia, 1001 + random.nextInt(10000));
		doReturn(socketAddress).when(playerConnection).getAddress();

		return playerConnection;
	}
}
