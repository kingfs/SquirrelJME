// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.multiphasicapps.collections.IntegerList;

/**
 * This class contains static methods which can be used for manipulating
 * strings.
 *
 * @since 2017/11/23
 */
public final class StringUtils
{
	/**
	 * Not used.
	 *
	 * @since 2017/11/23
	 */
	private StringUtils()
	{
	}
	
	/**
	 * Splits the given string using the specified delimeters and outputs it
	 * to the given collection.
	 *
	 * @param __delim The delimeters to use.
	 * @param __s The string to split.
	 * @param __out The collection to place split strings into.
	 * @return {@code __out}
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public static final Collection<String> basicSplit(char[] __delim,
		String __s, Collection<String> __out)
		throws NullPointerException
	{
		if (__delim == null || __s == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Parse string
		boolean dows = true;
		int lastdelim = -2;
		for (int i = 0, n = __s.length(), mark = 0; i <= n; i++)
		{
			// -1 is a special delimeter for the end of string because
			// otherwise if the string does not end in a delimeter it will not
			// be found
			int c = (i == n ? -1 : __s.charAt(i));
			
			// Is this a delimeter
			if (c == lastdelim || c == -1 || __indexOf(__delim, (char)c) >= 0)
			{
				// Remember last delimeter for potential speed
				lastdelim = c;
				
				// If reading delimeters, clear flag and mark
				// to remember the current index
				if (dows)
				{
					dows = false;
					mark = i;
				}
				
				// Otherwise end of sequence, generate string
				else
				{
					// Split out
					__out.add(__s.substring(mark, i));
					
					// Switch to handling delimeters
					dows = true;
				}
			}
			
			// If reading delimeters, clear flag and mark
			// to remember the current index, is not delimeters
			// here
			else if (dows)
			{
				dows = false;
				mark = i;
			}
		}
		
		// Return output always
		return __out;
	}
	
	/**
	 * Splits the given string using the specified delimeters and outputs it
	 * to the given collection.
	 *
	 * @param __delim The delimeters to use.
	 * @param __s The string to split.
	 * @param __out The collection to place split strings into.
	 * @return {@code __out}
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public static final Collection<String> basicSplit(String __delim,
		String __s, Collection<String> __out)
		throws NullPointerException
	{
		if (__delim == null || __s == null || __out == null)
			throw new NullPointerException("NARG");
		
		return basicSplit(__delim.toCharArray(), __s, __out);
	}
	
	/**
	 * Splits the given string using the specified delimeters.
	 *
	 * @param __delim The delimeters to use.
	 * @param __s The string to split.
	 * @return The split sequence of strings.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public static final String[] basicSplit(char[] __delim, String __s)
		throws NullPointerException
	{
		if (__delim == null || __s == null)
			throw new NullPointerException("NARG");
		
		Collection<String> rv = basicSplit(__delim, __s,
			new ArrayList<String>());
		return rv.<String>toArray(new String[rv.size()]);
	}
	
	/**
	 * Splits the given string using the specified delimeters.
	 *
	 * @param __delim The delimeters to use.
	 * @param __s The string to split.
	 * @return The split sequence of strings.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	public static final String[] basicSplit(String __delim, String __s)
		throws NullPointerException
	{
		if (__delim == null || __s == null)
			throw new NullPointerException("NARG");
		
		return basicSplit(__delim.toCharArray(), __s);
	}
	
	/**
	 * Returns an array containing all of the indexes that the specified
	 * character appears in the given sequence.
	 *
	 * @param __s The sequence to check in.
	 * @parma __c The character to get the indexes for.
	 * @return An array containing the array indexes for the given character,
	 * if there are none then the array will be empty.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/26
	 */
	public static final int[] multipleIndexOf(CharSequence __s, char __c)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		IntegerList list = new IntegerList();
		
		// Find every character index
		for (int i = 0, n = __s.length(), lastdx = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Add index to list if found
			if (c == __c)
				list.addInteger(i);
		}
		
		// Finish
		return list.toIntegerArray();
	}
	
	/**
	 * Searches the input array to see if the given character is within the
	 * array.
	 *
	 * @param __a The array to check.
	 * @param __c The character to find in the array.
	 * @return The index of the character or {@code -1} if it was not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/23
	 */
	private static final int __indexOf(char[] __a, char __c)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		for (int i = 0, n = __a.length; i < n; i++)
			if (__c == __a[i])
				return i;
		return -1;
	}
}

