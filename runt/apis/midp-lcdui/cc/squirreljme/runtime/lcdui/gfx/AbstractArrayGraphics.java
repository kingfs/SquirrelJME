// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.lcdui.font.SQFFont;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This is the base class for each individual array graphics implementation.
 *
 * @since 2018/03/25
 */
public abstract class AbstractArrayGraphics
	extends Graphics
{
	/** Clipping above. */
	private static final int _CLIP_ABOVE =
		1;
	
	/** Clipping below. */
	private static final int _CLIP_BELOW =
		2;
	
	/** Clip right. */
	private static final int _CLIP_RIGHT =
		4;
	
	/** Clip left. */
	private static final int _CLIP_LEFT =
		8;
	
	/** The width of the image. */
	protected final int width;
	
	/** The height of the image. */
	protected final int height;
	
	/** The pitch of the image. */
	protected final int pitch;
	
	/** The buffer offset. */
	protected final int offset;
	
	/** The number of pixels which exist per element. */
	protected final int pixelsperelement;
	
	/** The number of elements that consist of pixel data. */
	protected final int numelements;
	
	/** Physical end of the buffer. */
	protected final int lastelement;
	
	/** Is there an alpha channel? */
	protected final boolean hasalphachannel;
	
	/** Absolute translated X coordinate. */
	protected final int abstransx;
	
	/** Absolute translated Y coordinate. */
	protected final int abstransy;
	
	/** The current stroke style. */
	protected int strokestyle;
	
	/** Is a dot stroke being used? */
	protected boolean dotstroke;
	
	/** Translated X coordinate. */
	protected int transx;
	
	/** Translated Y coordinate. */
	protected int transy;
	
	/** The starting X clip. */
	protected int clipsx;
	
	/** The starting Y clip. */
	protected int clipsy;
	
	/** The ending X clip. */
	protected int clipex;
	
	/** The ending Y clip. */
	protected int clipey;
	
	/** The clip width. */
	protected int clipw;
	
	/** The clip height. */
	protected int cliph;
	
	/** The current font, null means default. */
	protected Font font;
	
	/** The current blending mode. */
	protected int blendmode;
	
	/** Could blending be done? */
	protected boolean candoblending;
	
	/** Is blending actually going to be done? */
	protected boolean doblending;
	
	/** The current color. */
	protected int color;
	
	/**
	 * Initializes the base graphics.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __p The image pitch.
	 * @param __o The buffer offset.
	 * @param __l The length of the buffer.
	 * @param __ppe Pixels per element.
	 * @param __alpha Is there an alpha channel?
	 * @param __atx Absolute X translation.
	 * @param __aty Absolute Y translation.
	 * @throws IllegalArgumentException If the width, height, or pitch are
	 * zero or negative; or pitch is lower than the width.
	 * @since 2018/03/25
	 */
	public AbstractArrayGraphics(int __w, int __h, int __p, int __o, int __l,
		int __ppe, boolean __alpha, int __atx, int __aty)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB07 Invalid width and/or height specified.}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("EB07");
		
		// {@squirreljme.error EB08 The pitch is less than the width.}
		if (__p < __w)
			throw new IllegalArgumentException("EB08");
		
		// {@squirreljme.error EB09 The specified parameters exceed the bounds
		// of the array. (The width; The height; The offset; The pitch;
		// The array length; The number of elements in the image)}
		int numelements = (__p * __h) / __ppe,
			lastelement = __o + numelements;
		if (__o < 0 || lastelement > __l)
			throw new ArrayIndexOutOfBoundsException(
				String.format("EB09 %d %d %d %d %d %d", __w, __h,
				__o, __p, __l, numelements));
		
		// Set
		this.width = __w;
		this.height = __h;
		this.pitch = __p;
		this.offset = __o;
		this.pixelsperelement = __ppe;
		this.numelements = numelements;
		this.lastelement = lastelement;
		this.hasalphachannel = __alpha;
		
		// Setup absolute translation and initialize the base translation
		// which is at the absolute origin
		this.abstransx = __atx;
		this.abstransy = __aty;
		this.transx = __atx;
		this.transy = __aty;
		
		// Initial clipping rectangle has the image bounds
		this.clipex = __w;
		this.clipey = __h;
	}
	
	/**
	 * Internally draws a bitmap character.
	 *
	 * @param __blend Is blending to be done?
	 * @param __color The color to draw.
	 * @param __dsx The drawing X coordinate.
	 * @param __dsy The drawing Y coordinate.
	 * @param __bmp The bitmap data.
	 * @param __bytesperscan The number of bytes per scanline.
	 * @param __scanoff The start X offset to start reading scan data from.
	 * @param __scanlen The number of X scans to draw.
	 * @param __lineoff The start Y offset (lines) to staart reading scans
	 * from.
	 * @param __linelen The number of scan lines to draw.
	 * @since 2018/12/01 
	 */
	protected abstract void internalDrawCharBitmap(boolean __blend,
		int __color, int __dsx, int __dsy, byte[] __bmp, int __bytesperscan,
		int __scanoff, int __scanlen, int __lineoff, int __linelen);
	
	/**
	 * Internal line draw.
	 *
	 * @param __blend Blended.
	 * @param __dot Dotted.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __ex The end X coordinate.
	 * @param __ey The end Y coordinate.
	 * @since 2018/11/18
	 */
	protected abstract void internalDrawLine(boolean __blend, boolean __dot,
		int __x, int __y, int __ex, int __ey);
	
	/**
	 * Internal rectangle fill.
	 *
	 * @param __blend Blend?
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __ex The end X coordinate.
	 * @param __ey The end Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/25
	 */
	protected abstract void internalFillRect(boolean __blend,
		int __x, int __y, int __ex, int __ey, int __w, int __h);
	
	/**
	 * Draws a primitive RGB tile.
	 *
	 * @param __b The source buffer containing RGB data.
	 * @param __o The offset into the buffer.
	 * @param __sl The scanline length in the source buffer.
	 * @param __x The destination X position.
	 * @param __y The destination Y position.
	 * @param __w The width of the tile.
	 * @param __h The height of the tile.
	 * @since 2017/02/11
	 */
	protected abstract void internalRGBTile(int[] __b, int __o, int __l,
		int __x, int __y, int __w, int __h);
	
	/**
	 * Internally sets the color to be used for drawing.
	 *
	 * @param __a The alpha level.
	 * @param __rgb The RGB color.
	 * @param __blend Is blending to be performed?
	 * @since 2018/03/25
	 */
	protected abstract void internalSetColor(int __a, int __rgb,
		boolean __blend);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		// Translate
		__x += this.transx;
		__y += this.transy;
		
		// Get right end coordinates
		int ex = __x + __w,
			ey = __y + __h;
		
		// Swap X if lower
		if (ex < __x)
		{
			int boop = __x;
			__x = ex;
			ex = boop;
		}
		
		// Same for Y
		if (ey < __y)
		{
			int boop = __y;
			__y = ey;
			ey = boop;
		}
		
		// Never go past the end of the viewport because pixels will never
		// be drawn in negative regions
		if (__x < 0)
			__x = 0;
		if (__y < 0)
			__y = 0;
		
		// Additionally do not go past the edge ever that way the end
		// clipping point is always valid
		int width = this.width,
			height = this.height;
		if (ex > width)
			ex = width;
		if (ey > height)
			ey = height;
		
		// Get the old clipping bounds
		int oldclipsx = this.clipsx,
			oldclipsy = this.clipsy,
			oldclipex = this.clipex,
			oldclipey = this.clipey;
		
		// Only set the clipping bounds if they exceed the previous ones
		if (__x > oldclipsx)
			this.clipsx = __x;
		if (__y > oldclipsy)
			this.clipsy = __y;
		if (ex < clipex)
			this.clipex = ex;
		if (ey < clipey)
			this.clipey = ey;
		
		// Set width/height
		this.clipw = ex - __x;
		this.cliph = ey - __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		this.__drawText(this.__buildText(
			Character.valueOf(__s).toString()), __x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		this.__drawText(this.__buildText(
			new String(__s, __o, __l)), __x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		// Translate all coordinates
		int transx = this.transx,
			transy = this.transy;
		__x1 += transx;
		__y1 += transy;
		__x2 += transx;
		__y2 += transy;
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex,
			clipey = this.clipey;
		
		// Perform Cohen-Sutherland line clipping
		for (;;)
		{
			// Determine points that lie outside the box
			int outa = AbstractArrayGraphics.__csOut(__x1, __y1,
					clipsx, clipsy, clipex - 1,clipey - 1),
				outb = AbstractArrayGraphics.__csOut(__x2, __y2, clipsx,
					clipsy, clipex - 1, clipey - 1);
			
			// Both points are outside the box, do nothing
			if ((outa & outb) != 0)
				return;
			
			// Both points are inside the box, use this line
			if (outa == 0 && outb == 0)
				break;
			
			// Only the second point is outside, swap the points so that the
			// first point is outside and the first is not
			if (outa == 0)
			{
				// Swap X
				int boop = __x1;
				__x1 = __x2;
				__x2 = boop;
				
				// Swap Y
				boop = __y1;
				__y1 = __y2;
				__y2 = boop;
				
				// Swap clip flags
				boop = outb;
				outb = outa;
				outa = boop;
			}
			
			// The point is clipped
			if (outa != 0)
			{
				// Differences of points
				int dx = __x2 - __x1,
					dy = __y2 - __y1;
				
				// Clips above the box
				if ((outa & _CLIP_ABOVE) != 0)
				{
					__x1 += dx * (clipey - __y1) / dy;
					__y1 = clipey - 1;
				}
			
				// Clips below
				else if ((outa & _CLIP_BELOW) != 0)
				{
					__x1 += dx * (clipsy - __y1) / dy;
					__y1 = clipsy;
				}
			
				// Clips the right side
				else if ((outa & _CLIP_RIGHT) != 0)
				{
					__y1 += dy * (clipex - __x1) / dx;
					__x1 = clipex - 1;
				}
			
				// Clips the left side
				else if ((outa & _CLIP_LEFT) != 0)
				{
					__y1 += dy * (clipsx - __x1) / dx;
					__x1 = clipsx;
				}
			}
		}
		
		// Have lines which always go to the right
		if (__x2 < __x1)
		{
			int boopx = __x1,
				boopy = __y1;
			__x1 = __x2;
			__y1 = __y2;
			__x2 = boopx;
			__y2 = boopy;
		}
		
		// The resulting line should never be out of bounds
		if (__x1 < clipsx || __x2 < clipsx || __y1 < clipsy || __y2 < clipsy ||
			__x1 > clipex || __x2 > clipex || __y1 > clipey || __y2 > clipey)
			return;
		
		// Forward depending on blending and/or dots
		try
		{
			this.internalDrawLine(this.doblending,
				(this.strokestyle == DOTTED), __x1, __y1, __x2, __y2);
		}
		
		// Exception happened when drawing a line
		catch (IndexOutOfBoundsException e)
		{
			todo.DEBUG.note("Line (%d, %d) -> (%d, %d)", __x1, __y1,
				__x2, __y2);
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		// Transform
		__x += this.transx;
		__y += this.transy;
		
		// Determine ending position
		int ex = __x + __w,
			ey = __y + __h;
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex,
			clipey = this.clipey;
		
		// Box is completely outside the bounds of the clip, do not draw
		if (ex < clipsx || __x >= clipex || ey < clipsy || __y >= clipey)
			return;
		
		// Determine sub-clipping area
		int subx = __x - clipsx,
			suby = __y - clipsy;
		
		// Clip into bounds
		if (__x < 0)
			__x = 0;
		if (__y < 0)
			__y = 0;
		if (ex >= clipex)
			ex = clipex;
		if (ey >= clipey)
			ey = clipey;
		
		// New tile size
		int tw = ex - __x,
			th = ey - __y;
		
		// We might have multiplied alpha blending, or just normal blending
		// If __alpha is true then this is 32-bit RGBA!
		if (this.doblending)
			if (__alpha)
				throw new todo.TODO();
			else
				throw new todo.TODO();
		else
			if (__alpha)
				throw new todo.TODO();
			else
				this.internalRGBTile(__data, __off, __scanlen,
					__x, __y, tw, th);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		// The width and height are increased by a single pixel
		__w += 1;
		__h += 1;
		
		// For now just cheat and draw four lines
		if (true)
		{
			int ex = __x + __w,
				ey = __y + __h;
			
			this.drawLine(__x, __y, ex, __y);
			this.drawLine(__x, ey, ex, ey);
			this.drawLine(__x, __y, __x, ey);
			this.drawLine(ex, __y, ex, ey);
			
			return;
		}
		
		// Get actual end points
		int ex = __x + __w,
			ey = __y + __h;
			
		// Translate all coordinates
		int transx = this.transx,
			transy = this.transy;
		__x += transx;
		__y += transy;
		ex += transx;
		ey += transy;
		
		// Force lower X
		if (ex < __x)
		{
			int boop = ex;
			ex = __x;
			__x = boop;
		}
		
		// Force lower Y
		if (ey < __y)
		{
			int boop = ey;
			ey = __y;
			__y = boop;
		}
		
		// Get clipping region
		int clipsx = this.clipsx, clipsy = this.clipsy,
			clipex = this.clipex,
			clipey = this.clipey;
		
		// Box is completely outside the bounds of the clip, do not draw
		if (ex < clipsx || __x >= clipex || ey < clipsy || __y >= clipey)
			return;
		
		// Left vertical shortening
		boolean lvs = (__x < clipsx);
		if (lvs)
			__x = clipsx;
		
		// Right vertical shortening
		boolean rvs = (ex >= clipex);
		if (rvs)
			ex = clipex;
		
		// Calculate new width
		if (lvs || rvs)
			__w = ex - __x;
		
		// Bottom horizontal shortening
		boolean bhs = (__y < clipsy);
		if (bhs)
			__y = clipsy;
		
		// Top horizontal shortening
		boolean ths = (ey >= clipey);
		if (ths)
			ey = clipey;
		
		// Calculate new height
		if (bhs || ths)
			__h = ey - __y;
		
		// Draw the box
		boolean blend = this.doblending;
		boolean dot = (this.strokestyle == DOTTED);
		
		if (!bhs)
			this.internalDrawLine(blend, dot, __x, __y, ex, __y);
		if (!ths)
			this.internalDrawLine(blend, dot, __x, ey, ex, ey);
		if (!lvs)
			this.internalDrawLine(blend, dot, __x, __y, __x, ey);
		if (!rvs)
			this.internalDrawLine(blend, dot, ex, __y, ex, ey);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawString(String __s, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.__drawText(this.__buildText(__s), __x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __s.length())
			throw new StringIndexOutOfBoundsException("IOOB");
		
		this.__drawText(this.__buildText(__s.substring(__o, __o + __l)),
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
	{
		this.__drawText(__t, __x, __y, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillRect(int __x, int __y, int __w, int __h)
	{
		// Get actual end points
		int ex = __x + __w,
			ey = __y + __h;
			
		// Translate all coordinates
		int transx = this.transx,
			transy = this.transy;
		__x += transx;
		__y += transy;
		ex += transx;
		ey += transy;
		
		// Force lower X
		if (ex < __x)
		{
			int boop = ex;
			ex = __x;
			__x = boop;
		}
		
		// Force lower Y
		if (ey < __y)
		{
			int boop = ey;
			ey = __y;
			__y = boop;
		}
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex - 1,
			clipey = this.clipey - 1;
		
		// Never clip past the left/top
		if (__x < clipsx)
			__x = clipsx;
		if (__y < clipsy)
			__y = clipsy;
		
		// Never clip past the right/bottom
		if (ex > clipex)
			ex = clipex;
		if (ey > clipey)
			ey = clipey;
		
		// Calculate actual dimensions used
		__w = ex - __x;
		__h = ey - __y;
		
		// Perform drawing
		this.internalFillRect(this.doblending, __x, __y, ex, ey, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getAlpha()
	{
		return (this.color >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getAlphaColor()
	{
		return this.color;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getBlendingMode()
	{
		return this.blendmode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getBlueComponent()
	{
		return (this.color) & 0xFF;
	}
	
	/**
	 * Returns the element in the input array which represents the end of the
	 * clipping rectangle.
	 *
	 * @return The element which contains the end of the clipping rectangle.
	 * @since 2018/03/28
	 */
	public final int getClipElementEnd()
	{
		// Subtract one from the Y because it is on the next row
		return this.offset + (((this.pitch * (this.clipey - 1)) +
			(this.clipex)) / this.pixelsperelement);
	}
	
	/**
	 * Returns the element in the input array which represents the start of
	 * the clipping rectangle.
	 *
	 * @return The element which contains the start of the clipping rectangle.
	 * @since 2018/03/28
	 */
	public final int getClipElementStart()
	{
		return this.offset + (((this.pitch * this.clipsy) + this.clipsx) /
			this.pixelsperelement);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipHeight()
	{
		return this.cliph;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipWidth()
	{
		return this.clipw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipX()
	{
		return this.clipsx - this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipY()
	{
		return this.clipsy - this.transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getColor()
	{
		return this.color & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getDisplayColor(int __rgb)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final Font getFont()
	{
		Font rv = this.font;
		if (rv == null)
			rv = Font.getDefaultFont();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getGrayScale()
	{
		return (getRedComponent() + getGreenComponent() +
			getBlueComponent()) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getGreenComponent()
	{
		return (this.color >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getRedComponent()
	{
		return (this.color >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getStrokeStyle()
	{
		return this.strokestyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getTranslateX()
	{
		return this.transx - this.abstransx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getTranslateY()
	{
		return this.transy - this.abstransy;
	}
	
	/**
	 * Resets all parameters of the graphics output.
	 *
	 * @param __clip If {@code true} then the clip is also reset.
	 * @since 2017/02/12
	 */
	public void resetParameters(boolean __clip)
	{
		// Clear translation
		this.transx = this.abstransx;
		this.transy = this.abstransy;
	
		// Reset clip also
		if (__clip)
		{
			int width = this.width,
				height = this.height;
			
			this.clipsx = 0;
			this.clipsy = 0;
			this.clipex = width;
			this.clipey = height;
			this.clipw = width;
			this.cliph = height;
		}
		
		// Always reset these
		this.setAlphaColor(0xFF000000);
		this.setBlendingMode(SRC_OVER);
		this.setStrokeStyle(SOLID);
		this.setFont(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlpha(int __a)
		throws IllegalArgumentException
	{
		this.setAlphaColor(__a, getRedComponent(), getGreenComponent(),
			getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __argb)
	{
		// Set the original color directly
		this.color = __argb;
		
		// Determine if blending is to be performed or it is just directly
		// setting values, blending is only performed if the alpha channel
		// is not fully opaque and blending is permitted
		int alpha = (__argb >>> 24);
		boolean doblending = (this.candoblending && alpha != 0xFF);
		
		// Set internal blend mode
		this.doblending = doblending;
		
		// Setting the color is internally implemented
		this.internalSetColor(alpha, __argb & 0xFFFFFF, doblending);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0a Color out of range. (Alpha; Red; Green;
		// Blue)}
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB0a %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this.setAlphaColor((__a << 24) | (__r << 16) | (__g << 8) | __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		boolean candoblending,
			oldcandoblending = this.candoblending;
		
		// Just use source pixels
		if (__m == SRC)
		{
			// {@squirreljme.error EB0b Cannot set the overlay blending mode
			// because this graphics context does not have the alpha channel.}
			if (!this.hasalphachannel)
				throw new IllegalArgumentException("EB0b");
			
			candoblending = false;
		}
		
		// Perform blending since this is the default mode
		else if (__m == SRC_OVER)
		{
			candoblending = true;
		}
		
		// {@squirreljme.error EB0c Unknown blending mode.}
		else
			throw new IllegalArgumentException("EB0c");
		
		// Set
		this.blendmode = __m;
		this.candoblending = candoblending;
		
		// If the blending mode has changed then possible blending tables
		// need to be recalculated accordingly
		if (candoblending != oldcandoblending)
			this.setAlphaColor(this.getAlphaColor());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setClip(int __x, int __y, int __w, int __h)
	{
		// Translate
		__x += this.transx;
		__y += this.transy;
		
		// Get right end coordinates
		int ex = __x + __w,
			ey = __y + __h;
		
		// Swap X if lower
		if (ex < __x)
		{
			int boop = __x;
			__x = ex;
			ex = boop;
		}
		
		// Same for Y
		if (ey < __y)
		{
			int boop = __y;
			__y = ey;
			ey = boop;
		}
		
		// Never go past the end of the viewport because pixels will never
		// be drawn in negative regions
		if (__x < 0)
			__x = 0;
		if (__y < 0)
			__y = 0;
		
		// Additionally do not go past the edge ever that way the end
		// clipping point is always valid
		int width = this.width,
			height = this.height;
		if (ex > width)
			ex = width;
		if (ey > height)
			ey = height;
		
		// Set
		this.clipsx = __x;
		this.clipsy = __y;
		this.clipex = ex;
		this.clipey = ey;
		
		// Set width/height
		this.clipw = ex - __x;
		this.cliph = ey - __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __rgb)
	{
		this.setAlphaColor((this.getAlphaColor() & 0xFF_000000) |
			(__rgb & 0x00_FFFFFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		this.setAlphaColor(getAlpha(), __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setFont(Font __a)
	{
		// Just set it
		this.font = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		this.setAlphaColor(getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setStrokeStyle(int __a)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0d Illegal stroke style.}
		if (__a != SOLID && __a != DOTTED)
			throw new IllegalArgumentException("EB0d");
		
		// Set
		this.strokestyle = __a;
		this.dotstroke = (__a == DOTTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		this.transx += __x;
		this.transy += __y;
	}
	
	/**
	 * Builds and returns a text object for usage.
	 *
	 * @param __s The string used.
	 * @return A new text object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	private final Text __buildText(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Get the font, or fallback to the default if it was not set
		Font font = this.getFont();
		
		// Setup, use a zero height for now since it will be calculated after
		// the font and such has been set
		Text rv = new Text(__s,
			font.stringWidth(__s), 0);
		
		// Set text properties
		if (font != null)
			rv.setFont(font);
		rv.setForegroundColor(this.color);
		
		// Set the height to the required height of the box now that the
		// parameters have been set
		rv.setHeight(rv.getRequiredHeight());
		
		return rv;
	}
	
	/**
	 * Draws the given text object.
	 *
	 * @param __t The text object to draw.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __anchor The, this just adjusts determines how the actual text
	 * box region is drawn. If baseline is used, Y is just offset by the
	 * baseline for the first character and not the entire block size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	final void __drawText(Text __t, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Translate to the displayed coordinate space
		__x += this.transx;
		__y += this.transy;
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex - 1,
			clipey = this.clipey - 1;
		
		// Wanting to draw a bunch of text completely out of the clip? Ignore
		if (__x >= clipex || __y >= clipey)
			return;
		
		// Trying to draw the text completely out of the clip as well?
		int textw = __t.getWidth(),
			texth = __t.getHeight(),
			tex = __x + textw,
			tey = __y + texth;
		if (tex < clipsx || tey < clipsy)
			return;
		
		// The text box acts as an extra clip, so force everything to clip
		// in there
		if (__x > clipsx)
			clipsx = __x;
		if (tex < clipex)
			clipex = tex;
		if (__y > clipsy)
			clipsy = __y;
		if (tey < clipey)
			clipey = tey;
		
		// Cache the default font in the event it is never changed ever
		Font lastfont = __t.getFont();
		SQFFont sqf = SQFFont.cacheFont(lastfont);
		byte[] bmp = new byte[sqf.charbitmapsize];
		int pixelheight = sqf.pixelheight,
			bitsperscan = sqf.bitsperscan;
		
		// Blending the text?
		boolean doblending = this.doblending;
		
		// Need to store the properties since drawing of the text will
		// change how characters are drawn
		int oldcolor = this.getAlphaColor();
		try
		{
			// Read in all the text characters
			int n = __t.getTextLength();
			String chars = __t.getText(0, n);
			
			// Draw each character according to their metrics
			int[] metrics = new int[4];
			for (int i = 0; i < n; i++)
			{
				// Ignore certain characters
				char c = chars.charAt(i);
				if (c == '\r' || c == '\n')
					continue;
				
				// Set color to the foreground color of this character
				this.setAlphaColor(__t.getForegroundColor(i));
				
				// Need to find the SQF for this font again?
				Font drawfont = __t.getFont(i);
				if (drawfont != lastfont)
				{
					lastfont = drawfont;
					sqf = SQFFont.cacheFont(lastfont);
					bmp = new byte[sqf.charbitmapsize];
					pixelheight = sqf.pixelheight;
					bitsperscan = sqf.bitsperscan;
				}
				
				// Get the metrics for the character
				__t.getCharExtent(i, metrics);
				
				// Calculate the draw position of the character
				int dsx = __x + metrics[0],
					dsy = __y + metrics[1],
					dex = dsx + metrics[2],
					dey = dsy + metrics[3];
				
				// Completely out of bounds, ignore because we cannot draw it
				// anyway
				if (dsx >= clipex || dex <= 0 ||
					dsy >= clipey || dey <= 0)
					continue;
				
				// Base scan offsets and such
				int scanoff = 0,
					scanlen = sqf.charWidth(c),
					lineoff = 0,
					linelen = pixelheight;
				
				// Off the left side?
				if (dsx < clipsx)
				{
					int diff = clipsx - dsx;
					scanoff += diff;
					scanlen -= diff;
					
					// Reset to clip bound
					dsx = clipsx;
				}
				
				// Off the right side
				if (dex > clipex)
					scanlen -= (dex - clipex);
				
				// Off the top?
				if (dsy < clipsy)
				{
					int diff = clipsy - dsy;
					lineoff += diff;
					linelen -= diff;
					
					// Reset to clip bound
					dsy = clipsy;
				}
				
				// Off the bottom
				if (dey > clipey)
					linelen -= (dey - clipey);
				
				// Draw the bitmap for the character
				int bps = sqf.loadCharBitmap(c, bmp);
				this.internalDrawCharBitmap(doblending, color,
					dsx, dsy, bmp, bps, scanoff, scanlen, lineoff, linelen);
			}
		}
		
		// Just in case revert properties
		finally
		{
			this.setAlphaColor(oldcolor);
		}
	}
	
	/**
	 * Determines the Cohen-Sutherland clipping flags.
	 *
	 * @param __x Input X coordinate.
	 * @param __y Input Y coordinate.
	 * @param __csx Clipping box starting X.
	 * @param __csy Clipping box starting Y.
	 * @param __cex Clipping box ending X.
	 * @param __cey Clipping box ending Y.
	 * @return The clipping bit flags.
	 * @since 2017/09/10
	 */
	private static final int __csOut(int __x, int __y, int __csx, int __csy,
		int __cex, int __cey)
	{
		int rv = 0;
		
		// Clips above or below?
		if (__y > __cey)
			rv |= _CLIP_ABOVE;
		else if (__y < __csy)
			rv |= _CLIP_BELOW;
		
		// Clips right or left?
		if (__x > __cex)
			rv |= _CLIP_RIGHT;
		else if (__x < __csx)
			rv |= _CLIP_LEFT;
		
		return rv;
	}
}

