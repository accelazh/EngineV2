package com.accela.SocketConnectionCenter.commandLine;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import com.accela.ConnectionCenter.ConnectionCenter;
import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.SocketConnectionCenter.SocketClientID;
import com.accela.SocketConnectionCenter.SocketConnectionCenter;
import com.accela.SynchronizeSupport.standard.AlreadyClosedException;
import com.accela.SynchronizeSupport.standard.ClosableBlockingQueue;
import com.accela.SynchronizeSupport.standard.FailedToCloseException;
import com.accela.SynchronizeSupport.standard.FailedToOpenException;

public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private final String[] commandList = new String[] { "close", "help",
			"displayAllInfo", "startToReceiveConnections",
			"stopToReceiveConnections", "isReceivingConnections",
			"getConnectionReceivingPort", "getLocalAddress",
			"openConnectionTo <IP> <port>", "closeConnectionTo <IP> <port>",
			"sendMessageTo <IP> <port> <message>", "hasConnectionOf",
			"getConnectedClients", "openBroadcastFunction",
			"closeBroadcastFunction", "isBroadcastFunctionOpen",
			"broadcastMessage <message>",
			"broadcastMessageOnPeriod <message> <period>",
			"cancelAllBroadcast", "getBroadcastPort",
			"setBroadcastPort <port>", "getBroadcastGroupAddress",
			"setBroadcastGroupAddress <IP>", "openConnectionCenter",
			"shutDownConnectionCenter", "isConnectionCenterShutDown", "exit",
			"clear connectionMessageArea", "clear broadcastMessageArea",
			"clear commandLineArea", "clear all", };

	private SocketConnectionCenter scc;

	private JTextArea connectionArea = new JTextArea();
	private JTextArea broadcastArea = new JTextArea();
	private CommandLineTextArea commandArea = new CommandLineTextArea();

	private boolean running = true;

	private ConnectionThread connectionThread;
	private BroadcastingThread broadcastingThread;
	private CommandThread commandThread;

	public MainFrame()
	{
		super("SocketConnectionCenter Command Line");
		scc = SocketConnectionCenter.createInstance(true);
		scc.setMessageBufferMaxSize(2);

		buildGUI();
		initGUI();

		initWorkingThreads();
	}

	private void buildGUI()
	{
		this.setLayout(new BorderLayout());

		// connectionArea.setRows(10);
		// connectionArea.setColumns(60);
		broadcastArea.setRows(4);
		// broadcastArea.setColumns(60);
		commandArea.setRows(18);
		// commandLineArea.setColumns(60);

		connectionArea.setEditable(false);
		broadcastArea.setEditable(false);
		commandArea.setEditable(true);

		JPanel backPanel = new JPanel(new BorderLayout());

		JPanel p1 = new JPanel(new BorderLayout());

		JPanel p11 = new JPanel(new BorderLayout());
		p11.add(new JScrollPane(connectionArea), BorderLayout.CENTER);
		p11.add(new JLabel("Connection Message Area: "), BorderLayout.NORTH);
		p1.add(p11, BorderLayout.CENTER);

		JPanel p12 = new JPanel(new BorderLayout());
		p12.add(new JScrollPane(broadcastArea), BorderLayout.CENTER);
		p12.add(new JLabel("Broadcast Message Area: "), BorderLayout.NORTH);
		p1.add(p12, BorderLayout.SOUTH);

		backPanel.add(p1, BorderLayout.CENTER);

		JPanel p2 = new JPanel(new BorderLayout());
		p2.add(new JScrollPane(commandArea), BorderLayout.CENTER);
		p2.add(new JLabel("Command Line Area: "), BorderLayout.NORTH);
		backPanel.add(p2, BorderLayout.SOUTH);

		// add command list
		JList list = new JList(commandList);

		list.setFixedCellHeight(20);

		JScrollPane listPane = new JScrollPane(list);
		listPane.setPreferredSize(new Dimension(200, 0));

		JPanel listPanel = new JPanel(new BorderLayout());
		listPanel.add(listPane, BorderLayout.CENTER);
		listPanel.add(new JLabel("Command List"), BorderLayout.NORTH);

		// add to the frame
		JPanel backBackPanel = new JPanel(new BorderLayout());
		backBackPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 10, 8));
		backBackPanel.add(backPanel, BorderLayout.CENTER);
		backBackPanel.add(listPanel, BorderLayout.WEST);

		this.getContentPane().add(backBackPanel, BorderLayout.CENTER);
	}

	private void initGUI()
	{
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
	}

	private void initWorkingThreads()
	{
		connectionThread = new ConnectionThread();
		broadcastingThread = new BroadcastingThread();
		commandThread = new CommandThread();

		connectionThread.start();
		broadcastingThread.start();
		commandThread.start();
	}

	private void processCommand(String command)
	{
		java.util.List<String> list = splitCommand(command);

		commandArea.println("Your command is: " + command);

		String commandHead = list.remove(0);
		String[] arguments = list.toArray(new String[0]);

		String result = executeCommand(commandHead, arguments);
		commandArea.println(result);
	}

	private void close() throws FailedToCloseException
	{
		running = false;
		scc.close();

		connectionThread.interrupt();
		broadcastingThread.interrupt();
		commandThread.interrupt();
	}

	private java.util.List<String> splitCommand(String command)
	{
		StringTokenizer tokens = new StringTokenizer(command);
		java.util.LinkedList<String> list = new java.util.LinkedList<String>();
		while (tokens.hasMoreTokens())
		{
			list.add(tokens.nextToken().trim());
		}

		return list;
	}

	private String executeCommand(String commandHead, String[] arguments)
	{
		if (commandHead.equals("close"))
		{
			try
			{
				close();
			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "Close succeed!";
		} else if (commandHead.equals("help"))
		{
			return "Command list: ... (this function is not completed)";
		} else if (commandHead.equals("startToReceiveConnections"))
		{
			try
			{
				scc.startToReceiveConnection();
			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "Successfully started!";
		} else if (commandHead.equals("stopToReceiveConnections"))
		{
			try
			{
				scc.stopToReceiveConnection();
			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "Successfully stopped!";
		} else if (commandHead.equals("isReceivingConnections"))
		{
			try
			{
				return "" + scc.isReceivingConnection();
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("getConnectionReceivingPort"))
		{
			try
			{
				return "port: " + scc.getConnectionReceivingPort();
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("getLocalAddress"))
		{
			try
			{
				return "local address: "
						+ InetAddress.getLocalHost().toString();
			} catch (UnknownHostException ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("openConnectionTo"))
		{
			try
			{
				if (arguments.length != 2)
				{
					throw new IllegalArgumentException(
							"openConnectionTo <IP> <port>");
				}

				InetAddress address = InetAddress.getByName(arguments[0]);
				int port = Integer.parseInt(arguments[1]);
				if (!scc.openConnection(new SocketClientID(address, port)))
				{
					return "Failed: connection already exists!";
				}

			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "Successfully opend connection!";

		} else if (commandHead.equals("closeConnectionTo"))
		{
			try
			{
				if (arguments.length != 2)
				{
					throw new IllegalArgumentException(
							"closeConnectionTo <IP> <port>");
				}

				InetAddress address = InetAddress.getByName(arguments[0]);
				int port = Integer.parseInt(arguments[1]);
				if (!scc.closeConnection(new SocketClientID(address, port)))
				{
					return "Failed: connection does not exist!";
				}

			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "Successfully closed connection!";

		} else if (commandHead.equals("sendMessageTo"))
		{
			try
			{
				if (arguments.length < 3)
				{
					throw new IllegalArgumentException(
							"sendMessageTo <IP> <port> <message>");
				}

				InetAddress address = InetAddress.getByName(arguments[0]);
				int port = Integer.parseInt(arguments[1]);

				String message = "";
				for (int i = 2; i < arguments.length; i++)
				{
					message += arguments[i] + " ";
				}
				
				scc.sendMessage(new SocketClientID(address, port), message);

			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "Successfully sent the message!";

		} else if (commandHead.equals("hasConnectionOf"))
		{
			boolean result = false;
			try
			{
				if (arguments.length != 2)
				{
					throw new IllegalArgumentException(
							"hasConnectionOf <IP> <port>");
				}

				InetAddress address = InetAddress.getByName(arguments[0]);
				int port = Integer.parseInt(arguments[1]);
				result = scc.hasConnectionOf(new SocketClientID(address, port));

			} catch (Exception ex)
			{
				return appendException(ex);
			}
			return "" + result;

		} else if (commandHead.equals("getConnectedClients"))
		{
			try
			{
				java.util.List<ClientID> ids = scc.getConnectedClientIDs();

				String out = "";
				for (ClientID id : ids)
				{
					assert (id instanceof SocketClientID);
					SocketClientID sid = (SocketClientID) id;
					out += sid.getAddress() + "\t" + sid.getPort() + "\n";
				}
				return out;
			} catch (Exception ex)
			{
				return appendException(ex);
			}

		} else if (commandHead.equals("openBroadcastFunction"))
		{
			try
			{
				scc.openBroadcastFunction();
				return "Successfully opened!";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("closeBroadcastFunction"))
		{
			try
			{
				scc.closeBroadcastFunction();
				return "Successfully closed!";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("isBroadcastFunctionOpen"))
		{
			try
			{
				boolean result = scc.isBroadcastFunctionOpen();
				return "" + result;
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("broadcastMessage"))
		{
			try
			{
				if (arguments.length < 1)
				{
					throw new IllegalArgumentException(
							"broadcastMessage <message>");
				}

				String message = "";
				for (int i = 0; i < arguments.length; i++)
				{
					message += arguments[i] + " ";
				}

				scc.broadcastMessage(message);
				return "successfully broadcasted!";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("broadcastMessageOnPeriod"))
		{
			try
			{
				if (arguments.length < 2)
				{
					throw new IllegalArgumentException(
							"broadcastMessageOnPeriod <message> <period>");
				}

				long period = Long.parseLong(arguments[arguments.length - 1]);
				String message = "";
				for (int i = 0; i < arguments.length - 1; i++)
				{
					message += arguments[i] + " ";
				}

				scc.broadcastMessage(message, period);
				return "successfully broadcasted periodly!";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("cancelAllBroadcast"))
		{
			try
			{
				scc.cancelAllBroadcast();
				return "Successfully canceled all broadcasts";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("getBroadcastPort"))
		{
			return "port: " + scc.getBroadcastPort();
		} else if (commandHead.equals("setBroadcastPort"))
		{
			try
			{
				if (arguments.length != 1)
				{
					throw new IllegalArgumentException(
							"setBroadcastPort <port>");
				}

				scc.setBroadcastPort(Integer.parseInt(arguments[0]));
				return "Successfully set broadcast port";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("setBroadcastGroupAddress"))
		{
			try
			{
				if (arguments.length != 1)
				{
					throw new IllegalArgumentException(
							"setBroadcastGroupAddress <IP>");
				}

				scc.setBroadcastGroupAddress(arguments[0]);
				return "Successfully set broadcast group address";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("getBroadcastGroupAddress"))
		{
			try
			{
				String result = scc.getBroadcastGroupAddress().toString();
				return "Broadcast group address: " + result;
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("shutDownConnectionCenter"))
		{
			try
			{
				scc.close();
				return "Successfully shut down!";
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("openConnectionCenter"))
		{
			try
			{
				scc.open();
				return "Successfully opened!";
			} catch (FailedToOpenException ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("isConnectionCenterShutDown"))
		{
			return "" + !scc.isOpen();
		} else if (commandHead.equals("displayAllInfo"))
		{
			try
			{
				String result = "";
				result += "Is receiving connections: "
						+ scc.isReceivingConnection() + "\n";
				try
				{
					result += "Connection receiving port: "
							+ scc.getConnectionReceivingPort() + "\n";
				} catch (AlreadyClosedException ex)
				{
					result += "Connection receiving port: "
							+ "connection receiving function not started yet"
							+ "\n";
				}
				result += "Local address: " + InetAddress.getLocalHost() + "\n";

				try
				{
					result += "Connection receiving Client ID: "
							+ scc.getConnectionReceivingClientID() + "\n";
				} catch (AlreadyClosedException ex)
				{
					result += "Connection receiving Client ID: "
							+ "connection receiving function not started yet"
							+ "\n";
				}

				result += "Connected clients: \n";
				java.util.List<ClientID> ids = scc.getConnectedClientIDs();
				String idInfo = "";
				for (ClientID id : ids)
				{
					assert (id instanceof SocketClientID);
					SocketClientID sid = (SocketClientID) id;
					idInfo += "\t" + sid.getAddress() + "\t" + sid.getPort()
							+ "\n";
				}
				result += idInfo;

				result += "Is broadcast function open: "
						+ scc.isBroadcastFunctionOpen() + "\n";
				result += "Broadcast port: " + scc.getBroadcastPort() + "\n";
				result += "Broadcast group address: "
						+ scc.getBroadcastGroupAddress() + "\n";
				result += "Is connection center shut down: " + !scc.isOpen()
						+ "\n";

				Field frq = ConnectionCenter.class
						.getDeclaredField("receivingQueue");
				Field fbrq = ConnectionCenter.class
						.getDeclaredField("broadcasterReceivingQueue");
				frq.setAccessible(true);
				fbrq.setAccessible(true);

				ClosableBlockingQueue<?> rq = (ClosableBlockingQueue<?>) frq
						.get(scc);
				ClosableBlockingQueue<?> brq = (ClosableBlockingQueue<?>) fbrq
						.get(scc);

				result += "receivingQueue size: " + rq.size() + "\n";
				result += "broadcasterReceivingQueue: " + brq.size() + "\n";

				result += "isUsingHPObjectStreams: "
						+ scc.isUsingHPObjectStreams();

				return result;
			} catch (Exception ex)
			{
				return appendException(ex);
			}
		} else if (commandHead.equals("exit"))
		{
			System.exit(1);
			return "Successfully exited";
		} else if (commandHead.equals("clear"))
		{
			if (arguments[0].equals("connectionMessageArea"))
			{
				connectionArea.setText(null);
			} else if (arguments[0].equals("broadcastMessageArea"))
			{
				broadcastArea.setText(null);
			} else if (arguments[0].equals("commandLineArea"))
			{
				commandArea.setText(null);
			} else if (arguments[0].equals("all"))
			{
				connectionArea.setText(null);
				broadcastArea.setText(null);
				commandArea.setText(null);
			} else
			{
				throw new IllegalArgumentException("illegal argument");
			}

			return "Successfully cleared!";
		} else
		{
			return "Invalid command!";
		}
	}

	private String appendException(String str, Throwable ex)
	{
		String out = str + "\nException:\n";
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		out += sw.toString();
		try
		{
			sw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		ex.printStackTrace();

		return out;
	}

	private String appendException(Throwable ex)
	{
		return appendException("Failed!", ex);
	}

	private class ConnectionThread extends Thread
	{
		public ConnectionThread()
		{
			super("MainFrame - ConnectionThread");
		}

		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					RemotePackage message = scc.retriveMessage();

					if (null == message)
					{
						assert (false);
						throw new NullPointerException("message is null");
					}

					connectionArea.append(message.toString());
					connectionArea.append("\n");
					connectionArea
							.scrollRectToVisible(connectionArea
									.modelToView(connectionArea.getText()
											.length() - 1));

				} catch (Exception ex)
				{
					System.out.println(ex.getClass().getName());
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException ex2)
					{
						ex2.printStackTrace();
					}
				}

			}
		}

	}

	private class BroadcastingThread extends Thread
	{
		public BroadcastingThread()
		{
			super("MainFrame - BroadcastingThread");
		}

		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					RemotePackage message = scc.retriveBroadcastMessage();

					if (null == message)
					{
						assert (false);
						throw new NullPointerException(
								"broadcast message is null");
					}

					broadcastArea.append(message.toString());
					broadcastArea.append("\n");
					broadcastArea.scrollRectToVisible(broadcastArea
							.modelToView(broadcastArea.getText().length() - 1));

				} catch (Exception ex)
				{
					System.out.println(ex.getClass().getName());
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException ex2)
					{
						ex2.printStackTrace();
					}
				}
			}
		}

	}

	private class CommandThread extends Thread
	{
		public CommandThread()
		{
			super("MainFrame - CommandThread");
		}

		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					String command = null;
					do
					{
						commandArea.print("<Command> ");
						command = commandArea.readLine().trim();
					} while (null == command || command.length() <= 0);

					if (null == command)
					{
						assert (false);
						throw new NullPointerException("command is null");
					}

					processCommand(command);

				} catch (Exception ex)
				{
					System.out.println(ex.getClass().getName());
					try
					{
						Thread.sleep(1000);
					} catch (InterruptedException ex2)
					{
						ex2.printStackTrace();
					}
				}
			}

		}
	}

	public static void main(String[] args)
	{
		MainFrame frame = new MainFrame();
		frame.setVisible(true);

	}

}
