package com.accela.SocketConnectionCenter.commandLine;

import java.awt.event.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import javax.swing.*;

public class CommandLineTextArea extends JTextArea
implements KeyListener
{
	private static final long serialVersionUID = 1L;
	
	private BlockingQueue<Character> readyBuffer=new LinkedBlockingQueue<Character>();
	
	private ReentrantLock lock=new ReentrantLock();
	
	public CommandLineTextArea()
	{
		this.addKeyListener(this);
	}
	
	public String readLine() throws InterruptedException
	{
		lock.lock();
		
		try
		{
			StringBuffer out=new StringBuffer();
			char c=0;
			while((c=readyBuffer.take())!='\n')
			{
				out.append(c);
			}
			
			return out.toString();
		}finally
		{
			lock.unlock();
		}
	}
	
	public void print(String s)
	{
		this.append(s);
		moveFocus();
	}
	
	public void println(String s)
	{
		this.append(s+"\n");
		moveFocus();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		//moveFocusWithKey(e);
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		//moveFocusWithKey(e);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(this.isFocusOwner())
		{
			if(e.getKeyChar()==KeyEvent.VK_ENTER)
			{
				int startIdx=this.getText().lastIndexOf(">")+2;
				String text=this.getText().substring(startIdx).trim();
				
				System.out.println("Input Command: "+text);
				
				for(Character c : text.toCharArray())
				{
					readyBuffer.add(c);
				}
				readyBuffer.add('\n');
			}
			
		}
	}
	
	private void moveFocus()
	{
		this.setCaretPosition(this.getText().length());
	}
	
	/*private void moveFocusWithKey(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_UP)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_END)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_PAGE_UP)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_PAGE_DOWN)
		{
			moveFocus();
		}
		else if(e.getKeyCode()==KeyEvent.VK_HOME)
		{
			moveFocus();
		}
	}*/

}
