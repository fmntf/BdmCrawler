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

public class BrowserTest
{
	@Test
	public void testGetsHtmlDocument() throws Exception
	{
		Browser browser = new Browser("http://blog.webmatters.it");
		assertTrue(browser.getPageTitle().startsWith("Take anything, but my IDE"));
	}
	
	@Test(expected=InvalidUrlException.class)
	public void testThrowsExceptionOnUrlThatPointsToAnImage() throws Exception
	{
		new Browser("http://blog.webmatters.it/wp-content/themes/twentyten/images/tramonto.jpg");
	}
	
	@Test(expected=InvalidUrlException.class)
	public void testThrowsExceptionOnUrlWithFtpSchema() throws Exception
	{
		new Browser("ftp://blog.webmatters.it");
	}
	
	@Test(expected=InvalidUrlException.class)
	public void testThrowsExceptionOnUrlWithMailtoSchema() throws Exception
	{
		new Browser("mailto:francesco.monte@gmail.com");
	}
	
	@Test(expected=InvalidUrlException.class)
	public void testThrowsExceptionOnUrlWithJavascriptSchema() throws Exception
	{
		new Browser("javascript:history.back()");
	}
}