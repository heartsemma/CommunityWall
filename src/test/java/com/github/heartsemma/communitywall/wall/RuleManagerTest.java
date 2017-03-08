package com.github.heartsemma.communitywall.wall;

import static org.junit.Assert.*;
import static com.github.heartsemma.communitywall.testUtils.InetAddressUtils.randomAddress;
import static com.github.heartsemma.communitywall.testUtils.MockUtils.newPlayerConnection;

import java.net.InetAddress;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.heartsemma.communitywall.wall.connectionRules.TestRule;
import com.github.heartsemma.communitywall.wall.whitelists.TestWhitelist;
import com.github.heartsemma.communityWall.wall.ConnectionManager;

public class RuleManagerTest
{
	private Logger logger;

	@Test
	public void ruleResponseTest()
	{
		logger = LoggerFactory.getLogger("ROOT");
		TestRule testRule;
		TestWhitelist testWhitelist;
		ConnectionManager connectionManager;

		// Create five randomized rules that block a random IP, add to
		// ruleManager, make sure ruleManager responds to the rules
		for (int i = 0; i < 5; i++)
		{
			// Empty Rule Manager with no rules about PlayerConnections
			connectionManager = new ConnectionManager(logger);
			// Small private Rule class that blocks added IPs.
			testRule = new TestRule();

			// Adding a new random IPv4 address to the list of blocked IPs
			InetAddress blockedIp = randomAddress();
			testRule.addBlockedIP(blockedIp);

			// Adding the rule to the ruleManager
			connectionManager.addConnectionRule(testRule);

			// Testing the
			assertFalse(connectionManager.goodConnection(newPlayerConnection(blockedIp)));

			// Gathering a random unblockedIP
			InetAddress unblockedIP;
			do
			{
				unblockedIP = randomAddress();
			} while (unblockedIP.equals(blockedIp));

			assertTrue(connectionManager.goodConnection(newPlayerConnection(unblockedIP)));
		}
		
		//Creates a whitelist and tests its effects on the rulemanager
		for (int i = 0; i < 5; i++)
		{
			// Empty Rule Manager with no rules about PlayerConnections
			connectionManager = new ConnectionManager(logger);
			// Small private Rule class that blocks added IPs.
			testRule = new TestRule();
			testWhitelist = new TestWhitelist();
			
			InetAddress blockedButWhitelistedIP = randomAddress();
			testRule.addBlockedIP(blockedButWhitelistedIP);
			testWhitelist.addWhitelistedIP(blockedButWhitelistedIP);
			
			// Gathering a random unblockedIP
			InetAddress blockedIP;
			do
			{
				blockedIP = randomAddress();
			} while (blockedIP.equals(blockedButWhitelistedIP));
			
			testRule.addBlockedIP(blockedIP);
			
			connectionManager.addConnectionRule(testRule);
			connectionManager.addConnectionWhitelist(testWhitelist);
			
			assertTrue(connectionManager.goodConnection(newPlayerConnection(blockedButWhitelistedIP)));
			assertFalse(connectionManager.goodConnection(newPlayerConnection(blockedIP)));
	
		}
	}
}
