package com.github.heartsemma.communitywall.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

/**
 * Provides utilities for managing configuration files and creating new ones in
 * others places.
 */
public class ConfigUtils
{
	/**
	 * Creates a new ConfigUtils object.
	 * 
	 * @param pluginName
	 *            Name of the plugin this class is used in. For the logs.
	 * @param logger
	 *            The logger used to display errors, information, etc.
	 */
	public ConfigUtils(String pluginName, Logger logger)
	{
		Preconditions.checkNotNull(pluginName, "Tried to instantiate a ConfigUtils object with a null pluginName.");
		Preconditions.checkNotNull(logger, "Tried to instantiate a ConfigUtils object with a null logger.");

		this.pluginName = pluginName;
		this.logger = logger;

	}

	private String pluginName;
	private Logger logger;

	/**
	 * Attempts to load and return the Config value of a configuration file.
	 * 
	 * @return A Config object. If the function is unable to retrieve a config
	 *         due to an exception, or the config is outdated, returns the
	 *         default configuration.
	 */
	public Config loadConfig(Path path)
	{
		Config defaultConfig = new Config();
		ConfigurationLoader<CommentedConfigurationNode> loader = getConfigLoader(path);
		ConfigurationNode rootNode = getRootNode(path);

		// Unable to load rootNode
		if (rootNode == null)
		{
			logger.error("When trying to load a Config, " + pluginName
					+ " encountered an error. Loading a default configuration file.");
			return defaultConfig;
		}

		// Making sure there is a configuration and if not loading a default
		// one.
		if (rootNode.equals(loader.createEmptyNode()))
		{
			logger.info("No configuration available. Generating new configuration...");
			newDefaultConfig(path);
			return defaultConfig;
		}

		if (rootNode.getNode("version").getDouble() < defaultConfig.getVersion())
		{
			logger.info("The version of the configuration is lower than the plugin version.");
			logger.info("Installing default recent configuration and moving your configuration to the side.");
			logger.info("Update the new one to your liking later.");
			newDefaultConfig(path);
			return defaultConfig;
		}

		Config loadedConfig;
		try
		{
			loadedConfig = rootNode.getValue(TypeToken.of(Config.class));
		} catch (ObjectMappingException e)
		{
			logger.error("The configuration was modified incorrectly. Creating a new default one in its place...");
			newDefaultConfig(path);
			return defaultConfig;
		}

		return loadedConfig;

	}

	/**
	 * Attempts (with no guarantee) to create a default configuration at the
	 * path specified. If there is an existing file there, it renames it
	 * (pluginname).conf.old and moves it to the side.
	 * 
	 * @param path
	 *            The path of the new configuration.
	 */
	public void newDefaultConfig(Path path)
	{
		Preconditions.checkNotNull(path, "Tried to create a new default configuration but the passed path was null.");
		newConfig(path, new Config(), "old");
	}

	/**
	 * Attempts (with no guarantee) to create a default configuration at the
	 * path specified. If there is an existing file there, it renames it
	 * (pluginname).conf.(extension) and moves it to the side.
	 * 
	 * @param path
	 *            The path of the new configuration.
	 * @param extension
	 *            The extension that the old file should be given to make it
	 *            unusable (if it exists). Name it something appropriate, like
	 *            .bad if the file was messed up or .old if it was an old
	 *            version. Defaults to old if no value is provided.
	 */
	public void newDefaultConfig(Path path, String extensionForOld)
	{
		Preconditions.checkNotNull(path, "Tried to create a new default configuration but the pased path was null.");
		Preconditions.checkNotNull(extensionForOld,
				"Tried to create a new default configuration but the passed extensionForOld was null.");

		newConfig(path, new Config(), extensionForOld);
	}

