// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCall;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgramType;
import net.multiphasicapps.squirreljme.runtime.syscall.SystemProgram;

/**
 * This class manages the bridge for the suite manager to the native program
 * manager.
 *
 * @since 2017/12/08
 */
final class __SystemSuiteManager__
	implements SuiteManager
{
	/** Internal lock for suite management. */
	protected final Object lock =
		new Object();
	
	/** Cached suites. */
	protected final Map<SystemProgram, Reference<Suite>> _suites =
		new WeakHashMap<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public void addSuiteListener(SuiteListener __sl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public Suite getSuite(String __vendor, String __name)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public SuiteInstaller getSuiteInstaller(byte[] __b, int __o,
		int __l, boolean __ignuplock)
		throws IllegalArgumentException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public SuiteInstaller getSuiteInstaller(String __url,
		boolean __ignuplock)
		throws IllegalArgumentException, SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public List<Suite> getSuites(SuiteType __t)
		throws IllegalArgumentException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// The system call can be masked to filter out unwanted suites
		int mask;
		if (__t == SuiteType.APPLICATION)
			mask = KernelProgramType.APPLICATION;
		else if (__t == SuiteType.LIBRARY)
			mask = KernelProgramType.LIBRARY;
		
		// {@squirreljme.error DG04 The specified suite type cannot be
		// listed. (The type)}
		else
			throw new IllegalArgumentException(String.format("DG04 %s", __t));
		
		// Lock so the suites are always up to date
		Suite[] rv;
		synchronized (this.lock)
		{
			SystemProgram[] programs = SystemCall.listPrograms(mask);
			int n = programs.length;
			
			// Return wrappers
			rv = new Suite[n];
			for (int i = 0; i < n; i++)
				rv[i] = __ofProgram(programs[i]);
		}
		
		// Since the returned set of programs is an array, just wrap the
		// returning array because it is faster than seting up the logic
		// for a new one.
		return Arrays.<Suite>asList(rv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public void removeSuite(Suite __s, boolean __ignuplock)
		throws IllegalArgumentException, SuiteLockedException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public void removeSuiteListener(SuiteListener __sl)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the suite which maps to the given program.
	 *
	 * @param __p The program to wrap.
	 * @return The suite for the given program.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/10
	 */
	final Suite __ofProgram(SystemProgram __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Use pre-existing suite when possible
		Map<SystemProgram, Reference<Suite>> suites = this._suites;
		synchronized (this.lock)
		{
			Reference<Suite> ref = suites.get(__p);
			Suite rv;
			
			if (ref == null || null == (rv = ref.get()))
				suites.put(__p, new WeakReference<>(
					(rv = new Suite(__p))));
			
			return rv;
		}
	}
}
