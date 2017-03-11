package com.github.heartsemma.communitywall.wall.whitelists;

import static org.junit.Assert.assertTrue;
import static com.github.heartsemma.communitywall.testUtils.MockUtils.newPlayerConnection;

import java.net.InetAddress;

import org.junit.Test;
import org.spongepowered.api.network.PlayerConnection;

import com.github.heartsemma.communityWall.wall.whitelists.LocalhostWhitelist;

public class LoopbackWhitelistTest
{
	@Test
	public void localhostWhitelistTest()
	{
		LocalhostWhitelist loopbackWhitelist = new LocalhostWhitelist();
		
		PlayerConnection localhostPlayer = newPlayerConnection(InetAddress.getLoopbackAddress());
		
		assertTrue(loopbackWhitelist.isWhitelistedConnection(localhostPlayer));
	}
}
