// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.kernel.server.ServerCaller;

/**
 * This supports system calls on the kernel itself.
 *
 * @since 2018/01/03
 */
public class JavaServerCaller
	extends ServerCaller
{
	/**
	 * Initializes the server caller.
	 *
	 * @param __k The kernel to call into.
	 * @since 2018/01/03
	 */
	public JavaServerCaller(JavaKernel __k)
	{
		super(__k);
	}
}
