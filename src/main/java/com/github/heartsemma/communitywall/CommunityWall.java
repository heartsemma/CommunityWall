package com.github.heartsemma.communitywall;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.github.heartsemma.communitywall.Configuration.Config;
import com.github.heartsemma.communitywall.Configuration.ConfigUtils;
import com.github.heartsemma.communitywall.Events.ClientLoginListener;
import com.github.heartsemma.communitywall.Wall.RuleManager;
import com.github.heartsemma.communitywall.Wall.ConnectionRules.BotScoutRule;
import com.github.heartsemma.communitywall.Wall.ConnectionRules.StopForumSpamRule;
import com.google.inject.Inject;

@Plugin(id = "communitywall", name = "CommunityWall", version = "0.1 (Alpha)")
public class CommunityWall
{

	@Inject
	private Logger logger;
	@Inject
	private PluginContainer pluginContainer;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfigPath;

	private Config config;
	private ConfigUtils configUtils;

	private String pluginName;

	private EventManager eventManager;
	private RuleManager checkManager;

	@Listener
	public void preInit(GamePreInitializationEvent event)
	{
		logger.info("CommunityWall is being initialized and is preparing to protect the server from unwanted bots and"
				+ " griefers.");

		// Name of plugin.
		pluginName = pluginContainer.getName();

		// Used for modifying the configuration, getting it, creating new ones,
		// etc.
		configUtils = new ConfigUtils(pluginName, logger);

		// Loading configuration. ConfigUtils class does all error checking.
		config = configUtils.loadConfig(defaultConfigPath);

		logger.debug("Preparing player checks...");

	}

	@Listener
	public void gameInit(GameInitializationEvent event)
	{
		logger.debug("Registering listeners...");

		checkManager = new RuleManager(logger);

		checkManager.addConnectionRule(new BotScoutRule(logger, config.getBotConfig()));
		checkManager.addConnectionRule(new StopForumSpamRule(logger, config.getBotConfig()));

		eventManager.registerListeners(this, new ClientLoginListener(logger, checkManager));

	}
}
