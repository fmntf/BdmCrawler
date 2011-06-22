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

import java.net.URL;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Browser
{
	private HtmlPage page;
	
	public Browser(String url) throws java.io.IOException, InvalidUrlException
	{
		// disable logging
		System.getProperties().put("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		
		WebClient webClient = new WebClient();
		Page page;
		URL u;
		
		try {
			u = new URL(url);
		}
		catch (java.net.MalformedURLException e) {
			throw new InvalidUrlException(e.getMessage());
		}
		
		try {
			page = webClient.getPage(u);
		}
		catch (java.lang.IllegalStateException e) {
			throw new InvalidUrlException(e.getMessage());
		}
		if (page instanceof HtmlPage) {
			this.page = (HtmlPage)page;
		} else {
			throw new InvalidUrlException("The URL " + url + " points to a non-HTML resource");
		}
	}
	
	public String getPageTitle()
	{
		return this.page.getTitleText();
	}
	
	public void test()
	{
		for (DomNode anchor:this.page.querySelectorAll("a:visible")) {
			anchor = (HtmlAnchor)anchor;
			System.out.println(anchor.getTextContent());
		}
		System.out.println("......................");
		for (DomNode anchor:this.page.querySelectorAll("a")) {
			anchor = (HtmlAnchor)anchor;
			System.out.println(anchor.getTextContent());
		}
	}
}
