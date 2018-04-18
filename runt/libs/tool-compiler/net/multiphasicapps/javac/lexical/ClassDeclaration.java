// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.lexical;

import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This repersents the definition of a class.
 *
 * @since 2018/04/10
 */
public abstract class ClassDeclaration
	extends ClassOrInterfaceDeclaration
{
	/**
	 * Parses a class declaration.
	 *
	 * @param __m Modifiers to the class.
	 * @param __t The input token source.
	 * @return The parsed type.
	 * @throws LexicalStructureException If the class is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/15
	 */
	public static final ClassDeclaration parseClass(Modifier[] __m,
		ExpandingSource __t)
		throws LexicalStructureException, NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Try to parse as a normal class
		try (ExpandingSource split = __t.split())
		{
			return NormalClassDeclaration.parseNormalClass(__m, split);
		}
		
		// Could not parse as normal class
		catch (LexicalStructureException e)
		{
			// Try parsing as enumeration
			try (ExpandingSource split = __t.split())
			{
				return EnumDeclaration.parseEnum(__m, split);
			}
			
			// Could not parse as enumeration
			catch (LexicalStructureException f)
			{
				// {@squirreljme.error AQ36 Could not parse as a normal class
				// or enumeration.}
				LexicalStructureException t = new LexicalStructureException(
					__t, "AQ38");
				
				// Make these suppressed so they always appear
				t.addSuppressed(e);
				t.addSuppressed(f);
				
				throw t;
			}
		}
	}
}
