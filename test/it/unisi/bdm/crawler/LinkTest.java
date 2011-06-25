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

public class LinkTest
{
	@Test
	public void penalizesHiddenLinks()
	{
		Link link = new Link("http://url", false, true);
		
		assertEquals(0.1, link.getScore(), 0.01);
	}
	
	@Test
	public void givesImportanceToBold()
	{
		Link bold = new Link("http://bold", true, true);
		Link std = new Link("http://std", true, false);
		
		assertEquals(1.0, bold.getScore(), 0.01);
		assertEquals(0.8, std.getScore(), 0.01);
	}
	
	@Test
	public void penalizesHugeQueryString()
	{
		Link std = new Link("http://www.google.it", true, true);
		Link qs = new Link("http://www.google.it/search?sourceid=chrome&client=ubuntu&channel=cs&ie=UTF-8&q=bdm", true, true);
		
		assertEquals(1.0, std.getScore(), 0.01);
		assertEquals(0.77, qs.getScore(), 0.01);
	}
}
