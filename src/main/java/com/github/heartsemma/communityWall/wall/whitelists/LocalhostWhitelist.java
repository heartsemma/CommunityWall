package com.github.heartsemma.communityWall.wall.whitelists;

import java.net.InetAddress;

import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.wall.Whitelist;

public class LocalhostWhitelist extends Whitelist
{
	private static final String NAME_OF_WHITELIST = "Localhost Whitelist";
	
	public LocalhostWhitelist(){}
	
	@Override
	public String getName()
	{
		return NAME_OF_WHITELIST;
	}
	
	/**
	 * @param connection The PlayerConnection to test.
	 * @return True if the connection comes from a loopback address (127.0.0.1, ::1), false if not.
	 */
	@Override
	protected boolean isWhitelisted(PlayerConnection connection)
	{
		InetAddress ia = connection.getAddress().getAddress();
		return ia.isLoopbackAddress();
	}
}
