// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class represents the sections that would exist in the output executable
 * such as the text and data sections. The bulk of the executable would
 * primarily only be generated containing all of the sections which are
 * contained here.
 *
 * @since 2017/06/15
 */
public class Sections
	extends __SubState__
{
	/** The sections which exist. */
	private final Map<SectionType, Section> _sections =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the section handler.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/06/15
	 */
	Sections(Reference<LinkerState> __ls)
		throws NullPointerException
	{
		super(__ls);
	}
	
	/**
	 * Obtains the specified section.
	 *
	 * @param __t The section to get.
	 * @return The section or {@code null} if it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/28
	 */
	public final Section get(SectionType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return this._sections.get(__t);
	}
}
