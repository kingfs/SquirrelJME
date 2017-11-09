// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.manifest.mutable;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;

/**
 * This is a mutable set of attributes which exist within a manifest.
 *
 * @since 2016/09/19
 */
public class MutableJavaManifestAttributes
	extends AbstractMap<JavaManifestKey, String>
{
	/** The manifest values. */
	protected final Map<JavaManifestKey, String> values =
		new LinkedHashMap<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public final Set<Map.Entry<JavaManifestKey, String>> entrySet()
	{
		return this.values.entrySet();
	}
	
	/**
	 * Returns the value used by the given key.
	 *
	 * @param __s The key to get the value for.
	 * @return The value for the given key or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public String getValue(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Find it
		return get(new JavaManifestKey(__s));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/19
	 */
	@Override
	public String put(JavaManifestKey __k, String __v)
		throws NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return this.values.put(__k, __v);
	}
	
	/**
	 * Places the specified key with the given value into the manifest.
	 *
	 * @param __k The key to use.
	 * @param __v The value for that key.
	 * @return The old value.
	 * @since 2016/12/27
	 */
	public String putValue(String __k, String __v)
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return put(new JavaManifestKey(__k), __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/29
	 */
	@Override
	public String remove(Object __k)
	{
		return this.values.remove(__k);
	}
}
