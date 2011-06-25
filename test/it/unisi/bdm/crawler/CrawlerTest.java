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

public class CrawlerTest
{
	@Test
	public void followsReachableUrls()
	{
		BrowserMock browser = new BrowserMock();
		this.injectAciclycGraph(browser);
		
		Crawler crawler = new Crawler("http://www.iveco.com");
		crawler.setBrowser(browser);
		
		crawler.unleash();
		
		assertTrue(crawler.getDownloadedPages().size() == 2);
		assertTrue(crawler.getBannedPages().isEmpty());
	}
	
	@Test
	public void bansUnreachableUrls()
	{
		BrowserMock browser = new BrowserMock();
		this.injectAciclycUnreachableGraph(browser);
		
		Crawler crawler = new Crawler("http://www.iveco.com");
		crawler.setBrowser(browser);
		
		crawler.unleash();
		
		assertTrue(crawler.getDownloadedPages().size() == 1);
		assertTrue(crawler.getDownloadedPages().containsKey(new Link("http://www.iveco.com")));
		
		assertTrue(crawler.getBannedPages().size() == 1);
		assertTrue(crawler.getBannedPages().contains(new Link("http://www.daily.iveco.com")));
	}
	
	@Test
	public void wontGetLostOnACycle()
	{
		BrowserMock browser = new BrowserMock();
		this.injectCiclycGraph(browser);
		
		Crawler crawler = new Crawler("http://www.iveco.com");
		crawler.setBrowser(browser);
		
		crawler.unleash();
		
		assertTrue(crawler.getDownloadedPages().size() == 2);
		assertTrue(crawler.getBannedPages().isEmpty());
	}
	
	@Test
	public void willHaltOverNInterations()
	{
		BrowserMock browser = new BrowserMock();
		this.injectLongGraph(browser);
		
		Crawler crawler = new Crawler("http://www.iveco.com");
		crawler.setMaxDownloadedPages(3);
		crawler.setBrowser(browser);
		
		crawler.unleash();
		
		assertTrue(crawler.getDownloadedPages().size() == 3);
		assertTrue(crawler.getBannedPages().isEmpty());
	}
	
	@Test
	public void ignoresNonHttpLinks()
	{
		BrowserMock browser = new BrowserMock();
		this.injectMailtoGraph(browser);
		
		Crawler crawler = new Crawler("http://www.iveco.com");
		crawler.setBrowser(browser);
		
		crawler.unleash();
		
		assertTrue(crawler.getDownloadedPages().size() == 1);
	}
	
	
	/**
	 * Creates an aciclyc graph:
	 *    index ==> internal
	 * 
	 * @param BrowserMock browser
	 */
	private void injectAciclycGraph(BrowserMock browser)
	{
		Link[] toDaily = new Link[1];
		toDaily[0] = new Link("http://www.daily.iveco.com");
		
		Page index = new Page("<!doctype html><html>...</html>", toDaily);
		browser.setPage("http://www.iveco.com", index);
		
		Page daily = new Page("<!doctype html><html>...</html>", new Link[0]);
		browser.setPage("http://www.daily.iveco.com", daily);
	}
	
	/**
	 * Creates an unreachable aciclyc graph:
	 *    index -- | --> internal
	 * 
	 * @param BrowserMock browser
	 */
	private void injectAciclycUnreachableGraph(BrowserMock browser)
	{
		Link[] toDaily = new Link[1];
		toDaily[0] = new Link("http://www.daily.iveco.com");
		
		Page index = new Page("<!doctype html><html>...</html>", toDaily);
		browser.setPage("http://www.iveco.com", index);
	}
	
	/**
	 * Creates a ciclyc graph:
	 *    index <==> internal
	 * 
	 * @param BrowserMock browser
	 */
	private void injectCiclycGraph(BrowserMock browser)
	{
		Link[] toDaily = new Link[1];
		toDaily[0] = new Link("http://www.daily.iveco.com");
		
		Link[] toIndex = new Link[1];
		toIndex[0] = new Link("http://www.iveco.com");
		
		Page index = new Page("<!doctype html><html>...</html>", toDaily);
		browser.setPage("http://www.iveco.com", index);
		
		Page daily = new Page("<!doctype html><html>...</html>", toIndex);
		browser.setPage("http://www.daily.iveco.com", daily);
	}
	
	/**
	 * Creates an aciclyc graph:
	 *    a ==> b ==> c ==> d
	 * 
	 * @param BrowserMock browser
	 */
	private void injectLongGraph(BrowserMock browser)
	{
		Link[] toB = new Link[1];
		toB[0] = new Link("http://www.daily.iveco.com");
		
		Link[] toC = new Link[1];
		toC[0] = new Link("http://www.dailyradio.iveco.com");
		
		Link[] toD = new Link[1];
		toD[0] = new Link("http://www.dailyminivan.iveco.com");
		
		Page a = new Page("<!doctype html>", toB);
		browser.setPage("http://www.iveco.com", a);
		
		Page b = new Page("<!doctype html>", toC);
		browser.setPage("http://www.daily.iveco.com", b);
		
		Page c = new Page("<!doctype html>", toD);
		browser.setPage("http://www.dailyradio.iveco.com", c);
		
		Page d = new Page("<!doctype html>", new Link[0]);
		browser.setPage("http://www.dailyminivan.iveco.com", d);
	}
	
	private void injectMailtoGraph(BrowserMock browser)
	{
		Link[] toMail = new Link[1];
		toMail[0] = new Link("mailto:info@iveco.com");
		
		Page index = new Page("<!doctype html><html>...</html>", toMail);
		browser.setPage("http://www.iveco.com", index);
	}
}
