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

public class WebRetrieverTest {
	
	@Test
	public void testGetsHtmlDocumentAsString() throws Exception
	{
		WebRetriever wr = new WebRetriever();
		String html = wr.fromUrl("http://blog.webmatters.it");
		
		assertTrue(html.trim().startsWith("<!DOCTYPE"));
	}
	
	@Test(expected=InvalidUrlException.class)
	public void testThrowsExceptionOnUrlWithBadSchema() throws Exception
	{
		WebRetriever wr = new WebRetriever();
		wr.fromUrl("bad://blog.webmatters.it");
	}
	
	@Test(expected=InvalidUrlException.class)
	public void testThrowsExceptionOnUrlThatPointsToAnImage() throws Exception
	{
		WebRetriever wr = new WebRetriever();
		wr.fromUrl("http://blog.webmatters.it/wp-content/themes/twentyten/images/tramonto.jpg");
	}
}
