// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.util.ServiceLoader;

/**
 * This is the base class for builders which can generate binaries for a given
 * target.
 *
 * @since 2016/07/22
 */
public abstract class TargetBuilder
{
	/** Suggestions. */
	private final TargetSuggestion[] _suggestions;
	
	/**
	 * Initializes the target builder.
	 *
	 * @param __sugs The suggested build targets, a triplet followed by a
	 * short description.
	 * @since 2016/07/22
	 */
	public TargetBuilder(String... __sugs)
	{
		// Setup suggestions
		if (__sugs != null)
		{
			// Setup target
			int n = __sugs.length;
			TargetSuggestion[] tss = new TargetSuggestion[n];
			this._suggestions = tss;
			
			// Fill suggestions
			for (int i = 0; (i + 1) < n; i += 2)
			{
				throw new Error("TODO");
			}
		}
		
		// No suggestions
		else
			this._suggestions = new TargetSuggestion[0];
	}
	
	/**
	 * Returns the suggested targets.
	 *
	 * @return An array of suggested targets.
	 * @since 2016/07/22
	 */
	public final TargetSuggestion[] suggestedTargets()
	{
		return this._suggestions.clone();
	}
}

