package com.github.heartsemma.communitywall.wall.connectionRules;

import static org.junit.Assert.*;
import static com.github.heartsemma.communitywall.testUtils.MockUtils.newPlayerConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.configuration.Config;
import com.github.heartsemma.communityWall.wall.rules.BotScoutRule;


public class BotScoutCheckTest
{
	/**
	 * Tests several trusted/untrusted IP Addresses with BotScoutRule to see 
	 * if it gives the correct answer.
	 */
	@Test
	public void isAllowedConnectionTest()
	{

		Logger logger = LoggerFactory.getLogger(this.getClass());

		BotScoutRule botScoutRule = new BotScoutRule(logger, new Config().getBotConfig().getBotScoutConfig());

		ArrayList<PlayerConnection> goodConnections = new ArrayList<PlayerConnection>();
		ArrayList<PlayerConnection> badConnections = new ArrayList<PlayerConnection>();
		
		try
		{
			goodConnections.add(newPlayerConnection(InetAddress.getByName("172.91.91.228")));
			goodConnections.add(newPlayerConnection(InetAddress.getByName("192.99.72.10")));
	
			badConnections.add(newPlayerConnection(InetAddress.getByName("120.40.130.70")));
			badConnections.add(newPlayerConnection(InetAddress.getByName("93.120.174.120")));
			badConnections.add(newPlayerConnection(InetAddress.getByName("212.129.8.153")));
		}
		catch (UnknownHostException uhe)
		{
			
			uhe.printStackTrace();
		}
		

		for (PlayerConnection i : goodConnections)
			assertTrue(botScoutRule.isAllowedConnection(i));

		for (PlayerConnection i : badConnections)
			assertFalse(botScoutRule.isAllowedConnection(i));

	}
}
