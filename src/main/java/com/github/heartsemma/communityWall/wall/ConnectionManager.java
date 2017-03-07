package com.github.heartsemma.communityWall.wall;

import java.util.HashSet;

import org.slf4j.Logger;
import org.spongepowered.api.network.PlayerConnection;

public class ConnectionManager
{
	public ConnectionManager(Logger logger)
	{
		this.logger = logger;
		rules = new HashSet<Rule>();
		whitelists = new HashSet<Whitelist>();
	}

	private Logger logger;
	private HashSet<Rule> rules;
	private HashSet<Whitelist> whitelists;

	/**
	 * Runs through every check in its HashSet and returns true if a good
	 * PlayerConnection, false if not.<br>
	 * goodConnection() determines the status of a playerConnection by running through all added whitelists,
	 * then all rules. If a connection is on any whitelists, it automatically passes regardless of whether or not
	 * it violates any rules.
	 * 
	 * @param playerConnection
	 *            The player to run through every check
	 * @return True if on a whitelist OR if passed <b>all</b> checks, false if failed any and was not on a whitelist.
	 */
	public boolean goodConnection(PlayerConnection playerConnection)
	{
		for (Whitelist i : whitelists)
		{
			if(i.isWhitelisted(playerConnection))
			{
				logger.info("Player is whitelisted past CommunityWall.");
				return true;
			}
		}
		
		for (Rule i : rules)
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
	 * Adds another whitelist to the HashSet of rules. <br>
	 * These are used to determine the validity of a playerConnection in
	 * goodConnection(PlayerConnection).
	 * 
	 * @param cc
	 *            The connectionCheck to add to the CheckManager's list.
	 */
	public void addConnectionWhitelist(Whitelist whitelist)
	{
		whitelists.add(whitelist);
	}

	/**
	 * Adds another rule to the HashSet of rules. <br>
	 * These are used to determine the validity of a playerConnection in
	 * goodConnection(PlayerConnection).
	 * 
	 * @param cc
	 *            The connectionCheck to add to the CheckManager's list.
	 */
	public void addConnectionRule(Rule rule)
	{
		rules.add(rule);
	}
}
