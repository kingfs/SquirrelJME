// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

/**
 * This is a single record which stores multiple tracks of data.
 *
 * Vinyls have a single lock on them.
 *
 * @see VinylTrack
 * @since 2018/12/13
 */
public abstract class VinylRecord
{
	/**
	 * Locks this record so only a single set of actions can be performed on
	 * them, even for the same thread.
	 *
	 * @return The lock used to eventually unlock, to be used with
	 * try-with-resources.
	 * @since 2018/12/14
	 */
	public abstract VinylLock lock();
}

