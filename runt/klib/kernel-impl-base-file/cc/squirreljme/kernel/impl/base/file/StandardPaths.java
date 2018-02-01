// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.runtime.cldc.OperatingSystemType;
import cc.squirreljme.runtime.cldc.SystemCall;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This class contains the standard set of paths which is where SquirrelJME's
 * data is stored.
 *
 * @since 2018/01/13
 */
public final class StandardPaths
{
	/**
	 * {@squirreljme.property cc.squirreljme.home=path
	 * This specifies the singular base for where the user configuration,
	 * library, and cache data is stored.}
	 */
	public static final String HOME_PROPERTY =
		"cc.squirreljme.home";
	
	/**
	 * {@squirreljme.env SQUIRRELJME_HOME=path
	 * This specifies the singular base for where the user configuration,
	 * library, and cache data is stored.}
	 */
	public static final String HOME_ENV =
		"SQUIRRELJME_HOME";
	
	/**
	 * {@squirreljme.property cc.squirreljme.path.config=path
	 * This specifies the location where configuration files are stored.}
	 */
	public static final String CONFIG_PATH_PROPERTY =
		"cc.squirreljme.path.config";
	
	/**
	 * {@squirreljme.env SQUIRRELJME_CONFIG_PATH=path
	 * This specifies the location where configuration files are stored.}
	 */
	public static final String CONFIG_PATH_ENV =
		"SQUIRRELJME_CONFIG_PATH";
	
	/**
	 * {@squirreljme.property cc.squirreljme.path.data=path
	 * This specifies the location where data files are stored.}
	 */
	public static final String DATA_PATH_PROPERTY =
		"cc.squirreljme.path.data";
	
	/**
	 * {@squirreljme.env SQUIRRELJME_DATA_PATH=path
	 * This specifies the location where data files are stored.}
	 */
	public static final String DATA_PATH_ENV =
		"SQUIRRELJME_DATA_PATH";
	/**
	 * {@squirreljme.property cc.squirreljme.path.cache=path
	 * This specifies the location where cache files are stored.}
	 */
	public static final String CACHE_PATH_PROPERTY =
		"cc.squirreljme.path.cache";
	
	/**
	 * {@squirreljme.env SQUIRRELJME_CACHE_PATH=path
	 * This specifies the location where cache files are stored.}
	 */
	public static final String CACHE_PATH_ENV =
		"SQUIRRELJME_CACHE_PATH";
	
	/** Determines the default paths to use. */
	public static final StandardPaths DEFAULT =
		__defaultPaths();
	
	/** The configuration path. */
	protected final Path config;
	
	/** The data path. */
	protected final Path data;
	
	/** The cache path. */
	protected final Path cache;
	
	/**
	 * Initializes a standard path using the specified path as a base for
	 * the required directories.
	 *
	 * @param __p The base path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	public StandardPaths(Path __p)
		throws NullPointerException
	{
		this(__p.resolve("config"), __p.resolve("data"), __p.resolve("cache"));
	}
	
	/**
	 * Initializes the standard path set.
	 *
	 * @param __conf The configuration directory.
	 * @param __data The data directory.
	 * @param __cache The cache directory.
	 * @throws NullPointerException On null arguments.
	 * @sicne 2018/01/13
	 */
	public StandardPaths(Path __conf, Path __data, Path __cache)
		throws NullPointerException
	{
		if (__conf == null || __data == null || __cache == null)
			throw new NullPointerException("NARG");
		
		this.config = __conf;
		this.data = __data;
		this.cache = __cache;
	}
	
	/**
	 * Determines how to get the default paths.
	 *
	 * @return The set of standard paths.
	 * @since 2018/01/13
	 */
	private static StandardPaths __defaultPaths()
	{
		Path[] rv = null;
		
		// Using a basic home path
		String basichome = Objects.toString(System.getProperty(HOME_PROPERTY),
			SystemCall.getEnv(HOME_ENV));
		if (basichome != null)
			rv = new Path[]{Paths.get(basichome)};
		
		// If no basic path was used, detect more paths
		if (rv == null)
			rv = __defaultPathsOs();
		
		// If only a single path is returned then expand it to
		// multiple ones
		if (rv.length == 1)
			rv = new Path[]
				{
					rv[0].resolve("config"),
					rv[1].resolve("data"),
					rv[2].resolve("cache"),
				};
		
		// Setup paths
		return new StandardPaths(
			__triple(rv[0], System.getProperty(CONFIG_PATH_PROPERTY),
				SystemCall.getEnv(CONFIG_PATH_ENV)),
			__triple(rv[1], System.getProperty(DATA_PATH_PROPERTY),
				SystemCall.getEnv(DATA_PATH_ENV)),
			__triple(rv[2], System.getProperty(CACHE_PATH_PROPERTY),
				SystemCall.getEnv(CACHE_PATH_ENV)));
	}
	
	/**
	 * Use paths specific to the operation system.
	 *
	 * @return The set of paths, either 1 for singular or 3 for config, data,
	 * and cache.
	 * @since 2018/01/31
	 */
	private static Path[] __defaultPathsOs()
	{
		// Based on OS
		OperatingSystemType ostype = SystemCall.operatingSystemType();
		
		Path userhome = StandardPaths.__getPropertyPath("user.home");
		Path userdir = StandardPaths.__getPropertyPath("user.dir");
		
		// Unix systems
		if (ostype.isUnix())
		{
			throw new todo.TODO();
		}
		
		// Unknown
		else
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Returns the path of a system property.
	 *
	 * @param __s The system property to get.
	 * @return The path of the property or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	private static Path __getPropertyPath(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		String v = System.getProperty(__s);
		if (v != null)
			return Paths.get(v);
		return null;
	}
	
	/**
	 * Prefers path A, then B, and if neither the default is used.
	 *
	 * @param __default The default path.
	 * @param __a The first path.
	 * @param __b The second path.
	 * @return The path to use.
	 * @since 2018/01/31
	 */
	private static Path __triple(Path __default, String __a, String __b)
	{
		if (__a != null)
			return Paths.get(__a);
		
		else if (__b != null)
			return Paths.get(__b);
		
		return __default;
	}
}

