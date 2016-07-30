// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This class contains the main controller for a given emulator.
 *
 * @since 2016/07/30
 */
public final class Emulator
	implements Closeable, Runnable
{
	/** Empty array. */
	private static final String[] _EMPTY_STRINGS =
		new String[0];
	
	/** The display output to use when. */
	final DisplayOutput _displayout;
	
	/** The volumes associated with the emulator. */
	final Map<String, Volume> _volumes;
	
	/**
	 * Initializes an emulator using the given builder.
	 *
	 * @param __eb The emulator builder to grab initial state from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	Emulator(EmulatorBuilder __eb)
		throws NullPointerException
	{
		// Check
		if (__eb == null)
			throw new NullPointerException("NARG");
		
		// Setup display output where console text goes
		DisplayOutput displayout = __eb._displayout;
		if (displayout == null)
			displayout = new DefaultDisplayOutput();
		this._displayout = displayout;
		
		// Setup volumes
		Map<String, Volume> volumes = new LinkedHashMap<>();
		for (Map.Entry<String, Volume> e : __eb._volumes.entrySet())
			volumes.put(e.getKey(), e.getValue());
		volumes = UnmodifiableMap.<String, Volume>of(volumes);
		this._volumes = volumes;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final void run()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Resolves the path of a file in the virtual volume (which uses ZIP
	 * compatible file names) to one that is valid on the filesystem.
	 *
	 * The file to be resolved need not actually exist.
	 *
	 * @param __v The volume to get the real path for.
	 * @param __sp The path in the volume to resolve a file for.
	 * @return The resolved path to the file as it appears to the emulated
	 * system.
	 * @throws IOException On read errors or if the path could not be resolved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public final String resolvePath(Volume __v, String __sp)
		throws IOException, NullPointerException
	{
		// Check
		if (__v == null || __sp == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Starts the given process using the given environment and set of
	 * arguments.
	 *
	 * @param __env The environment to use, the array is a multiple of two
	 * and appear in key/value pairs.
	 * @param __args The arguments to the process, the first argument is the
	 * name of the executable to be ran (if applicable).
	 * @return The created process.
	 * @since 2016/07/30
	 */
	public final Process startProcess(String[] __env, String... __args)
	{
		// Use empty array if these are not set
		if (__env == null)
			__env = _EMPTY_STRINGS;
		if (__args == null)
			__args = _EMPTY_STRINGS;
		
		throw new Error("TODO");
	}
}

