// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is an object which acts as an array, which stores some kind of data.
 *
 * @since 2018/09/15
 */
public final class SpringArrayObject
	implements SpringObject
{
	/** The monitor for this array. */
	protected final SpringMonitor monitor =
		new SpringMonitor();
	
	/** The type of this object itself. */
	protected final SpringClass selftype;
	
	/** The component type. */
	protected final SpringClass component;
	
	/** The length of the array. */
	protected final int length;
	
	/** Elements in the array. */
	private final Object[] _elements;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the array.
	 *
	 * @param __self The self type.
	 * @param __cl The component type.
	 * @param __l The array length.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNegativeArraySizeException If the array size is negative.
	 * @since 2018/09/15
	 */
	public SpringArrayObject(SpringClass __self, SpringClass __cl, int __l)
		throws NullPointerException
	{
		if (__self == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BK16 Attempt to allocate an array of a
		// negative size. (The length requested)}
		if (__l < 0)
			throw new SpringNegativeArraySizeException(
				String.format("BK16 %d", __l));
		
		this.selftype = __self;
		this.component = __cl;
		this.length = __l;
		this._elements = new Object[__l];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringMonitor monitor()
	{
		return this.monitor;
	}
	
	/**
	 * Returns the length of this array.
	 *
	 * @return The array length.
	 * @since 2018/09/16
	 */
	public final int length()
	{
		return this.length;
	}
	
	/**
	 * Sets the index to the specified value.
	 *
	 * @param __dx The index to set.
	 * @param __v The value to set.
	 * @throws SpringArrayStoreException If the array cannot store the given
	 * type.
	 * @throws SpringArrayIndexOutOfBoundsException If the index is not within
	 * bounds.
	 * @since 2018/09/16
	 */
	public final void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException
	{
		// {@squirreljme.error BK1h Out of bounds access to array. (The index;
		// The length of the array)}
		int length = this.length;
		if (__dx < 0 || __dx >= length)
			throw new SpringArrayStoreException(String.format("BK1h %d %d",
				__dx, length));
		
		// {@squirreljme.error BK1i The specified type is not compatible
		// with the values this array stores. (The input value;
		// The component type)}
		SpringClass component = this.component;
		if (!component.isCompatible(__v))
			throw new SpringArrayIndexOutOfBoundsException(String.format(
				"BK1i %s %s", __v, component));
		
		// Set
		this._elements[__dx] = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"%s@%08x", this.selftype.name(),
				System.identityHashCode(this))));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final SpringClass type()
	{
		return this.selftype;
	}
}
