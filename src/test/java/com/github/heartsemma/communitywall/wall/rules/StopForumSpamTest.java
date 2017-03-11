package com.github.heartsemma.communitywall.wall.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.github.heartsemma.communitywall.testUtils.MockUtils.newPlayerConnection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.configuration.Config;
import com.github.heartsemma.communityWall.wall.rules.StopForumSpamRule;

public class StopForumSpamTest
{
	/**
	 * Tests several trusted/untrusted IP Addresses with our StopForumSpamRule class 
	 * to see if it gives the correct answer.
	 */
	@Test
	public void isAllowedConnectionTest()
	{
		Logger logger = LoggerFactory.getLogger(this.getClass());

		StopForumSpamRule sfsRule = new StopForumSpamRule(logger, new Config().getBotConfig().getStopForumSpamConfig());

		ArrayList<PlayerConnection> goodConnections = new ArrayList<PlayerConnection>();
		ArrayList<PlayerConnection> badConnections = new ArrayList<PlayerConnection>();
		
		try
		{
			goodConnections.add(newPlayerConnection(InetAddress.getByName("172.91.91.228")));
			goodConnections.add(newPlayerConnection(InetAddress.getByName("192.99.72.10")));
	
			badConnections.add(newPlayerConnection(InetAddress.getByName("37.115.189.95")));
			badConnections.add(newPlayerConnection(InetAddress.getByName("115.76.85.63")));
			badConnections.add(newPlayerConnection(InetAddress.getByName("13.112.16.193")));
		} 
		catch(UnknownHostException uhe)
		{
			logger.error("One of the IP addresses in test was invalid.");
			logger.error("This message should never be seen ever.");
			uhe.printStackTrace();
		}
		

		for (PlayerConnection i : goodConnections)
			assertTrue(sfsRule.isAllowedConnection(i));

		for (PlayerConnection i : badConnections)
			assertFalse(sfsRule.isAllowedConnection(i));
	}


}
