package com.github.heartsemma.communitywall.Configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Test;
import org.slf4j.LoggerFactory;

public class ConfigUtilsTest
{
	private ConfigUtils configUtils = new ConfigUtils("TestPlugin", LoggerFactory.getLogger("ROOT"));

	// TODO: Finish this testing suite when config is done.

	@Test
	public void newConfigTest()
	{
		String tempConfigFilename = "superDuperTempDefaultConfig.conf";
		Path defaultConfigPath = newPath(tempConfigFilename);

		configUtils.newDefaultConfig(defaultConfigPath);

		File configFile = defaultConfigPath.toFile();
		assertTrue(configFile.exists());
		assertTrue(configFile.length() > 1);

		configUtils.newDefaultConfig(defaultConfigPath, "bad");

		assertTrue(configFile.exists());
		assertTrue(configFile.length() > 1);

		Path oldConfigPath = newPath(tempConfigFilename + ".bad");
		File oldConfig = oldConfigPath.toFile();

		assertTrue(oldConfig.exists());
		assertTrue(oldConfig.length() > 1);

		oldConfig.delete();
		configFile.delete();
	}

	private Path newPath(String filename)
	{
		return FileSystems.getDefault().getPath(System.getProperty("java.io.tmpdir") + filename);
	}
}
