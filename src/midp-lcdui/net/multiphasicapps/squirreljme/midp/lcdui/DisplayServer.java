// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * This is a class which implements the display server used by the LCDUI
 * interface. The client implementation communicates with this server.
 *
 * @since 2016/10/11
 */
public abstract class DisplayServer
	implements Runnable
{
	/** The URI the client uses to connect. */
	public static final String CLIENT_URI =
		"imc://*:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"0.0.2;authmode=false";
	
	/** The URI the server uses to host. */
	private static final String _SERVER_URI =
		"imc://:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"0.0.2;authmode=false";
	
	/** The display server thread. */
	protected final Thread thread;
	
	/** The server socket. */
	private final StreamConnectionNotifier _serversock;
	
	/** All the displays that are available. */
	private final Map<Byte, VirtualDisplay> _displays =
		new HashMap<>();
	
	/** A connection count, just for threading. */
	private volatile int _conncount;
	
	/**
	 * Initializes the base display server using the default server.
	 *
	 * @since 2016/10/11
	 */
	public DisplayServer()
		throws IOException
	{
		this((StreamConnectionNotifier)Connector.open(_SERVER_URI));
	}
	
	/**
	 * Initializes the base display server using the given connection.
	 *
	 * @param __sc The connection to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	public DisplayServer(StreamConnectionNotifier __sc)
		throws NullPointerException
	{
		// Check
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		// Setup display server thread
		Thread thread;
		this.thread = (thread = new Thread(this, "SquirrelJMEDisplayServer"));
		
		// Server specific thread modification?
		modifyThread(thread);
		
		// Set server socket
		this._serversock = __sc;
		
		// Start it
		thread.start();
	}
	
	/**
	 * Queries all of the displays that are currently available.
	 *
	 * @return An array of the available virtual displays.
	 * @since 2016/10/14
	 */
	protected abstract VirtualDisplay[] queryDisplays();
	
	/**
	 * This may be used by an implementation of the display server to modify
	 * the thread behavior.
	 *
	 * The default implementation does nothing.
	 *
	 * @param __t The thread to modify.
	 * @since 2016/10/11
	 */
	protected void modifyThread(Thread __t)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	public final void run()
	{
		// Infinite loop
		try (StreamConnectionNotifier svsock = this._serversock)
		{
			for (;;)
				try (StreamConnection sock = svsock.acceptAndOpen())
				{
					// Create thread for the given socket
					Thread t = new Thread(new __Socket__(sock),
						"SquirrelJMEDisplayConnection-" + (++_conncount));
				
					// Start it
					t.start();
				}
			
				// Failed read, ignore
				catch (IOException e)
				{
				}
		}
		
		// {@squirreljme.error EB05 Fail to close the display server socket.}
		catch (IOException e)
		{
			throw new RuntimeException("EB05", e);
		}
	}
	
	/**
	 * Binds the stream to the given display and performs looping on it
	 *
	 * @param __in The input data source.
	 * @param __out The data output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/15
	 */
	void __commandBindDisplay(DataInputStream __in, DataInputStream __out)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		// Read ID
		byte id = __in.readByte();
		
		// Lock
		Map<Byte, VirtualDisplay> displays = this._displays;
		VirtualDisplay bind;
		synchronized (displays)
		{
			// Get binding
			bind = displays.get(id);
			
			// {@squirreljme.error EB0c The specified display does not
			// exist. (The display ID)}
			if (bind == null)
				throw new RuntimeException(String.format("EB0c %d", id));
		}
		
		// Loop it
		bind.outputLoop(__out);
	}
	
	/**
	 * Outputs the number of displays which are currently available to this
	 * display server.
	 *
	 * @param __out The output stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	void __commandNumDisplays(DataOutputStream __out)
		throws IOException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Lock displays available
		Map<Byte, VirtualDisplay> displays = this._displays;
		synchronized (displays)
		{
			// Get displays
			VirtualDisplay[] qds = queryDisplays();
			
			// Write the display count
			__out.write(qds.length);
			
			// Go through all the displays
			for (VirtualDisplay vd : qds)
			{
				// Make sure it is in the map
				Byte id = Byte.valueOf(vd.id());
				if (!displays.containsKey(id))
					displays.put(id, vd);
				
				// Write it to the output stream
				__out.write(id);
				
				// Write properties
				for (DisplayProperty p : DisplayProperty.values())
				{
					// Ignore the stop property
					byte ord = (byte)p.ordinal();
					if (ord == 0)
						continue;
					
					// Write ordinal and its data
					__out.write(ord);
					__out.writeInt(vd.property(p));
				}
				
				// End properties
				__out.write(0);
			}
		}
	}
	
	/**
	 * A display socket that has been initialized.
	 *
	 * @since 2016/10/13
	 */
	private final class __Socket__
		implements Runnable
	{
		/** The input for the socket. */
		protected final DataInputStream in;
		
		/** The output to the remote end. */
		protected final DataOutputStream out;
		
		/**
		 * Initializes the socket connection to a display.
		 *
		 * @param __sock The socket connection.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/13
		 */
		private __Socket__(StreamConnection __sock)
			throws IOException, NullPointerException
		{
			// Check
			if (__sock == null)
				throw new NullPointerException("NARG");
			
			// Open input and output
			this.in = __sock.openDataInputStream();
			this.out = __sock.openDataOutputStream();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/10/13
		 */
		@Override
		public void run()
		{
			// Handle input
			try (DataInputStream in = this.in;
				DataOutputStream out = this.out)
			{
				// Handle every command possible
__outer:
				for (;;)
				{
					// Read a command and handle it
					int command = in.readUnsignedByte();
					switch (command)
					{
							// Request the displays that are available and
							// are attached
						case DisplayProtocol.COMMAND_REQUEST_NUMDISPLAYS:
							__commandNumDisplays(out);
							break;
						
							// Bind to a given display
							// Execution does not continue if this method
							// exits or closes otherwise
						case DisplayProtocol.COMMAND_BIND_DISPLAY:
							__commandBindDisplay(in, out);
							break __outer;
						
							// {@squirreljme.error EB07 Unknown display server
							// command. (The command ID)}
						default:
							throw new RuntimeException(String.format("EB07 %s",
								command));
					}
				
					// Flush the output so the commands are written
					out.flush();
				}
			}
			
			// {@squirreljme.error EB06 Read/write error handling display
			// command.}
			catch (IOException e)
			{
				throw new RuntimeException("EB06", e);
			}
			
			// Make sure cleanup happens
			finally
			{
			}
		}
	}
}

