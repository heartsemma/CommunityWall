package com.github.heartsemma.communitywall.Wall.ConnectionRules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communitywall.Configuration.Config;

public class StopForumSpamTest
{
	/**
	 * Tests several trusted/untrusted IP Addresses with our StopForumSpamRule class 
	 * to see if it gives the correct answer.
	 */
	@Test
	public void isAllowedConnectionTest()
	{
		Logger logger = LoggerFactory.getLogger("ROOT");

		StopForumSpamRule sfsRule = new StopForumSpamRule(logger, new Config().getBotConfig());

		ArrayList<PlayerConnection> goodConnections = new ArrayList<PlayerConnection>();

		goodConnections.add(newConnection("172.91.91.228"));
		goodConnections.add(newConnection("192.99.72.10"));

		ArrayList<PlayerConnection> badConnections = new ArrayList<PlayerConnection>();

		badConnections.add(newConnection("37.115.189.95"));
		badConnections.add(newConnection("115.76.85.63"));
		badConnections.add(newConnection("13.112.16.193"));

		for (PlayerConnection i : goodConnections)
			assertTrue(sfsRule.isAllowedConnection(i));

		for (PlayerConnection i : badConnections)
			assertFalse(sfsRule.isAllowedConnection(i));
	}

	/**
	 * Creates a partial mock of a connection that can return an ip address.
	 * 
	 * @param ipString
	 *            The IP to return.
	 * 
	 * @return A PlayerConnection object that can return a Host Address of the
	 *         ipString but nothing else.
	 */
	private PlayerConnection newConnection(String ipString)
	{
		PlayerConnection playerConnection = mock(PlayerConnection.class);

		InetAddress inetAddress = mock(InetAddress.class);
		doReturn(ipString).when(inetAddress).getHostAddress();

		InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, 51);
		doReturn(socketAddress).when(playerConnection).getAddress();

		return playerConnection;
	}
}
