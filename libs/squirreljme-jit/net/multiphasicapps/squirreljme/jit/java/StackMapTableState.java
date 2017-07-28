// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a single state within the stack map table which contains
 * a listing of all of the types used for local and stack variable along with
 * the current depth of the stack.
 *
 * @since 2017/07/28
 */
public final class StackMapTableState
{
	/** The depth of the stack. */
	protected final int depth;
	
	/** Local variables. */
	private final JavaType[] _locals;
	
	/** Stack variables. */
	private final JavaType[] _stack;
	
	/**
	 * Initializes the stack map table state.
	 *
	 * @param __l Local variables.
	 * @param __s Stack variables.
	 * @param __d The depth of the stack.
	 * @throws JITException If the state is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public StackMapTableState(JavaType[] __l, JavaType[] __s, int __d)
		throws JITException, NullPointerException
	{
		// Check
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI1t The depth of the stack is not within the
		// bounds of the stack. (The stack depth; The stack size)}
		int ns = __s.length;
		if (__d < 0 || __d > ns)
			throw new JITException(String.format("JI1t %d %d", __d, ns));
		
		// Duplicate
		__l = __l.clone();
		__s = __s.clone();
		
		// Clear elements above the stack top
		for (int i = __d; i < ns; i++)
			__s[i] = null;
		
		// Verify each state
		__verify(__l);
		__verify(__s);
		
		// Set
		this._locals = __l;
		this._stack = __s;
		this.depth = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/28
	 */
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Verifies the types within the map.
	 *
	 * @param __t The types to check.
	 * @throws JITException If the type are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	private static void __verify(JavaType[] __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

