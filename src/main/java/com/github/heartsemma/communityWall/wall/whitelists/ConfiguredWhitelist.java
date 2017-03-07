package com.github.heartsemma.communityWall.wall.whitelists;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.configuration.whitelists.WhitelistConfig;
import com.github.heartsemma.communityWall.wall.Whitelist;

public class ConfiguredWhitelist extends Whitelist
{
	public ConfiguredWhitelist(Logger logger, WhitelistConfig config, Path pathToConfigFile)
	{
		this.logger = logger;
		this.config = config;
		this.pathToConfigFile = pathToConfigFile;
	}
	
	private Logger logger;
	private WhitelistConfig config;
	private Path pathToConfigFile;

	@Override
	public String getName()
	{
		return "";
	}

	@Override
	protected boolean isWhitelisted(PlayerConnection connection)
	{
		return config.onConfiguredWhitelist(logger, connection.getPlayer(), pathToConfigFile);
	}
	
}
