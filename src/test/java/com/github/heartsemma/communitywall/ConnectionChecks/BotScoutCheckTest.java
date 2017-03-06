package com.github.heartsemma.communitywall.ConnectionChecks;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communitywall.Configuration.Config;
import com.github.heartsemma.communitywall.Wall.ConnectionRules.BotScoutRule;

import static org.mockito.Mockito.*;

public class BotScoutCheckTest
{

	@Test
	public void isAllowedConnectionTest()
	{

		Logger logger = LoggerFactory.getLogger("ROOT");

		BotScoutRule botScoutRule = new BotScoutRule("CommunityWallTest", logger, new Config().getBotConfig().getBotScoutConfig());

		ArrayList<PlayerConnection> goodConnections = new ArrayList<PlayerConnection>();

		goodConnections.add(newConnection("172.91.91.228"));
		goodConnections.add(newConnection("192.99.72.10"));

		ArrayList<PlayerConnection> badConnections = new ArrayList<PlayerConnection>();

		badConnections.add(newConnection("120.40.130.70"));
		badConnections.add(newConnection("93.120.174.120"));
		badConnections.add(newConnection("212.129.8.153"));

		for (PlayerConnection i : goodConnections)
			assertTrue(botScoutRule.isAllowedConnection(i));

		for (PlayerConnection i : badConnections)
			assertFalse(botScoutRule.isAllowedConnection(i));

	}

	/**
	 * Creates a partial mock of a connection that can return an ip address.
	 * 
	 * @param ipString The IP to return.
	 *            
	 * @return A PlayerConnection object that can return a Host Address of the ipString but nothing else.
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