	/**
	 * Attempts (with no guarantee) to create a configuration at the path
	 * specified. If there is an existing file there, it renames it
	 * (pluginname).conf.old and moves it to the side.
	 * 
	 * @param path
	 *            The path of the new configuration.
	 * @param config
	 *            The configuration to be written to the new file. the file was
	 *            messed up or .old if it was an old version. Defaults to old if
	 *            no value is provided.
	 */
	public void newConfig(Path path, Config config)
	{
		Preconditions.checkNotNull(path, "Tried to create a new configuration but the passed path object was null.");
		Preconditions.checkNotNull(config,
				"Tried to create a new configuration but the passed config object was null.");

		newConfig(path, config, "old");
	}

	/**
	 * Attempts (with no guarantee) to create a configuration at the path
	 * specified. If there is an existing file there, it renames it
	 * (pluginname).conf.(extension) and moves it to the side.
	 * 
	 * @param path
	 *            The path of the new configuration.
	 * @param config
	 *            The configuration to be written to the new file.
	 * @param extensionForOld
	 *            The extension that the old file should be given to make it
	 *            unusable (if it exists). Name it something appropriate, like
	 *            .bad if the file was messed up or .old if it was an old
	 *            version. Defaults to old if no value is provided.
	 */
	public void newConfig(Path path, Config config, String extensionForOld)
	{
		Preconditions.checkNotNull(path, "Tried to create a new configuration but the passed path object was null.");
		Preconditions.checkNotNull(config,
				"Tried to create a new configuration but the passed config object was null.");
		Preconditions.checkNotNull(extensionForOld,
				"Tried to create a new configuration but the passed extensionForOld object was null.");

		File configFile = new File(path.toString());
		if (configFile.exists())
		{
			File oldConfig = new File(path + "." + extensionForOld);
			if (!configFile.renameTo(oldConfig))
			{
				// Renaming didn't succeed.
				logger.error(pluginName + " had trouble renaming bad/old configuration file.");
				logger.error("A new default configuration file could not be created.");
				return;
			}
			writeDefaultConfig(path);
		} else
		{
			writeDefaultConfig(path);
		}
	}

	/**
	 * Attempts (with no guarantee) to overwrite any existing configuration file
	 * at the global path with a default configuration.
	 * 
	 * @param path
	 *            The path to write the default configuration to.
	 */
	public void writeDefaultConfig(Path path)
	{
		Preconditions.checkNotNull(path,
				"Tried to write a default configuration to a path but the passed path object was null.");
		writeConfig(path, new Config());
	}

	/**
	 * Attempts (with no guarantee) to overwrite any existing configuration file
	 * at the global path with the provided configuration.
	 * 
	 * @pre path and config are not null
	 * @param path
	 *            The path to write the configuration to.
	 * @param config
	 *            The configuration object to write to the path.
	 */
	public void writeConfig(Path path, Config config)
	{
		Preconditions.checkNotNull(path,
				"Tried to write a configuration to a path but the passed path object was null.");
		Preconditions.checkNotNull(config,
				"Tried to write a configuration to a path but the passed config object was null.");

		ConfigurationNode rootNode = getRootNode(path);
		if (rootNode == null)
		{
			logger.error(
					"We were unable to write the default configuration to file because we were unable to load it.");
		} else
		{
			try
			{
				rootNode.setValue(TypeToken.of(Config.class), config);
				getConfigLoader(path).save(rootNode);
			} catch (ObjectMappingException e)
			{
				logger.error("Tried to write an invalid configuration object to a file. This should never happen.");
				e.printStackTrace();
			} catch (IOException e)
			{
				logger.error("Attempted to modify a configuration file but failed to access it in the filesystem.");
				e.printStackTrace();
			}
		}

	}

	private ConfigurationNode getRootNode(Path path)
	{
		ConfigurationLoader<CommentedConfigurationNode> configurationLoader = getConfigLoader(path);
		ConfigurationNode rootNode = null;

		try
		{
			rootNode = configurationLoader.load();
		} catch (IOException ie)
		{
			logger.error("Attempted to load configuration at " + path.toString() + " but failed.");
			ie.printStackTrace();
		}

		return rootNode;

	}

	private ConfigurationLoader<CommentedConfigurationNode> getConfigLoader(Path path)
	{
		return HoconConfigurationLoader.builder().setPath(path).build();
	}
}
