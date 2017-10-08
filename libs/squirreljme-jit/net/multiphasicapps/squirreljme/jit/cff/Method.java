// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This represents a method which is used to execute byte code.
 *
 * @since 2017/09/30
 */
public final class Method
	extends Member
{
	/** The version of the class. */
	protected final ClassVersion version;
	
	/** The flags for class. */
	protected final ClassFlags classflags;
	
	/** The name of the current class. */
	protected final ClassName classname;
	
	/** The constant pool. */
	protected final Pool pool;
	
	/** The flags for the current method. */
	protected final MethodFlags methodflags;
	
	/** The name of the method. */
	protected final MethodName methodname;
	
	/** The type of the method. */
	protected final MethodDescriptor methodtype;
	
	/** The code attribute data, which is optional. */
	private final byte[] _rawcodeattr;
	
	/**
	 * Initializes the method.
	 *
	 * @param __ver The class version.
	 * @param __cf The class flags.
	 * @param __tn The name of the class.
	 * @param __pool The constant pool.
	 * @param __mf The method flags.
	 * @param __mn The name of the method.
	 * @param __mt The method type.
	 * @param __mc An optional byte array representing the code attribute, the
	 * value is used directly.
	 * @throws NullPointerException On null arguments except for {@code __mc}.
	 * @since 2017/09/30
	 */
	Method(ClassVersion __ver, ClassFlags __cf, ClassName __tn, Pool __pool,
		MethodFlags __mf, MethodName __mn, MethodDescriptor __mt, byte[] __mc)
		throws NullPointerException
	{
		if (__ver == null || __cf == null || __tn == null || __pool == null ||
			__mf == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.version = __ver;
		this.classflags = __cf;
		this.classname = __tn;
		this.pool = __pool;
		this.methodflags = __mf;
		this.methodname = __mn;
		this.methodtype = __mt;
		this._rawcodeattr = __mc;
	}
		
	/**
	 * Decodes all methods from the input class data.
	 *
	 * @param __ver The version of the class.
	 * @param __tn The name of the owning class.
	 * @param __cf The flags for the owning class.
	 * @param __pool The constant pool for the class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/30
	 */
	public static Method[] decode(ClassVersion __ver, ClassName __tn,
		ClassFlags __cf, Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__ver == null || __tn == null || __cf == null || __pool == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		int nm = __in.readUnsignedShort();
		Method[] rv = new Method[nm];
		Set<NameAndType> dup = new HashSet<>();
		
		// Parse fields
		for (int i = 0; i < nm; i++)
		{
			MethodFlags flags = new MethodFlags(__cf,
				__in.readUnsignedShort());
			MethodName name = new MethodName(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			MethodDescriptor type = new MethodDescriptor(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			
			// {@squirreljme.error JI2v A duplicate method exists within the
			// class. (The method name; The method descriptor)}
			if (!dup.add(new NameAndType(name.toString(), type.toString())))
				throw new InvalidClassFormatException(String.format(
					"JI2v %s %s", name, type));
			
			// Handle attributes
			int na = __in.readUnsignedShort();
			String[] attr = new String[1];
			int[] alen = new int[1];
			byte[] code = null;
			for (int j = 0; j < na; j++)
				try (DataInputStream ai = ClassFile.__nextAttribute(__in,
					__pool, attr, alen))
				{
					// Only care about the code attribute
					if (!"Code".equals(attr[0]))
						continue;
			
					// {@squirreljme.error JI1b The specified method
					// contains more than one code attribute. (The current
					// class; The method name; The method type)}
					if (code != null)
						throw new InvalidClassFormatException(String.format(
							"JI1b %s %s %s", __tn, name, type));
					
					// Copy bytes
					int rlen = alen[0];
					code = new byte[rlen];
					ai.readFully(code, 0, rlen);
				}
		
			// {@squirreljme.error JI1c The specified method does not have
			// the correct matching for abstract and if code exists or not.
			// (The current
			// class; The method name; The method type; The method flags)}
			if ((code == null) != (flags.isAbstract() | flags.isNative()))
				throw new InvalidClassFormatException(String.format(
					"JI1c %s %s %s", __tn, name, type, flags));
			
			// Create
			rv[i] = new Method(__ver, __cf, __tn, __pool, flags, name, type,
				code);
		}
		
		// All done!
		return rv;
	}
}
