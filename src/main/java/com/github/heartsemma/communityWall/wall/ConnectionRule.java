package com.github.heartsemma.communityWall.wall;

import org.spongepowered.api.network.PlayerConnection;

public abstract class ConnectionRule
{
	/**
	 * This abstract method is used to check whether or not the ClientConnection
	 * is allowed on the server, according to the specific rules of this class.
	 * 
	 * @param event
	 *            A ClientConnectionEvent Object
	 * @return A boolean indicating whether or not the connection is "Good"
	 *         (follows the rules of the class) or "Bad" (breaks the rules of
	 *         the class).
	 */
	protected abstract boolean isAllowed(PlayerConnection playerConnection);

	/**
	 * This function returns a name for the ConnectionCheck, usable in logs and
	 * such.
	 * 
	 * @return The name or title of the ConnectionCHeck
	 */
	public abstract String getName();

	/**
	 * Takes a ClientConnectionEvent and gives the IP Address of the client
	 * connecting.
	 * 
	 * @param ClientConnectionEvent
	 *            The client that you want the IP Address of.
	 * @return The IP Address of the client connecting in the
	 *         ClientConnectionEvent
	 */

	/**
	 * This method is used to check whether or not the ClientConnection is
	 * allowed on the server. It checks for universal rules on all connections
	 * and then hands it over to the abstract isAllowedConnection() method for
	 * further processing by the subclass.
	 * 
	 * @param event
	 *            A ClientConnectionEvent Object
	 * @return A boolean indicating whether or not the connection is "Good"
	 *         (follows the rules of the class) or "Bad" (breaks the rules of
	 *         the class).
	 */
	public boolean isAllowedConnection(PlayerConnection playerConnection)
	{
		// By policy this plugin rejects all nonexistent player connections
		if (playerConnection == null)
			return false;

		// Allowing a range of local ip addresses to connect at all times
		String ip = playerConnection.getAddress().getAddress().getHostAddress();
		if (ip.equals("127.0.0.1") || ip.startsWith("192.168"))
			return true;

		// Give to subclass for processing
		return isAllowed(playerConnection);
	}
}
