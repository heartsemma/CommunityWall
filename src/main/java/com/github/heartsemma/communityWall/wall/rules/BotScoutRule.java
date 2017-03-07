package com.github.heartsemma.communityWall.wall.rules;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.configuration.bots.BotScoutConfig;
import com.github.heartsemma.communityWall.wall.Rule;

/**
 * This check uses the API from botscout.com to check incoming players.
 */
public class BotScoutRule extends Rule
{

	private static final String NAME_OF_CHECK = "Botscout";

	/**
	 * @param logger
	 *            The logger of the plugin.
	 * @param BotConfig
	 *            A configuration object detailing how BotScoutRule should
	 *            function.
	 */
	public BotScoutRule(Logger logger, BotScoutConfig config)
	{
		this.logger = logger;
		this.config = config;
	}

	private BotScoutConfig config;
	private Logger logger;

	@Override
	public String getName()
	{
		return NAME_OF_CHECK;
	}

	/**
	 * Returns false whenever the connection appears to come from an ip
	 * associated with bots according to BotScout.com. <br>
	 * <br>
	 * Unfortunately, botscout.com only allows any given IP to make 100 requests
	 * a day for non-api-key-holders. If the server has an API key, however, we
	 * will use it for the requests.
	 * 
	 * @param connectionEvent
	 *            The client to check the humanity of.
	 * @return True if it passes the bot test, false if not or an unrecoverable
	 *         exception occurs.
	 */
	@Override
	protected boolean isAllowed(PlayerConnection connection)
	{
		boolean onError = !config.shouldRejectOnError();
		boolean onNoUses = !config.shouldRejectOnNoUses();

		String connectionIP = connection.getAddress().getAddress().getHostAddress();
		String ipCheckString = "http://www.botscout.com/test/?ip=" + connectionIP;

		String apiKey = config.getAPIKey();
		if (apiKey != null && !apiKey.equals(""))
			ipCheckString += "&key=" + apiKey;

		URL ipCheckURL;
		try
		{
			ipCheckURL = new URL(ipCheckString);
		} catch (MalformedURLException e)
		{
			logger.error("Encountered an error while attempting to craft a URL to access botscout.com.");
			e.printStackTrace();
			return onError;
		}

		Document doc;
		try
		{
			doc = Jsoup.parse(ipCheckURL, 3000);
		} catch (IOException e)
		{
			logger.error("Ecountered an error while attempting to connect to botscout.com");
			logger.error("Can your server connect the internet?");
			e.printStackTrace();
			return onError;
		}

		// There should be one p tag, in the entire document. That's how the api
		// at botscout works.
		Elements pElements = doc.getElementsByTag("body");
		if (pElements.size() != 1)
		{
			logger.info("Botscout site is down or modified. Cannot check IP address.");
			System.out.println(doc.toString());
			return onError;
		}

		String text = pElements.get(0).text();

		if (text.startsWith("N|"))
		{
			return true;
		} else if (text.startsWith("Y|"))
		{
			logger.info("Player turned up on botscout.com's list of bots.");
			return false;
		} else if (text.startsWith("! Maximum daily usage limit reached."))
		{
			logger.warn("Your ip has run out of botscout.com uses. CommunityWall can no longer use it to check incoming "
				   + "IPs for today.");
			logger.warn("If you haven't already, get an api key from botscout.com and configure it in your "
					+ "plugin to get more player checks a day.");
			return onNoUses;
		} else
		{
			logger.error("BotScout.com returned this error when attempting the check: " + text);
			return onError;
		}
	}

}
