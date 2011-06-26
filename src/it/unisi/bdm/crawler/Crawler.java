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
	private PriorityQueue<Link> queue;
	private HashMap<Link, String> downloaded;
	private ArrayList<Link> banned;
	private BrowserInterface browser;
	private int maxDownloadedPages = Integer.MAX_VALUE;
	private boolean verbose = false;
	private UrlInspector urlInspector;
	private HtmlValidator validator;
	
	public Crawler()
	{
		Comparator<Link> comparator = new LinkComparator();
        this.queue = new PriorityQueue<Link>(30, comparator);
		this.banned = new ArrayList<Link>();
		this.downloaded = new HashMap<Link, String>();
		
		this.urlInspector = new UrlInspector();
		this.validator = new HtmlValidator();
	}
	
	public void setBrowser(BrowserInterface browser)
	{
		this.browser = browser;
	}
	
	public void setMaxDownloadedPages(int n)
	{
		this.maxDownloadedPages = n;
	}
	
	public void setVerbose(boolean verbose)
	{
		this.verbose = verbose;
	}
	
	public void unleash(String startUrl)
	{
		this.queue.add(
			new Link(startUrl)
		);
		
		Link linkToProcess;
		Page downloadedPage;
		int downloadedCount = 0;
		
		while (!this.queue.isEmpty() && downloadedCount<this.maxDownloadedPages) {
			try {
				linkToProcess = this.queue.poll();
				downloadedPage = this.processLink(linkToProcess, 3);
				this.say("[Downloaded page] " + linkToProcess);
				
				// store HTML
				this.downloaded.put(linkToProcess, downloadedPage.toString());
				
				// add links to queue if
				//  - not downloaded yet
				//  - not in download queue
				//  - not banned
				for (Link link:downloadedPage.getLinks()) {
					if (
						!this.banned.contains(link) &&
						!this.downloaded.containsKey(link) &&
						!this.queue.contains(link)
					) {
						if (this.urlInspector.isLegal(link.toString())) {
							link.setContextMess(downloadedPage.getContextMess(this.validator));
							this.queue.add(link);
							this.say("   [Adding "+link.how()+" @" +link.getScore().toString()+ "] " + link);
						} else {
							this.say("   [NOT adding link] " + link);
						}
					}
				}
				
				downloadedCount++;
			}
			catch (UnreachableUrlException e) {
				this.banned.add(e.getLink());
			}
		}
		
		this.say("\n\nNothing else to crawl!");
	}
	
	public HashMap<Link, String> getDownloadedPages()
	{
		return this.downloaded;
	}
	
	public ArrayList<Link> getBannedPages()
	{
		return this.banned;
	}
	
	private void say(String message)
	{
		if (verbose) {
			System.out.println(message);
		}
	}
	
	private Page processLink(Link link, int tryAgain) throws UnreachableUrlException
	{
		try {
			return browser.getPage(link.toString());
		}
		catch (Exception e) {
			if (tryAgain>0) {
				this.say("[WARNING] Browser hangs, process killed. Times before banning page: " + (tryAgain-1));
				return this.processLink(link, tryAgain-1);
			} else {
				throw new UnreachableUrlException(link);
			}
		}
	}
}
