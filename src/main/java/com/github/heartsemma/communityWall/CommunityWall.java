package com.github.heartsemma.communityWall;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.github.heartsemma.communityWall.configuration.Config;
import com.github.heartsemma.communityWall.configuration.ConfigUtils;
import com.github.heartsemma.communityWall.configuration.bots.BotConfig;
import com.github.heartsemma.communityWall.configuration.bots.BotScoutConfig;
import com.github.heartsemma.communityWall.configuration.bots.StopForumSpamConfig;
import com.github.heartsemma.communityWall.configuration.whitelists.WhitelistConfig;
import com.github.heartsemma.communityWall.listeners.ClientLoginListener;
import com.github.heartsemma.communityWall.wall.ConnectionManager;
import com.github.heartsemma.communityWall.wall.rules.BotScoutRule;
import com.github.heartsemma.communityWall.wall.rules.StopForumSpamRule;
import com.github.heartsemma.communityWall.wall.whitelists.ConfiguredWhitelist;
import com.github.heartsemma.communityWall.wall.whitelists.LinkLocalWhitelist;
import com.github.heartsemma.communityWall.wall.whitelists.LocalhostWhitelist;
import com.google.inject.Inject;

@Plugin(id = "communitywall", name = "CommunityWall", version = "0.1 (Alpha)")
public class CommunityWall
{

	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfigPath;

	private Config config;
	private ConfigUtils configUtils;

	private EventManager eventManager;
	private ConnectionManager checkManager;

	@Listener
	public void preInit(GamePreInitializationEvent event)
	{
		logger.info("CommunityWall is being initialized and is preparing to protect the server from unwanted bots and"
				+ " griefers.");

		// Used for modifying the configuration, getting it, creating new ones,
		// etc.
		configUtils = new ConfigUtils(logger);

		// Loading configuration. ConfigUtils class does all error checking.
		config = configUtils.loadConfig(defaultConfigPath);

		logger.debug("Preparing player checks...");

	}

	@Listener
	public void gameInit(GameInitializationEvent event)
	{
		logger.debug("Registering listeners...");

		checkManager = new ConnectionManager(logger);
		
		BotConfig botConfig = config.getBotConfig();
		if(botConfig.shouldBlockBots())
		{
			BotScoutConfig botScoutConfig = botConfig.getBotScoutConfig();
			StopForumSpamConfig stopForumSpamConfig = botConfig.getStopForumSpamConfig();
			
			if(botScoutConfig.isEnabled())
				checkManager.addConnectionRule(new BotScoutRule(logger, botScoutConfig));
				
			if(stopForumSpamConfig.isEnabled())
				checkManager.addConnectionRule(new StopForumSpamRule(logger, stopForumSpamConfig));
		}
		
		WhitelistConfig whitelistConfig = config.getWhitelistConfig();
		
		if(whitelistConfig.isLocalhostWhitelisted())
			checkManager.addConnectionWhitelist(new LocalhostWhitelist());
		if(whitelistConfig.isIntranetWhitelisted())
			checkManager.addConnectionWhitelist(new LinkLocalWhitelist());
		
		checkManager.addConnectionWhitelist(new ConfiguredWhitelist(logger, whitelistConfig, defaultConfigPath));
		
		eventManager.registerListeners(this, new ClientLoginListener(logger, checkManager));

	}
}
