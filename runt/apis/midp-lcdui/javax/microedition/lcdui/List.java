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

import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import cc.squirreljme.runtime.lcdui.common.CommonColors;
import cc.squirreljme.runtime.lcdui.common.CommonMetrics;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.ui.UIEventTranslate;
import cc.squirreljme.runtime.lcdui.ui.UIPersist;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

public class List
	extends Screen
	implements Choice
{
	/** The default select command used for lists. */
	public static final Command SELECT_COMMAND =
		new Command("Select", Command.SCREEN, 0, true);
	
	/** Items on the list. */
	final __VolatileList__<__ChoiceEntry__> _items =
		new __VolatileList__<>();
	
	/** The type of list this is. */
	private final int _type;
	
	/** Last persist. */
	UIPersist _lastpersist;
	
	/** The focal index. */
	volatile int _focalindex;
	
	/**
	 * Initializes the list.
	 *
	 * @param __title The list title.
	 * @param __type The type of list this is.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @since 2018/11/16
	 */
	public List(String __title, int __type)
		throws IllegalArgumentException
	{
		this(__title, __type, new String[0], new Image[0]);
	}
	
	/**
	 * Initializes the list.
	 *
	 * @param __title The list title.
	 * @param __type The type of list this is.
	 * @param __strs The initial string elements to add.
	 * @param __imgs The initial image elements to add.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NullPointerException If the string elements is null or contains
	 * a null element.
	 * @since 2018/11/16
	 */
	public List(String __title, int __type, String[] __strs, Image[] __imgs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__strs == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB25 String and image elements differ in
		// size.}
		if (__imgs != null && __strs.length != __imgs.length)
			throw new IllegalArgumentException("EB25");
		
		// {@squirreljme.error EB26 Invalid list type. (The list type)}
		if (__type != Choice.IMPLICIT && __type != Choice.EXCLUSIVE &&
			__type != Choice.MULTIPLE)
			throw new IllegalArgumentException("EB26 " + __type);
		
		// Set
		this._title = __title;
		this._type = __type;
		
		// Append all of the items to the list
		for (int i = 0, n = __strs.length; i < n; i++)
			this.append(__strs[i], (__imgs == null ? null : __imgs[i]));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public int append(String __s, Image __i)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Append item
		__ChoiceEntry__ e;
		__VolatileList__<__ChoiceEntry__> items = this._items;
		int rv = items.append((e = new __ChoiceEntry__(__s, __i)));
		
		// If this is the only item and this is an exclusive list, select it
		if (items.size() == 1 && this._type == Choice.EXCLUSIVE)
			e._selected = true;
		
		// Visual changed, need to recalculate
		UIPersist lastpersist = this._lastpersist;
		if (lastpersist != null)
			lastpersist.visualUpdate(true);
		
		return rv;
	}
	
	public void delete(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Deletes all of the items in the list.
	 *
	 * @since 2018/11/17
	 */
	public void deleteAll()
	{
		this._items.clear();
	}
	
	public int getFitPolicy()
	{
		throw new todo.TODO();
	}
	
	public Font getFont(int __a)
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
	
	public Image getImage(int __a)
	{
		throw new todo.TODO();
	}
	
	public int getSelectedFlags(boolean[] __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public int getSelectedIndex()
	{
		// Multiple choice is always invalid
		if (this._type == Choice.MULTIPLE)
			return -1;
		
		// Find the first entry!
		int at = 0;
		for (__ChoiceEntry__ e : this._items)
		{
			if (e._selected)
				return at;
			at++;
		}
		
		// Not found
		return -1;
	}
	
	public String getString(int __a)
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
	
	public void insert(int __a, String __b, Image __c)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean isEnabled(int __i)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean isSelected(int __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public void removeCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void set(int __a, String __b, Image __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setEnabled(int __i, boolean __e)
	{
		this._items.get(__i)._disabled = !__e;
		
		// Visual changed, need to update
		UIPersist lastpersist = this._lastpersist;
		if (lastpersist != null)
			lastpersist.visualUpdate(false);
	}
	
	public void setFitPolicy(int __a)
	{
		throw new todo.TODO();
	}
	
	public void setFont(int __a, Font __b)
	{
		throw new todo.TODO();
	}
	
	public void setSelectCommand(Command __a)
	{
		throw new todo.TODO();
	}
	
	public void setSelectedFlags(boolean[] __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@Override
	public void setSelectedIndex(int __i, boolean __e)
		throws IndexOutOfBoundsException
	{
		this._items.get(__i)._selected = true;
		
		// Visual changed, need to update
		UIPersist lastpersist = this._lastpersist;
		if (lastpersist != null)
			lastpersist.visualUpdate(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/10
	 */
	@Override
	public int size()
	{
		return this._items.size();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/09
	 */
	@SerializedEvent
	@Override
	void __doKeyAction(int __type, int __kc, char __ch, int __time)
	{
		// Ignore these
		if (__type != NativeDisplayEventCallback.KEY_PRESSED &&
			__type != NativeDisplayEventCallback.KEY_REPEATED)
			return;
		
		// Flag for repaint?
		boolean repaint = false;
		
		// Depends on the game action used
		int ga;
		switch ((ga = UIEventTranslate.keyCodeToGameAction(__kc)))
		{
				// Focus up/down
			case Canvas.UP:
			case Canvas.DOWN:
				{
					__VolatileList__<__ChoiceEntry__> items = this._items;
					int nlen = items.size(),
						oldfocalindex = this._focalindex,
						newfocalindex = oldfocalindex +
							(ga == Canvas.UP ? -1 : 1);
					
					// If in range, adjust
					if (newfocalindex >= 0 && newfocalindex < nlen)
					{
						// Set new focal
						this._focalindex = newfocalindex;
						
						// Implicit items always follow the focal index
						if (this._type == Choice.IMPLICIT &&
							oldfocalindex != newfocalindex &&
							oldfocalindex >= 0 && oldfocalindex < nlen &&
							newfocalindex >= 0 && newfocalindex < nlen)
						{
							items.get(oldfocalindex)._selected = false;
							items.get(newfocalindex)._selected = true;
						}
						
						// Do a repaint!
						repaint = true;
					}
				}
				break;
				
				// Select item (or toggle select, depends on the list type)
			case Canvas.FIRE:
				{
					// Get items and the current selected item
					__VolatileList__<__ChoiceEntry__> items = this._items;
					int nlen = items.size(),
						focalindex = this._focalindex;
					
					// Depends on the list type
					switch (this._type)
					{
							// Only a single item
						case Choice.EXCLUSIVE:
							if (focalindex >= 0 && focalindex < nlen)
							{
								__ChoiceEntry__ e = items.get(focalindex);
								
								// Disabled items cannot be selected
								if (!e._disabled)
								{
									// Clear all items
									int at = 0;
									for (__ChoiceEntry__ f : items)
										f._selected = ((at++) == focalindex);
									
									// Is updated
									repaint = true;
								}
							}
							break;
						
							// Multiple choice
						case Choice.MULTIPLE:
							if (focalindex >= 0 && focalindex < nlen)
							{
								__ChoiceEntry__ e = items.get(focalindex);
								
								// If this is disabled then it cannot be
								// selected
								if (!e._disabled)
								{
									e._selected = !e._selected;
									repaint = true;
								}
							}
							break;
						
							// Do nothing
						default:
							break;
					}
				}
				
				break;
				
				// Nothing
			default:
				break;
		}
		
		// Visually update if there were changes
		UIPersist lastpersist = this._lastpersist;
		if (lastpersist != null)
			lastpersist.visualUpdate(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __draw(UIPersist __persist, UIStack __parent, UIStack __self,
		Graphics __g)
	{
		// Draw background of the list
		__g.setAlphaColor(CommonColors.BACKGROUND);
		__g.fillRect(0, 0, __self.drawwidth, __self.drawheight);
		
		// Focus on this item
		int focalindex = this._focalindex;
		java.util.List<UIStack> kids = __self.kids;
		int klen = kids.size();
		if (focalindex >= 0 && focalindex < klen - 1)
			__persist.focalstack = kids.get(1 + focalindex);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	int __supportBit()
	{
		return Display.SUPPORTS_LISTS;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	final void __updateUIStack(UIPersist __keep, UIStack __parent)
	{
		// We need this for future updates
		this._lastpersist = __keep;
		
		// Setup our list stack
		int rw = __parent.reservedwidth;
		UIStack stack = new UIStack(this, rw, __parent.reservedheight);
		__parent.add(stack);
		
		// Add each item in the list to be drawn
		for (__ChoiceEntry__ e : this._items)
		{
			// Need the height of the font to draw this item
			Font f = e._font;
			int fh = (f == null ? Font.getDefaultFont() : f).getHeight();
			
			// Add to the stack
			UIStack s;
			stack.add((s = new UIStack(e, rw, fh)));
		}
	}
}

