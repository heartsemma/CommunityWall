package com.github.heartsemma.communityWall.wall;

import java.util.HashSet;

import org.slf4j.Logger;
import org.spongepowered.api.network.PlayerConnection;

public class RuleManager
{
	public RuleManager(Logger logger)
	{
		this.logger = logger;
		connectionChecks = new HashSet<ConnectionRule>();
	}

	private Logger logger;
	private HashSet<ConnectionRule> connectionChecks;

	/**
	 * Runs through every check in its HashSet and returns true if a good
	 * PlayerConnection, false if not.
	 * 
	 * @param playerConnection
	 *            The player to run through every check
	 * @return True if passed <b>all</b> checks, false if failed any.
	 */
	public boolean goodConnection(PlayerConnection playerConnection)
	{
		for (ConnectionRule i : connectionChecks)
		{
			if (!i.isAllowedConnection(playerConnection))
			{
				logger.info("Player failed " + i.getName() + " check.");
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds another ConnectionCheck to the HashSet of ConnectionChecks. <br>
	 * These are used to determine the goodness of a playerConnection in
	 * goodConnection(PlayerConnection).
	 * 
	 * @param cc
	 *            The connectionCheck to add to the CheckManager's list.
	 */
	public void addConnectionRule(ConnectionRule connectionRule)
	{
		connectionChecks.add(connectionRule);
	}
}
