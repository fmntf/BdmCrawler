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

import java.io.ByteArrayOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import com.google.gson.Gson;

public class Browser implements BrowserInterface
{
	private long timeout;
	
	public Browser()
	{
		this.timeout = 9999;
	}
	
	public Browser(long timeout)
	{
		this.timeout = timeout;
	}
	
	@Override
	/**
	 * Invokes PhantomJS in order to download the page associated to `url`.
	 * Please note that since the browser sometimes hangs, it's execution is
	 * monitored by a wathdog that kills it after this.timeout ms.
	 * 
	 * @param url
	 * @return Page
	 * @throws BrowserTimeoutException If PhantomJS hangs.
	 */
	public Page getPage(String url) throws BrowserTimeoutException
	{
		ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(stdout);
			
		String line = "library/phantomjs library/crawler.js " + url;
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
		return new Gson().fromJson(json, Page.class);
	}
}
