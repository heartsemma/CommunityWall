package com.github.heartsemma.communityWall.configuration.bots;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class StopForumSpamConfig
{
	// Sets default values.
	public StopForumSpamConfig()
	{
		enabled = true;
		rejectOnError = true;
		;
	}

	@Setting(value = "Enabled", comment = "Whether or not to use StopForumSpam to check users for personhood.")
	private boolean enabled;

	@Setting(value = "Reject_On_Error", comment = "True if CommunityWall should reject users if an error occurs during StopForumSpam's tests, false if not.")
	private boolean rejectOnError;

	/**
	 * @return Whether or not to use StopForumSpam API for bot detection.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * @return Whether or not to reject connections if an error occurs while
	 *         using the StopforumSpam API
	 */
	public boolean shouldRejectOnError()
	{
		return rejectOnError;
	}
}
