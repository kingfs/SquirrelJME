// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.classfile;

import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.StackMapTableState;
import net.multiphasicapps.scrf.CodeLocation;
import net.multiphasicapps.scrf.RegisterLocation;
import net.multiphasicapps.scrf.SummerFormatException;

/**
 * This class contains the state of the Java stack, it gets initialized to
 * a format which is used for conversion to SummerCoat.
 *
 * @since 2019/02/16
 */
public final class JavaState
{
	/** Local variables. */
	private final Slot[] _locals;
	
	/** Stack variables. */
	private final Slot[] _stack;
	
	/** The current top of the stack. */
	private int _stacktop;
	
	/**
	 * Initializes the base Java state.
	 *
	 * @param __nl The number of local variables.
	 * @param __ns The number of stack variables.
	 * @since 2019/02/16
	 */
	public JavaState(int __nl, int __ns)
	{
		// Virtual register index counter
		int vr = 2;
		
		// Initialize locals
		Slot[] locals = new Slot[__nl];
		for (int i = 0; i < __nl; i++)
			locals[i] = new Slot(vr++);
		this._locals = locals;
		
		// Initialize stack
		Slot[] stack = new Slot[__ns];
		for (int i = 0; i < __ns; i++)
			stack[i] = new Slot(vr++);
		this._stack = stack;
	}
	
	/**
	 * Loads the Java state from the given stack map table state.
	 *
	 * @param __smt The state to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final void fromState(StackMapTableState __smt)
		throws NullPointerException
	{
		if (__smt == null)
			throw new NullPointerException("NARG");
		
		Slot[] locals = this._locals;
		Slot[] stack = this._stack;
		
		// Copy locals
		for (int i = 0, n = locals.length; i < n; i++)
			locals[i]._type = __smt.getLocal(i).type();
		
		// Copy the stack
		int depth = __smt.depth();
		for (int i = 0; i < depth; i++)
			stack[i]._type = __smt.getStack(i).type();
		this._stacktop = depth;
	}
		
	/**
	 * Obtains the information in a local variable.
	 *
	 * @param __dx The local variable index.
	 * @return The result of the operation.
	 * @since 2019/02/16
	 */
	public final JavaStateResult localGet(int __dx)
	{
		Slot sl = this._locals[__dx];
		return new JavaStateResult(sl._type,
			sl.register.asWide(sl._type.isWide()));
	}
	
	/**
	 * Sets the local variable to the given type.
	 *
	 * @param __dx The index to set.
	 * @param __t The type to store.
	 * @return The result of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final JavaStateResult localSet(int __dx, JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set base register
		Slot[] locals = this._locals;
		Slot sl;
		(sl = locals[__dx])._type = __t;
		
		// Set top for wide local?
		boolean wide;
		if ((wide = __t.isWide()))
			locals[__dx + 1]._type = __t.topType();
		
		// Just a narrow set
		return new JavaStateResult(__t, sl.register.asWide(wide));
	}
	
	/**
	 * Pops a single value from the stack.
	 *
	 * @return The result of the pop.
	 * @since 2019/02/16
	 */
	public final JavaStateResult stackPop()
	{
		int stacktop = this._stacktop;
		Slot[] stack = this._stack;
		
		// {@squirreljme.error AV03 Stack underflow.}
		if ((--stacktop) < 0)
			throw new SummerFormatException("AV03");
		
		// Read from top of stack, also handle wide values
		Slot at = stack[stacktop];
		boolean wide;
		if ((wide = at._type.isTop()))
			at = stack[--stacktop];
		
		// Set new stack top
		this._stacktop = stacktop;
		
		return new JavaStateResult(at._type, at.register.asWide(wide));
	}
	
	/**
	 * Pushes the given type to the stack.
	 *
	 * @param __t The type to push.
	 * @return The result of the push.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public final JavaStateResult stackPush(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AV04 Stack overflow.}
		int stacktop = this._stacktop;
		Slot[] stack = this._stack;
		if (stacktop >= stack.length)
			throw new SummerFormatException("AV04");
		
		// Just needs simple set of type
		Slot at = stack[stacktop];
		at._type = __t;
		
		// Set required top type
		boolean wide;
		if ((wide = __t.isWide()))
		{
			Slot top = stack[++stacktop];
			top._type = __t.topType();
		}
		
		// Store new top
		this._stacktop = stacktop + 1;
		
		// Build result
		return new JavaStateResult(__t, at.register.asWide(wide));
	}
	
	/**
	 * Represents a slot within Java locals or the stack.
	 *
	 * @since 2019/02/16
	 */
	public static final class Slot
	{
		/** Virtual register index. */
		protected final RegisterLocation register;
		
		/** The Java type stored here. */
		private JavaType _type;
		
		/**
		 * Initializes the slot.
		 *
		 * @param __vr The virtual register index.
		 * @since 2019/02/16
		 */
		private Slot(int __vr)
		{
			this.register = new RegisterLocation(__vr);
		}
	}
}

