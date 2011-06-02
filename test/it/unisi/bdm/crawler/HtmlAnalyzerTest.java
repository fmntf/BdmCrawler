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

public class HtmlAnalyzerTest {
	
	@Test
	public void testTidiesTheHtmlString()
	{
		HtmlAnalyzer ha = new HtmlAnalyzer("Page content");
		
		String expected = "<html>\n" + 
			"<head>\n" + 
			"<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" + 
			"<title></title>\n" + 
			"</head>\n" + 
			"<body>Page content</body>\n" + 
			"</html>";
		
		assertEquals(expected, ha.toString().trim());
	}
	
	@Test
	public void testExtractsLinksFromHtml()
	{
		HtmlAnalyzer ha = new HtmlAnalyzer("<a href=\"http://www.google.com\">link</a>");
		
		assertEquals(1, ha.getLinks().size());
		assertEquals("http://www.google.com", ha.getLinks().get(0).getUrl());
	}
	
	@Test
	public void testReturnsNoLinksOnADocumentWithoutAHrefs()
	{
		HtmlAnalyzer ha = new HtmlAnalyzer("<div>and nothing else</div>");
		
		assertEquals(0, ha.getLinks().size());
	}
	
}
