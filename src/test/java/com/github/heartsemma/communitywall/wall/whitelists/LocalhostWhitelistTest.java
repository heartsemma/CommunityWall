package com.github.heartsemma.communitywall.wall.whitelists;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.heartsemma.communityWall.wall.ConnectionManager;
import com.github.heartsemma.communityWall.wall.whitelists.LocalhostWhitelist;

public class LocalhostWhitelistTest
{
	@Test
	public void localhostWhitelistTest()
	{
		Logger logger = LoggerFactory.getLogger(this.getClass());
		ConnectionManager ruleManager = new ConnectionManager(logger);
		
		LocalhostWhitelist lw = new LocalhostWhitelist();
		ruleManager.addConnectionWhitelist(lw);
		
	}
}
