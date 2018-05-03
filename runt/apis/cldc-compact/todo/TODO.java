// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.system.SystemCall;
import java.io.PrintStream;

/**
 * This exception is thrown.
 *
 * When constructed, this exception does not normall finish execution.
 *
 * @since 2017/02/28
 */
public class TODO
	extends Error
{
	/**
	 * Initializes the exception, prints the trace, and exits the program.
	 *
	 * @since 2017/02/28
	 */
	public TODO()
	{
		// Print a starting banner
		PrintStream ps = System.err;
		ps.println("*******************************************************");
		ps.println("INCOMPLETE CODE HAS BEEN REACHED:");
		
		// Print the trace
		printStackTrace(ps);
		
		// Ending banner
		ps.println("*******************************************************");
		
		// {@squirreljme.property
		// cc.squirreljme.notodoexit=(boolean)
		// If this is {@code true} then the ToDo exception will not tell the
		// virtual machine to exit.}
		if (!Boolean.valueOf(
			System.getProperty("cc.squirreljme.notodoexit")))
			try
			{
				System.exit(3);
			}
		
			// Ignore
			catch (SecurityException e)
			{
			}
	}
	
	/**
	 * Specifies that the integer value is missing.
	 *
	 * @return An integer, but is not returned from.
	 * @since 2017/10/27
	 */
	public static final int missingInteger()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Specifies that the object value is missing.
	 *
	 * @param <T> The object to miss.
	 * @return Should return that object, but never does.
	 * @since 2017/10/24
	 */
	public static final <T> T missingObject()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Prints a note to standard error about something that is incomplete.
	 *
	 * @param __fmt The format string.
	 * @param __args The arguments to the call.
	 * @since 2018/04/02
	 */
	public static final void note(String __fmt, Object... __args)
	{
		// Print it out
		PrintStream ps = System.err;
		ps.print("TODO -- ");
		ps.print(TODO.__formatCondensedTrace(TODO.__where()));
		ps.print(" -- ");
		ps.printf(__fmt, __args);
		ps.println();
	}
	
	/**
	 * Formats a call trace element in a condensed form.
	 *
	 * @param __e The element to print out.
	 * @return The condensed string.
	 * @since 2018/05/03
	 */
	static final String __formatCondensedTrace(CallTraceElement __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		String cn = __e.className(),
			mn = __e.methodName(),
			md = __e.methodDescriptor(),
			fi = __e.file();
		long ad = __e.address();
		int ln = __e.line();
		
		StringBuilder sb = new StringBuilder();
		
		// Location
		if (cn != null)
		{
			// Get identifier part
			int ld = cn.lastIndexOf('.');
			if (ld < 0)
				ld = 0;
			
			// Print slimmed down packages since they could be guessed
			for (int i = 0, n = cn.length(); i >= 0 && i < n;)
			{
				// Before the last dot, print single and skip ahead
				if (i < ld)
				{
					sb.append(cn.charAt(i));
					sb.append('.');
					
					i = cn.indexOf('.', i) + 1;
				}
				
				// Finish string
				else
				{
					sb.append(cn.substring(i));
					break;
				}
			}
		}
		sb.append("::");
		if (mn != null)
			sb.append(mn);
		if (md != null)
			sb.append(md);
		
		// Address, if it is valid
		if (ad != -1L)
			sb.append(String.format("@0x%016X", ad));
		
		// Add file/line information if it is valid
		if (fi != null)
		{
			sb.append(" (");
			
			// Use blank class name if not specified
			if (cn == null)
				cn = "";
			
			// The class will start with a package, so drop that
			int ld = cn.lastIndexOf('.');
			if (ld >= 0)
				cn = cn.substring(ld + 1);
			
			// Determine how many charcters the class name and the file
			// name have in common, so that it can be shortened
			int deep = 0;
			for (int n = Math.min(cn.length(), fi.length()); deep < n; deep++)
				if (cn.charAt(deep) != fi.charAt(deep))
					break;
			
			// Use whole name
			if (deep == 0)
				sb.append(fi);
			
			// Use fragment
			else
			{
				// But only if it does not end in .java, this makes it
				// implied since it is always around
				String sub = fi.substring(deep);
				if (!sub.equals(".java"))
				{
					sb.append('*');
					sb.append(sub);
				}
			}
			
			if (ln >= 0)
			{
				sb.append(':');
				sb.append(ln);
			}
			
			sb.append(')');
		}
		
		return sb.toString();
	}
	
	/**
	 * Formats the call trace element.
	 *
	 * @param __e The element to format.
	 * @return The formatted string.
	 * @since 2018/04/02
	 */
	static final String __formatTrace(CallTraceElement __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		String cn = __e.className(),
			mn = __e.methodName(),
			md = __e.methodDescriptor(),
			fi = __e.file();
		long ad = __e.address();
		int ln = __e.line();
		
		StringBuilder sb = new StringBuilder();
		
		// Location
		if (cn != null)
			sb.append(cn);
		sb.append("::");
		if (mn != null)
			sb.append(mn);
		if (md != null)
			sb.append(md);
		
		// Address, if it is valid
		if (ad != -1L)
			sb.append(String.format("@0x%016X", ad));
		
		// Add file/line information if it is valid
		if (fi != null)
		{
			sb.append(" (");
			sb.append(fi);
			
			if (ln >= 0)
			{
				sb.append(':');
				sb.append(ln);
			}
			
			sb.append(')');
		}
		
		return sb.toString();
	}
	
	/**
	 * Determines where the code is in the call stack.
	 *
	 * @return The call trace element for the current location.
	 * @since 2018/04/02
	 */
	static final CallTraceElement __where()
	{
		return TODO.__where(new Throwable());
	}
	
	/**
	 * Determines where the code is in the call stack.
	 *
	 * @param __t The throwable to get the trace from.
	 * @return The call trace element for the given location.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/02
	 */
	static final CallTraceElement __where(Throwable __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Get the first one which is not in this class
		for (CallTraceElement e : SystemCall.EASY.throwableGetStack(__t))
		{
			String cn = e.className();
			if (cn == null)
				cn = "";
			
			if (!cn.startsWith("todo."))
				return e;
		}
		
		// Unknown
		return new CallTraceElement();
	}
}

