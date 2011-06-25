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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Crawler
{
	PriorityQueue<Link> queue;
	HashMap<Link, String> downloaded;
	ArrayList<Link> banned;
	Browser browser;
	
	public Crawler(String startUrl)
	{
		Comparator<Link> comparator = new LinkComparator();
        this.queue = new PriorityQueue<Link>(30, comparator);
		this.queue.add(
			new Link(startUrl, true)
		);
		
		this.banned = new ArrayList<Link>();
		this.downloaded = new HashMap<Link, String>();
	}
	
	public void setBrowser(Browser browser)
	{
		this.browser = browser;
	}
	
	public void unleash()
	{
		Link linkToProcess;
		Page downloadedPage;
		
		while (!this.queue.isEmpty()) {
			try {
				linkToProcess = this.queue.poll();
				downloadedPage = this.processLink(linkToProcess, 3);
				
				// store HTML
				this.downloaded.put(linkToProcess, downloadedPage.toString());
				
				// add links to queue if not downloaded yet AND not banned
				for (Link link:downloadedPage.getLinks()) {
					if (!this.banned.contains(link) && !this.downloaded.containsKey(link)) {
						this.queue.add(link);
					}
				}
			}
			catch (UnreachableUrlException e) {
				this.banned.add(e.getLink());
			}
		}
	}
	
	private Page processLink(Link link, int tryAgain) throws UnreachableUrlException
	{
		try {
			return browser.getPage(link.toString());
		}
		catch (Exception e) {
			if (tryAgain>0) {
				return this.processLink(link, tryAgain-1);
			} else {
				throw new UnreachableUrlException(link);
			}
		}
	}
}