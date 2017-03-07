package com.github.heartsemma.communityWall.configuration.whitelists;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;

import com.github.heartsemma.communityWall.configuration.Config;
import com.github.heartsemma.communityWall.configuration.ConfigUtils;
import com.google.common.base.Preconditions;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class WhitelistConfig
{
	/**
	 * Initializes the default values for this whitelist configuration, which are:<br>
	 * <li> Whitelist Localhost is set to true.
	 * <li> Whitelist Intranet is set to true.
	 */
	public WhitelistConfig()
	{
		whitelistLocalhost = true;
		whitelistIntranet = true;
	}
	
	@Setting (value = "Whitelist_Localhost", comment = "Whether or not to whitelist loopback connections coming from within the server,"
			+ " meaning 127.0.0.1 and ::1")
	private boolean whitelistLocalhost;
	
	@Setting (value = "Whitelist_Intranet", comment = "Whether or not to whitelist IPs that come from private"
			+ " addresses within the server's network. Specifically, this whitelists link-local and interface addresses."
			+ " If you don't know what that means, leave this true.")
	private boolean whitelistIntranet;
	
	@Setting (value = "Whitelisted_IPs", comment = "Put all of the IP Addresses you want to whitelist in here. They will bypass "
			+ "*all* their rules. IP Address ranges are currently not supported.")
	private List<String> whitelistedIPs;
	
	@Setting (value = "Whitelisted_Players", comment = "Put the current username of any player you want whitelisted in here. "
			+ "Because player usernames may change at any time, he plugin will convert all names "
			+ "stored here into UUIDs that remain reference those players cross-username when "
			+ "the player logs in. ")
	private List<String> whitelistedPlayers;
	
	/**
	 * @return True if the configuration has been set to Whitelist_Localhost: True, false if not. 
	 */
	public boolean isLocalhostWhitelisted()
	{
		return whitelistLocalhost;
	}

	/**
	 * @return True if the configuration has been set to Whitelist_Intranet: True, false if not. 
	 */
	public boolean isIntranetWhitelisted()
	{
		return whitelistIntranet;
	}
	
	/**
	 * @return The map of all IPs written under Whitelisted_IPs in the configuration
	 */
	public List<String> getWhitelistedIPs()
	{
		return whitelistedIPs;
	}
	
	/**
	 * This function checks the list of players to whitelist and returns it as a list of UUIDs.<br>
	 * If the player list contains usernames, they are converted to UUIDs and written to the configuration.
	 * @return The list of all UUIDs the plugin should whitelist.
	 */
	public ArrayList<UUID> getWhitelistedUUIDs()
	{
		ArrayList<UUID> uuidList = new ArrayList<UUID>();
		for(String i : whitelistedPlayers)
		{
			try 
			{
				uuidList.add(UUID.fromString(i));
			} 
			catch(IllegalArgumentException iae){}
		}
		return uuidList;
	}
	
	/**
	 * Sees if the playername is on a whitelist.<br>
	 * If they are, attempts to do the work of taking them off the config at the file specified and replaces them with a uuid.
	 * @param logger A logger instance to log how the file overwriting goes.
	 * @param player The player to check for whitelisted status. Specifically checks the whitelist for this player's name and UUID.
	 * @param pathToConfig The path to configuration file that onConfiguredWhitelist should modify. This must be the configuration that
	 * WhitelistConfig was instantiated under.
	 * @return True if the player's name is on the whitelist, false if not.
	 */
	public boolean onConfiguredWhitelist(Logger logger, Player player, Path pathToConfig)
	{
		Preconditions.checkNotNull(logger, "Passed a null logger argument to onConfiguredWhitelist.");
		Preconditions.checkNotNull(player, "Passed a null player argument to onConfiguredWhitelist.");
		Preconditions.checkNotNull(pathToConfig, "Passed a null pathToConfig argument to onConfiguredWhitelist.");
		
		for(String i: whitelistedPlayers)
		{
			String name = player.getName();
			if(i.equals(player.getUniqueId()))
				return true;
			if(i.equals(name))
			{
				logger.info("Removing playername and replacing with UUID.");
				
				whitelistedPlayers.add(player.getUniqueId().toString());
				whitelistedPlayers.remove(name);
				
				ConfigUtils util = new ConfigUtils(logger);
				Config loadedConfiguration = util.loadConfig(pathToConfig);
				
				if(loadedConfiguration.getWhitelistConfig()==this)
					util.writeConfig(pathToConfig, loadedConfiguration);
				return true;
			}
		}
		return false;
	}
	
}
