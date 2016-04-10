// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.util.AbstractList;

/**
 * This represents a verification state which on entry to an operation, the
 * specified state must be matched. If it is not matched then a verification
 * exception would be thrown to indicate badly verified code.
 *
 * Similar to the variable states, these are combined into one where locals are
 * first then followed by the stack elements.
 *
 * @since 2016/03/31
 */
public class CPVerifyState
	extends AbstractList<CPVariableType>
{
	/** The owning program. */
	protected final CPProgram program;
	
	/** Verification data. */
	private final byte[] _types;
	
	/** The top of the stack. */
	private volatile int _stacktop;
	
	/**
	 * Initializes the verification state.
	 *
	 * @param __prg The program which contains this state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	public CPVerifyState(CPProgram __prg)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
		
		// Get the number of element
		int count = (size() / 2) + 1;
		_types = new byte[count];
		_stacktop = program.maxLocals();
	}
	
	/**
	 * Initializes a verification state which is a copy of another.
	 *
	 * @param __copy The verification state to copy.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	public CPVerifyState(CPVerifyState __copy)
		throws NullPointerException
	{
		// Check
		if (__copy == null)
			throw new NullPointerException("NARG");
		
		// Copy data
		program = __copy.program;
		
		// Setup array using the original size count
		int count = (size() / 2) + 1;
		byte[] vt;
		_types = vt = new byte[count];
		
		// Copy the top of the stack
		_stacktop = __copy._stacktop;
		
		// Copy each item
		byte[] ot = __copy._types;
		for (int i = 0; i < count; i++)
			vt[i] = ot[i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/31
	 */
	@Override
	public CPVariableType get(int __i)
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// Is this a high or low reference?
		boolean hi = (__i & 1) != 0;
		int div = __i / 2;
		
		// Get value at the index
		byte val = _types[div];
		
		// Return which one?
		if (hi)
			return CPVariableType.byIndex(val >>> 4);
		return CPVariableType.byIndex(val & 0xF);
	}
	
	/**
	 * Returns the top of the stack.
	 *
	 * @return The top of the stack.
	 * @since 2016/03/31
	 */
	public int getStackTop()
	{
		return _stacktop;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/31
	 */
	@Override
	public CPVariableType set(int __i, CPVariableType __vt)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		// Is this a high or low reference?
		boolean hi = (__i & 1) != 0;
		int div = __i / 2;
		
		// Get value at the index
		byte[] tips = _types;
		byte val = tips[div];
		
		// Old value
		CPVariableType old = CPVariableType.byIndex((hi ? val >>> 4 :
			val & 0xF));
		
		// Set the new value
		if (hi)
			val = (byte)((val & 0x0F) | (__vt.ordinal() << 4));
		else
			val = (byte)((val & 0xF0) | __vt.ordinal());
		
		// Place it
		tips[div] = val;
		
		// Return the old
		return old;
	}
	
	/**
	 * Sets the top of the stack.
	 *
	 * @param __t The top of the stack.
	 * @return {@code this}.
	 * @throws IndexOutOfBoundsException If the specified stack top is out of
	 * bounds.
	 * @since 2016/03/31
	 */
	public CPVerifyState setStackTop(int __t)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__t < program.maxLocals() || __t > size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __t));
		
		_stacktop = __t;
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/31
	 */
	@Override
	public int size()
	{
		return program.variableCount();
	}
}

