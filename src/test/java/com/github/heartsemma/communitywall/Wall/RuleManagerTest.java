package com.github.heartsemma.communitywall.Wall;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.wall.ConnectionRule;
import com.github.heartsemma.communityWall.wall.RuleManager;

public class RuleManagerTest
{
	private Logger logger;

	@Test
	public void ruleResponseTest()
	{
		logger = LoggerFactory.getLogger("ROOT");
		TestRule testRule;
		RuleManager ruleManager;

		// Create five randomized rules that block a random IP, add to
		// ruleManager, make sure ruleManager responds to the rules
		for (int i = 0; i < 5; i++)
		{
			// Empty Rule Manager with no rules about PlayerConnections
			ruleManager = new RuleManager(logger);
			// Small private ConnectionRule class that blocks added IPs.
			testRule = new TestRule();

			// Adding a new random IPv4 address to the list of blocked IPs
			String blockedIp = randomIPv4();
			testRule.addBlockedIP(blockedIp);

			// Adding the rule to the ruleManager
			ruleManager.addConnectionRule(testRule);

			// Testing the
			assertFalse(ruleManager.goodConnection(newConnection(blockedIp)));

			// Gathering a random unblockedIP
			String unblockedIP = "";
			do
			{
				unblockedIP = randomIPv4();
			} while (unblockedIP.equals(blockedIp));

			assertTrue(ruleManager.goodConnection(newConnection(unblockedIP)));
		}
	}

	/**
	 * Generates and returns a random IPv4 address that does not start with 192
	 * and is not 255.255.255.255 or 127.0.0.1
	 * 
	 * @return A random 32-bit IPv4 address that does not start with 192 and is
	 *         not 255.255.255.255 or 127.0.0.1
	 */
	private String randomIPv4()
	{
		Random rand = new Random();
		String s = "";
		do
		{
			do
			{
				s += Integer.toString(rand.nextInt(255));
			} while (s.equals("192"));
			s += ".";
			s += Integer.toString(rand.nextInt(255));
			s += ".";
			s += Integer.toString(rand.nextInt(255));
			s += ".";
			s += Integer.toString(rand.nextInt(255));
		} while (s.equals("127.0.0.1") || s.equals("255.255.255.255"));

		return s;
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

	private class TestRule extends ConnectionRule
	{

		public TestRule()
		{
			blockedIps = new HashSet<String>();
		}

		private HashSet<String> blockedIps;

		@Override
		protected boolean isAllowed(PlayerConnection playerConnection)
		{
			String ip = playerConnection.getAddress().getAddress().getHostAddress();
			return !(blockedIps.contains(ip));
		}

		@Override
		public String getName()
		{
			return "CheckOne";
		}

		/**
		 * Adds the String to a list of IP Addresses to block. If the String is
		 * not an IP address, no exceptions will be thrown, but blockedIps will
		 * have one more dumb IP to check every time isAllowed(PlayerConnection
		 * playerConnection) is called.
		 * 
		 * @param ip
		 *            The IP Address to block.
		 */
		public void addBlockedIP(String ip)
		{
			blockedIps.add(ip);
		}
	}
}
