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

import org.junit.Test;
import static org.junit.Assert.*;

public class PhantomjsBrowserTest
{
	@Test
	public void getsHtmlPageWithLinks() throws BrowserTimeoutException
	{
		PhantomjsBrowser browser = new PhantomjsBrowser();
		Page page = browser.getPage("http://blog.webmatters.it");
		
		assertTrue(page.toString().length() > 0);
		assertTrue(page.getLinks().length > 0);
		assertTrue(page.getLinks()[0].toString().startsWith("http://"));
	}
	
	@Test(expected=BrowserTimeoutException.class)
	public void throwsExceptionOnTimeout() throws BrowserTimeoutException
	{
		PhantomjsBrowser browser = new PhantomjsBrowser(10); // msec
		browser.getPage("http://blog.webmatters.it");
	}
}
