// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.StreamConnection;
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

public class FileSelector
	extends Screen
{
	/**
	 * This is used when the user has dismissed the file selector without
	 * having any file selected.
	 */
	public static final Command CANCEL_COMMAND =
		new Command("Cancel", Command.CANCEL, 1, true);
	
	/** This is used when the user has selected a file. */
	public static final Command OK_COMMAND =
		new Command("Select", Command.OK, 0, true);
	
	public static final int DIRECTORY_CREATE =
		3;
	
	public static final int DIRECTORY_SELECT =
		2;
	
	public static final int LOAD =
		0;
	
	public static final int SAVE =
		1;
	
	public FileSelector(String __title, int __mode)
	{
		throw new todo.TODO();
	}
	
	public String[] getFilterExtensions()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getHeight()
	{
		return this.__defaultHeight();
	}
	
	public int getMode()
	{
		throw new todo.TODO();
	}
	
	public String getURL()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/24
	 */
	@Override
	public int getWidth()
	{
		return this.__defaultWidth();
	}
	
	public StreamConnection open(int __mode, boolean __to)
		throws ConnectionNotFoundException, IOException
	{
		throw new todo.TODO();
	}
	
	public void setFilterExtensions(String[] __ext)
	{
		throw new todo.TODO();
	}
	
	public void setMode(int __m)
	{
		throw new todo.TODO();
	}
	
	public void setURL(String __u)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIPersist __persist, UIStack __parent, UIStack __self,
		Graphics __g)
	{
		__g.drawString(this.getClass().getName().toString(),
			__g.getClipX(), __g.getClipY(), 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIPersist __keep, UIStack __parent)
	{
		throw new todo.TODO();
	}
}

