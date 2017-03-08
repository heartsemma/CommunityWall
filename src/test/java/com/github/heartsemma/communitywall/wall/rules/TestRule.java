package com.github.heartsemma.communitywall.wall.connectionRules;

import java.net.InetAddress;
import java.util.HashSet;

import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.wall.Rule;

public class TestRule extends Rule
{

	public TestRule()
	{
		blockedIps = new HashSet<InetAddress>();
	}

	private HashSet<InetAddress> blockedIps;

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
	public void addBlockedIP(InetAddress ip)
	{
		blockedIps.add(ip);
	}
}
