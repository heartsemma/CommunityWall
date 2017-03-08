package com.github.heartsemma.communityWall.wall.whitelists;

import java.net.InetAddress;

import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.wall.Whitelist;

public class LinkLocalWhitelist extends Whitelist
{
	private static final String NAME_OF_WHITELIST = "Link Local (Intranet) Whitelist";
	
	public LinkLocalWhitelist(){}
	
	@Override
	public String getName()
	{
		return NAME_OF_WHITELIST;
	}
	
	/**
	 * @param connection The PlayerConnection to test.
	 * @return True if the PlayerConnection uses a LinkLocal address, false if not.
	 */
	@Override
	protected boolean isWhitelisted(PlayerConnection connection)
	{
		InetAddress ia = connection.getAddress().getAddress();
		return ia.isLinkLocalAddress();
	}
	
}
