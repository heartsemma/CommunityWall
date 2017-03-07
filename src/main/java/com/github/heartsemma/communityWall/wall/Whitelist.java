package com.github.heartsemma.communityWall.wall;

import org.spongepowered.api.network.PlayerConnection;

public abstract class Whitelist
{
	/**
	 * This function returns a name for this ConnectionWhitelist, usable in logs and
	 * such.
	 * 
	 * @return The name or title of the ConnectionWhitelist
	 */
	public abstract String getName();
	
	/**
	 * This is the abstract method subclasses should Override and add their own checks to.
	 * @param connection The PlayerConnection to check the whitelisted status of.
	 * @return True if the PlayerConnection is whitelisted in the extended
	 * class, false if not.
	 */
	protected abstract boolean isWhitelisted(PlayerConnection connection);
	
	/**
	 * This is the method that outside classes use to probe instances of Whitelist
	 * to see if a PlayerConnection is on their whitelist.
	 * @param connection The PlayerConnection to check the whitelisted status of.
	 * @return True if the PlayerConnection is whitelisted, false if not.
	 */
	public boolean isWhitelistedConnection(PlayerConnection connection)
	{
		// By policy this plugin rejects all nonexistent player connections
		if (connection == null)
			return false;
		
		return isWhitelisted(connection);
	}
}
