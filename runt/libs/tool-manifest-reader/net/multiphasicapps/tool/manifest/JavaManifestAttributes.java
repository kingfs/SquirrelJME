// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tool.manifest;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableMap;

/**
 * This contains the attributes for a single section within the manifest file.
 *
 * This class is immutable.
 *
 * @since 2016/05/20
 */
public final class JavaManifestAttributes
	extends AbstractMap<JavaManifestKey, String>
{
	/** The key value pairs. */
	protected final Map<JavaManifestKey, String> pairs;
	
	/**
	 * Initializes the manifest attributes.
	 *
	 * @param __from The map to copy from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	JavaManifestAttributes(Map<JavaManifestKey, String> __from)
		throws NullPointerException
	{
		// Check
		if (__from == null)
			throw new NullPointerException("NARG");
		
		// Copy
		this.pairs = UnmodifiableMap.<JavaManifestKey, String>of(
			new HashMap<>(__from));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		if (__o instanceof String)
			return this.pairs.containsKey(new JavaManifestKey((String)__o));
		return this.pairs.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public Set<Map.Entry<JavaManifestKey, String>> entrySet()
	{
		return this.pairs.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public String get(Object __o)
	{
		return this.pairs.get(__o);
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
	 * @since 2016/05/20
	 */
	@Override
	public int size()
	{
		return this.pairs.size();
	}
}
