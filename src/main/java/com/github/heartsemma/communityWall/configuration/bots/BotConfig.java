package com.github.heartsemma.communityWall.configuration.bots;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BotConfig
{
	// Sets default values.
	public BotConfig()
	{
		blockBots = true;
		botScoutConfig = new BotScoutConfig();
		stopForumSpamConfig = new StopForumSpamConfig();
	}

	@Setting(value = "Block_Bots", comment = "Whether or not to block bots on your server. Remember, instead of setting this to false, "
			+ "you can always whitelist an IP or user to override any CommunityWall blocking.")
	private boolean blockBots;

	@Setting(value = "Bot_Scout", comment = "These options control how your Bot Detector uses the botscout.com API.")
	private BotScoutConfig botScoutConfig;

	@Setting(value = "Stop_Forum_Spam", comment = "These options control how your Bot Detector uses the Stop Forum Spam API")
	private StopForumSpamConfig stopForumSpamConfig;

	/**
	 * @return True if bot blocking is on, false if not.
	 */
	public boolean shouldBlockBots()
	{
		return blockBots;
	}

	/**
	 * @return A BotScoutConfig object that holds all the values related to
	 *         BotScout.com's configuration
	 */
	public BotScoutConfig getBotScoutConfig()
	{
		return botScoutConfig;
	}

	/**
	 * @return A StopForumSpam object that holds all the values related to
	 *         StopForumSpam's configuration.
	 */
	public StopForumSpamConfig getStopForumSpamConfig()
	{
		return stopForumSpamConfig;
	}
}
