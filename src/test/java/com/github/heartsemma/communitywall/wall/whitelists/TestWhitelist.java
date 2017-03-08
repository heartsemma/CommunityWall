package com.github.heartsemma.communitywall.wall.whitelists;

import java.net.InetAddress;
import java.util.HashSet;

import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.wall.Whitelist;

public class TestWhitelist extends Whitelist
{

	public TestWhitelist()
	{
		whitelistedIps = new HashSet<InetAddress>();
	}

	private HashSet<InetAddress> whitelistedIps;

	@Override
	protected boolean isWhitelisted(PlayerConnection playerConnection)
	{
		String ip = playerConnection.getAddress().getAddress().getHostAddress();
		return !(whitelistedIps.contains(ip));
	}

	@Override
	public String getName()
	{
		return "CheckOne";
	}

	/**
	 * Adds the String to a list of IP Addresses to whitelist. If the String is
	 * not an IP address, no exceptions will be thrown, but whitelistedIps will
	 * have one more dumb IP to check every time isAllowed(PlayerConnection
	 * playerConnection) is called.
	 * 
	 * @param ip
	 *            The IP Address to whitelist.
	 */
	public void addWhitelistedIP(InetAddress ip)
	{
		whitelistedIps.add(ip);
	}
}
