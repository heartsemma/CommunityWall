package com.github.heartsemma.communitywall.Configuration.Bots;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BotConfig
{
	//Sets default values.
	public BotConfig()
	{
		blockBots = true;
		botScoutConfig = new BotScoutConfig();
	}
	
	@Setting(value="Block_Bots", comment="Whether or not to block bots on your server. Remember, instead of setting this to false, "
			+ "you can always whitelist an IP or user to override any CommunityWall blocking.")
	private boolean blockBots;
	
	@Setting(value="Bot_Scout", comment="The options control how your Bot Detector uses the botscout.com API.")
	private BotScoutConfig botScoutConfig;
	
	/**
	 * @return True if bot blocking is on, false if not.
	 */
	public boolean shouldBlockBots()
	{
		return blockBots;
	}
	
	/**
	 * @return A BotScoutConfig object that holds all the values related to BotScout.com's configuration
	 */
	public BotScoutConfig getBotScoutConfig()
	{
		return botScoutConfig;
	}
	
}
