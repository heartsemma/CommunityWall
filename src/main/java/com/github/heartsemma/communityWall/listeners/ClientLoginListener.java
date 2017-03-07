package com.github.heartsemma.communityWall.listeners;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RemoteConnection;

import com.github.heartsemma.communityWall.wall.ConnectionManager;

public class ClientLoginListener
{
	public ClientLoginListener(Logger logger, ConnectionManager connectionManager)
	{
		this.logger = logger;
		this.connectionManager = connectionManager;
	}

	private Logger logger;
	private ConnectionManager connectionManager;

	@Listener
	public void onClientLogin(ClientConnectionEvent.Login event)
	{
		logger.info("Checking connecting client...");
		RemoteConnection connection = event.getConnection();

		if (connection instanceof PlayerConnection)
		{
			PlayerConnection playerConnection = (PlayerConnection) connection;

			if (connectionManager.goodConnection(playerConnection))
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
