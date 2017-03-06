package com.github.heartsemma.communitywall.Events;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RemoteConnection;

import com.github.heartsemma.communitywall.Wall.RuleManager;

public class ClientLoginListener
{
	public ClientLoginListener(Logger logger, RuleManager ruleManager)
	{
		this.logger = logger;
		this.ruleManager = ruleManager;
	}

	private Logger logger;
	private RuleManager ruleManager;

	@Listener
	public void onClientLogin(ClientConnectionEvent.Login event)
	{
		logger.info("Checking connecting client...");
		RemoteConnection connection = event.getConnection();

		if (connection instanceof PlayerConnection)
		{
			PlayerConnection playerConnection = (PlayerConnection) connection;

			if (ruleManager.goodConnection(playerConnection))
			{
				// No need to say why; the CheckManager does that.
				logger.info("Letting them log in...");
			} else
			{
				logger.info("Denying player entry.");
				event.setCancelled(true);
			}
		} else
		{
			logger.info("Client not a player, allowing login.");
		}
	}
}
