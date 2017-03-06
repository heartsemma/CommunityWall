package com.github.heartsemma.communityWall.configuration.bots;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class BotScoutConfig
{
	// Sets default values.
	public BotScoutConfig()
	{
		enabled = true;
		rejectOnError = true;
		rejectOnNoUses = false;
	}

	@Setting(value = "Enabled", comment = "Whether or not to use BotScout to check users for personhood.")
	private boolean enabled;

	@Setting(value = "API_Key", comment = "The API key that CommunityWall should use for this user. Getting one from botscout.com is recommended if you need to check a lot of users.")
	private String apiKey;

	@Setting(value = "Reject_On_Error", comment = "True if CommunityWall should reject users if an error occurs during BotScouts tests, false if not.")
	private boolean rejectOnError;

	@Setting(value = "Reject_On_Excessive_Uses", comment = "True if CommunityWall should reject users if the server has run out of checks on botscout.com, false if not.")
	private boolean rejectOnNoUses;

	/**
	 * @return Whether or not to use the BotScout API for bot detection.
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * @return The botscout.com API key, to be used with inquiries to bypass the
	 *         100-checks-per-day-limit.
	 */
	public String getAPIKey()
	{
		return apiKey;
	}

	/**
	 * @return Whether or not to reject connections if an error occurs in trying
	 *         to detect a bot using the botscout.com APIf
	 */
	public boolean shouldRejectOnError()
	{
		return rejectOnError;
	}

	/**
	 * @return Whether or not to reject connections if the botscout API has no
	 *         more uses available.
	 */
	public boolean shouldRejectOnNoUses()
	{
		return rejectOnNoUses;
	}

}