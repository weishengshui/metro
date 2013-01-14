/**
 * 
 */
package com.chinarewards.metro.core.common;

import java.util.UUID;

/**
 * This class contain utility method related to UUID.
 * 
 */
public abstract class UUIDUtil {

	/**
	 * Generate a UUID. Current implementation generates a version 4 UUID.
	 * <p>
	 * 
	 * The generated UUID contains alphanumeric characters only, all alphabets
	 * are in lowercase.
	 * 
	 * @return
	 * @see UUID
	 */
	public final static String generate() {

		UUID uuid = UUID.randomUUID();
		String s = uuid.toString();
		// strip off hyphens
		s = s.replaceAll("-", "");
		return s;

	}

}
