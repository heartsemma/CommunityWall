package com.github.heartsemma.communitywall.Wall.ConnectionRules;

import java.io.IOException;

import org.kohsuke.stopforumspam.*;
import org.slf4j.Logger;
import org.spongepowered.api.network.PlayerConnection;
import org.xml.sax.SAXException;

import com.github.heartsemma.communitywall.Configuration.Bots.BotConfig;
import com.github.heartsemma.communitywall.Wall.ConnectionRule;

public class StopForumSpamRule extends ConnectionRule
{

	private static final String NAME_OF_CHECK = "StopForumSpam";

	public StopForumSpamRule(Logger logger, BotConfig config)
	{
		this.logger = logger;
		this.config = config;
	}

	private BotConfig config;
	private Logger logger;
	
	
	/**
	 * @param playerConnection The player for StopForumSpam to check.
	 * @return False whenever the connection appears to StopForumSpam to be a bot, true if clear, and either
	 * true or false depending on the configuration if an error occurs.
	 */
	@Override
	protected boolean isAllowed(PlayerConnection playerConnection)
	{
		if (!config.shouldBlockBots() || !config.getBotScoutConfig().isEnabled())
		{
			// If the detection is not enabled we don't check for bots.
			return true;
		}

		boolean onError = !config.getBotScoutConfig().shouldRejectOnError();

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
