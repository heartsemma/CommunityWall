package com.github.heartsemma.communitywall.Wall.ConnectionRules;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communitywall.Configuration.Bots.BotScoutConfig;
import com.github.heartsemma.communitywall.Wall.ConnectionRule;

/**
 * This check uses the API from botscout.com to check incoming players.
 */
public class BotScoutRule extends ConnectionRule
{

	private static final String NAME_OF_CHECK = "Botscout";

	/**
	 * @param pluginName The name of the whole plugin.
	 * @param logger The logger of the plugin.
	 * @param BotScoutConfig A configuration object detailing how BotScoutRule should function.
	 */
	public BotScoutRule(String pluginName, Logger logger, BotScoutConfig config)
	{
		this.pluginName = pluginName;
		this.logger = logger;
		this.config = config;
	}
	
	private BotScoutConfig config;
	private Logger logger;
	private String pluginName;

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
		if(!config.isEnabled())
			return true;
		
		boolean shouldRejectOnError = config.shouldRejectOnError();
		boolean shouldRejectOnNoUses = config.shouldRejectOnNoUses();
		
		String connectionIP = connection.getAddress().getAddress().getHostAddress();
		String ipCheckString = "http://www.botscout.com/test/?ip=" + connectionIP;
		
		String apiKey = config.getAPIKey();
		if(apiKey!=null && !apiKey.equals(""))
			ipCheckString += "&key=" + apiKey;	
		
		URL ipCheckURL;
		try
		{
			ipCheckURL = new URL(ipCheckString);
		} 
		catch (MalformedURLException e)
		{
			logger.error(
					"When attempting one of the bot checking mechanisms, " + pluginName + " encountered an error.");
			logger.error(
					"We're marking the user as having failed this bot check (although you should find out what went wrong from the stack trace).");
			e.printStackTrace();
			return !shouldRejectOnError;
		}

		Document doc;
		try
		{
			doc = Jsoup.parse(ipCheckURL, 3000);
		} 
		catch (IOException e)
		{
			logger.error("When attempting one of the bot checking mechanisms, " + pluginName + " encountered an error.");
			logger.error("Can your server connect the internet?");
			logger.error("We're marking the user as having failed this bot check (although you should find out what went wrong from the stack trace).");
			e.printStackTrace();
			return !shouldRejectOnError;
		}

		// There should be one p tag, in the entire document. That's how the api
		// at botscout works.
		Elements pElements = doc.getElementsByTag("body");
		if(pElements.size()!=1)
		{
			logger.info("Botscout site is down or modified. Cannot check IP address.");
			System.out.println(doc.toString());
			return !shouldRejectOnError;
		} 
		
		String text = pElements.get(0).text();
			
		if (text.startsWith("N|"))
		{
			return true;
		} 
		else if (text.startsWith("Y|"))
		{
			logger.info("Player turned up on botscout.com's list of bots.");
			return false;
		} 
		else if (text.startsWith("! Maximum daily usage limit reached."))
		{
			logger.warn("You have run out of botscout uses.");
			logger.warn(
					"If you haven't already, go to botscout.com and configure your api key in the plugin to get up to 1000 player checks a day.");
			return !shouldRejectOnNoUses;
		} 
		else
		{
			logger.error("BotScout.com returned this error when attempting the check: " + text);
			return !shouldRejectOnError;
		}
	}

}
