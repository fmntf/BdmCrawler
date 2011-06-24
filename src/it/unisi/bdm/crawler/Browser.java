/**
 * This file is part of BdmCrawler.
 * BdmCrawler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BdmCrawler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BdmCrawler.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.unisi.bdm.crawler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import com.google.gson.Gson;


public class Browser
{
	private long timeout;
	
	public Browser(long timeout)
	{
		this.timeout = timeout;
	}
	
	public Link[] getLinks(String url)
		throws BrowserTimeoutException
	{
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(stdout);
			
		String line = "library/phantomjs library/phantom.js " + url;
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(this.timeout);
		executor.setWatchdog(watchdog);
		executor.setStreamHandler(psh);
		try {
			executor.execute(cmdLine);
		}
		catch (Exception e) {
			throw new BrowserTimeoutException();
		}
		
		String json = stdout.toString();
		return new Gson().fromJson(json, Link[].class);
	}
	
	public static OutputStream executeCommandLine(final String commandLine, final long timeout)
		throws IOException, InterruptedException, BrowserTimeoutException
	{
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(commandLine);
		/* Set up process I/O. */
		
		Worker worker = new Worker(process);
		worker.start();
		try {
			worker.join(timeout);
			if (worker.exit != null)
				return process.getOutputStream();
			else
				throw new BrowserTimeoutException();
		} catch (InterruptedException ex) {
			worker.interrupt();
			Thread.currentThread().interrupt();
			throw ex;
		} finally {
			process.destroy();
		}
	}

	private static class Worker extends Thread {
		private final Process process;
		private Integer exit;
		private Worker(Process process) {
			this.process = process;
		}
		
		@Override
		public void run() {
			try { 
				exit = process.waitFor();
			} catch (InterruptedException ignore) {
				return;
			}
		}  
	}
}
