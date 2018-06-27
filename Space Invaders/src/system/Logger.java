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
	private static boolean printToFile = true;
	private static boolean onlyPrintErrsToFile = true;
	
	public Logger()
	{
		Thread.setDefaultUncaughtExceptionHandler(this);
		setup();
	}
	
	public static void setup()
	{
		Runtime.getRuntime().addShutdownHook(shutdownThread);
	}
	
	private static void createOutFile()
	{
		File dir = new File("output");
		
		if(dir.exists() == false || dir.isDirectory() == false)
		{
			dir.mkdir();
		}
		
		outputFile = new File("output/output-" + System.currentTimeMillis() + ".txt");
		
		try
		{
			outputFile.createNewFile();
			fw = new FileWriter(outputFile.getPath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		bw = new BufferedWriter(fw);
	}
	
	public static void stdout(Class<?> src, String msg)
	{
		String premsg = new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + " | " + src.toString().substring(6) + " | ";
		
		if(printToConsole == true)
		{
			System.out.println(premsg + msg);
		}
		
		if(printToFile == true && onlyPrintErrsToFile == false)
		{
			try
			{
				if(outputFile == null || outputFile.exists() == false)
				{
					createOutFile();
				}
				bw.write(premsg + msg + "\n");
				bw.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void stderr(Class<?> src, String msg)
	{
		String premsg = "ERROR: " + new SimpleDateFormat("HH:mm:ss.SS").format(Calendar.getInstance().getTime()) + " | " + src.toString().substring(6) + " | ";
		
		if(printToConsole == true)
		{
			System.err.println(premsg + msg);
		}
		
		if(printToFile == true)
		{
			try
			{
				if(outputFile == null || outputFile.exists() == false)
				{
					createOutFile();
				}
				bw.write(premsg + msg + "\n");
				bw.flush();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void exception(Throwable e)
	{
		StringBuilder strTrace = new StringBuilder("");
		
		strTrace.append("A caught exception has occured.\n\t");
		
		if (e.getMessage() != null) 
		{
			strTrace.append(e.getMessage() + "\n\t");
		}
		
		if (e.getCause() != null)
		{
			strTrace.append("Caused by: \n" + e.getCause().toString() + "\n\t");
		}
		
		StackTraceElement[] trace = e.getStackTrace();
		
		for(int i = 0; i < trace.length ;i++) 
		{
			strTrace.append(trace[i].toString() + "\n\t");
		}
		
		stderr(e.getClass(), strTrace.toString());
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e)
	{
		StringBuilder strTrace = new StringBuilder("");
		
		strTrace.append("An uncaught exception has occured.\n\t");
		
		if (e.getMessage() != null) 
		{
			strTrace.append(e.getMessage() + "\n\t");
		}
		
		if (e.getCause() != null)
		{
			strTrace.append("Caused by: \n" + e.getCause().toString() + "\n\t");
		}
		
		StackTraceElement[] trace = e.getStackTrace();
		
		for(int i = 0; i < trace.length ;i++) 
		{
			strTrace.append(trace[i].toString() + "\n\t");
		}
		
		stderr(e.getClass(), strTrace.toString());
	}
	
	private static Thread shutdownThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			if(printToFile == false || outputFile == null || outputFile.exists() == false)
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
