package com.github.heartsemma.communityWall.configuration;

import com.github.heartsemma.communityWall.configuration.bots.BotConfig;
import com.github.heartsemma.communityWall.configuration.whitelists.WhitelistConfig;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config
{
	public Config()
	{
		// These are all the default values and are assigned when the
		// constructor to Config is called.
		version = 1;
		botConfig = new BotConfig();

	}

	/*
	 * Settings
	 */
	@Setting(value = "Version", comment = "The version number of the configuration. Do not modify this unless you want your config overwritten.")
	private double version;
	
	@Setting(value = "Whitelist", comment = "These options control the CommunityWall whitelist, allowing you to let some "
			+ " connections bypass Communitywall")
	private WhitelistConfig whitelistConfig;

	@Setting(value = "Bots", comment = "These options govern how your server detects and blocks bots.")
	private BotConfig botConfig;

	/**
	 * @return The version number of this configuration.
	 */
	public double getVersion()
	{
		return version;
	}

	/**
	 * @return The held configuration object for rules about Bots.
	 */
	public BotConfig getBotConfig()
	{
		return botConfig;
	}
	
	/**
	 * @return The held configuration object for whitelists.
	 */
	public WhitelistConfig getWhitelistConfig()
	{
		return whitelistConfig;
	}

}
