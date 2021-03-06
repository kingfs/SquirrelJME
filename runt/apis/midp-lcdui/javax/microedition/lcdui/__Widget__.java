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

import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.ui.UIDrawable;
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

/**
 * This class acts as the lowest base for displays and items.
 *
 * @since 2018/03/23
 */
abstract class __Widget__
	extends __Drawable__
{
	/** The parent widget being used. */
	volatile __Widget__ _parent;
	
	/** The last UI width. */
	volatile int _lastuiwidth;
	
	/** The last UI height. */
	volatile int _lastuiheight;
	
	/**
	 * Updates the UI stack.
	 *
	 * @param __keep Persistent UI data.
	 * @param __parent The parent draw stack.
	 * @since 2018/12/08
	 */
	abstract void __updateUIStack(UIPersist __keep, UIStack __parent);
	
	/**
	 * Returns the current display.
	 *
	 * @return The current display.
	 * @since 2018/11/18
	 */
	final Display __currentDisplay()
	{
		// Since the widget might be part of a tabbed pane the parent will
		// technically not be a display, so recursively go up until one is
		// reached
		for (__Widget__ w = this._parent; w != null; w = w._parent)
			if (w instanceof Display)
				return (Display)w;
		
		return null;
	}
	
	/**
	 * Returns the default height.
	 *
	 * @return The default height.
	 * @since 2018/11/18
	 */
	final int __defaultHeight()
	{
		__Widget__ parent = this._parent;
		if (parent == null)
			return Display.getDisplays(0)[0].getHeight();
		return this._lastuiheight;
	}
	
	/**
	 * Returns the default width.
	 *
	 * @return The default width.
	 * @since 2018/11/18
	 */
	final int __defaultWidth()
	{
		__Widget__ parent = this._parent;
		if (parent == null)
			return Display.getDisplays(0)[0].getWidth();
		return this._lastuiwidth;
	}
	
	/**
	 * This is called when a key action has been performed. This should be
	 * overridden as needed.
	 *
	 * @param __type The key type.
	 * @param __kc The key code.
	 * @param __ch The character code.
	 * @param __time The time code.
	 * @since 2018/12/02
	 */
	@SerializedEvent
	void __doKeyAction(int __type, int __kc, char __ch, int __time)
	{
	}
	
	/**
	 * This is called when a pointer action happens.
	 *
	 * @param __type The event type.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __time The time code.
	 * @since 2018/12/02
	 */
	@SerializedEvent
	void __doPointerAction(int __type, int __x, int __y, int __time)
	{
	}
	
	/**
	 * This is called when the widget has its shown changed, this may be
	 * overridden as it is needed.
	 *
	 * @param __shown Is this shown?
	 * @since 2018/12/01
	 */
	void __doShown(boolean __shown)
	{
	}
	
	/**
	 * The bit that is used to check support.
	 *
	 * @return The bit for support.
	 * @since 2018/11/17
	 */
	int __supportBit()
	{
		throw new todo.OOPS(this.getClass().getName());
	}
}

