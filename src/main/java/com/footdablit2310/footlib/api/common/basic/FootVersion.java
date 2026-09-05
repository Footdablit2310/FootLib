package com.footdablit2310.footlib.api.common.basic;

import com.footdablit2310.footlib.exceptions.IllegalNullValueException;

public class FootVersion {
	private int overhaul;
	private int major;
	private int semiMajor;
	private int minor;
	private ReleaseChannel channel;
	public FootVersion(int overhaul, int major, int semiMajor, int minor,ReleaseChannel channel) {}
	public static ReleaseChannel stringToReleaseChannel(String name) {
		if (name.isBlank()) {
			throw new IllegalNullValueException("String cannot be empty.");
		} else if (name.toLowerCase().contains("release")) {
			return ReleaseChannel.RELEASE;
		} else if (name.toLowerCase().contains("beta")) {
			return ReleaseChannel.BETA;
		} else if (name.toLowerCase().contains("alpha")) {
			return ReleaseChannel.ALPHA;
		} else {
			throw new RuntimeException("Could not convert string to ReleaseChannel");
		}
	}
	public static String releaseChannelToString(ReleaseChannel channel) {
		switch (channel.toString().toLowerCase()) {
			case "release" -> { return "release"; }
			case "beta" -> { return "beta"; }
			case "alpha" -> { return "alpha"; }
			default -> throw new RuntimeException("Could not match channel to String");
		}
	}
	@Override
	public String toString() {
		return overhaul + "." + major + "." + semiMajor + "." + minor + "-" + releaseChannelToString(channel);
	}

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getOverhaul() {
		return overhaul;
	}

	public int getSemiMajor() {
		return semiMajor;
	}

	public ReleaseChannel getChannel() {
		return channel;
	}

	public void setChannel(ReleaseChannel channel) {
		this.channel = channel;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public void setOverhaul(int overhaul) {
		this.overhaul = overhaul;
	}

	public void setSemiMajor(int semiMajor) {
		this.semiMajor = semiMajor;
	}

	public enum ReleaseChannel {
		RELEASE,
		BETA,
		ALPHA
	}
}
