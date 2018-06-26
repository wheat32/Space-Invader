package system;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger implements Thread.UncaughtExceptionHandler
{
	private static File outputFile = null;
	private static FileWriter fw = null;
	private static BufferedWriter bw = null;
	
	private static boolean printToConsole = true;
	private static boolean printToFile = false;
	
	public static void setup()
	{
		Runtime.getRuntime().addShutdownHook(shutdownThread);
		
		File dir = new File("output");
		
		if(dir.exists() == false || dir.isDirectory() == false)
		{
			dir.mkdir();
		}
		
		//outputFile = new File("output/output- " + System.currentTimeMillis() + ".txt");
		
		//try
		{
			//fw = new FileWriter(outputFile.getPath());
		}
		//catch (IOException e)
		{
			//e.printStackTrace();
		}
		
		//bw = new BufferedWriter(fw);
	}
	
	public static void stdout(Object src, String msg)
	{
		String premsg = src.toString().substring(6) + " | " + new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + " | ";
		
		if(printToConsole == true)
		{
			System.out.println(premsg + msg);
		}
		
		if(printToFile == true)
		{
			try
			{
				if(outputFile.exists() == false)
				{
					if(outputFile.createNewFile() == false)
					{
						System.err.println("Debugger | " + new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + " | Failed to create new file.");
					}
				}
				bw.write(premsg + msg);
				bw.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void stderr(Object src, String msg)
	{
		String premsg = "ERROR: " + src.toString().substring(6) + " | " + new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + " | ";
		
		if(printToConsole == true)
		{
			System.err.println(premsg + msg);
		}
		
		if(printToFile == true)
		{
			try
			{
				if(outputFile.exists() == false)
				{
					if(outputFile.createNewFile() == false)
					{
						System.err.println("Debugger | " + new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + " | Failed to create new file.");
					}
				}
				bw.write(premsg + msg);
				bw.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		stderr(e.getClass(), "An uncaught exception has occured.");
		
		if (e.getMessage() != null) 
		{
			stderr(e.getClass(), e.getMessage());
		}
		
		if (e.getCause() != null)
		{
			stderr(e.getClass(), "Caused by: \n" + e.getCause().toString());
		}
		
		StackTraceElement[] trace = e.getStackTrace();
		StringBuilder strTrace = new StringBuilder("");
		
		for(int i = 0; i < trace.length ;i++) 
		{
			strTrace.append(trace[i].toString());
		}
		
		stderr(e.getClass(), strTrace.toString());
	}
	
	private static Thread shutdownThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			if(printToFile == false)
			{
				return;
			}
			
			try
			{
				bw.close();
				fw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			outputFile.renameTo(new File("output/output-" + new SimpleDateFormat("MM-dd-yy_HH-mm-ss").format(Calendar.getInstance().getTime()) + ".txt"));
		}
	});
}
