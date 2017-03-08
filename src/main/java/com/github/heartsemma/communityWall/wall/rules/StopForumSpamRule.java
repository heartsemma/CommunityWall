package com.github.heartsemma.communityWall.wall.rules;

import java.io.IOException;

import org.kohsuke.stopforumspam.*;
import org.slf4j.Logger;
import org.spongepowered.api.network.PlayerConnection;
import org.xml.sax.SAXException;

import com.github.heartsemma.communityWall.configuration.bots.StopForumSpamConfig;
import com.github.heartsemma.communityWall.wall.Rule;

public class StopForumSpamRule extends Rule
{

	private static final String NAME_OF_CHECK = "StopForumSpam";

	public StopForumSpamRule(Logger logger, StopForumSpamConfig config)
	{
		this.logger = logger;
		this.config = config;
	}

	private StopForumSpamConfig config;
	private Logger logger;
	
	
	/**
	 * @param playerConnection The player for StopForumSpam to check.
	 * @return False whenever the connection appears to StopForumSpam to be a bot, true if clear, and either
	 * true or false depending on the configuration if an error occurs.
	 */
	@Override
	protected boolean isAllowed(PlayerConnection playerConnection)
	{
		boolean onError = !config.shouldRejectOnError();

		String ip = playerConnection.getAddress().getAddress().getHostAddress();
		try
		{
			for (Answer a : new StopForumSpam().build().ip(ip).query())
			{
				// Shows up in the bot database.
				if (a.isAppears())
				{
					return false;
				}
			}
		} catch (IOException e)
		{
			logger.error("An error occurred while attempting to look through stopforumspam.com's bot database.");
			e.printStackTrace();
			return onError;
		} catch (SAXException e)
		{
			logger.error("An error occured attempting to parse the xml file given by StopForumSpam's database.");
			e.printStackTrace();
			return onError;
		}

		// Didn't show up in the database.
		return true;
	}

	@Override
	public String getName()
	{
		return NAME_OF_CHECK;
	}

}
