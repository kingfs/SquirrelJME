// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

import java.io.PrintStream;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Main entry class for the TAC runner system.
 *
 * @since 2018/10/17
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments, used to filter tests.
	 * @since 2018/10/17
	 */
	public static void main(String... __args)
	{
		// Load arguments
		if (__args == null)
			__args = new String[0];
		Deque<String> args = new LinkedList<>();
		for (String s : __args)
			if (s != null)
				args.add(s);
		
		// Load the database
		Database db = Database.build();
		
		// List tests?
		if ("-l".equals(args.pollFirst()))
		{
			// Banner to standard error
			System.err.println("Available tests:");
			
			// Output the tests to standard output
			PrintStream out = System.out;
			for (SingleUnit u : db)
				out.println(u.fullName());
			
			// Stop
			return;
		}
		
		// Run each test
		int total = 0,
			pass = 0,
			fail = 0;
		boolean hasfailed = false;
		for (SingleUnit su : db)
		{
			boolean passed = su.run();
			
			// Keep track
			total++;
			if (!passed)
			{
				fail++;
				hasfailed = true;
			}
			else
				pass++;
		}
		
		// Note it
		System.err.printf("Ran %d tests: %d passed, %d failed.%n",
			total, pass, fail);
		
		// Exit with failure if there are bad tests
		if (hasfailed)
			System.exit(1);
	}
}

