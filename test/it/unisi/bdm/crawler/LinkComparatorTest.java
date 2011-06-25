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

import java.util.PriorityQueue;
import java.util.Comparator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LinkComparatorTest
{
	@Test
	public void givesPriorityToBoldLinks()
	{
		Comparator<Link> comparator = new LinkComparator();
        PriorityQueue<Link> queue = new PriorityQueue<Link>(5, comparator);
		
		Link std = new Link("http://std", true, false);
		Link bold = new Link("http://bold", true, true);
		
		queue.add(std);
		queue.add(bold);
		
		assertSame(bold, queue.peek());
	}
	
	@Test
	public void givesPriorityToBoldLinksWithoutQueryString()
	{
		Comparator<Link> comparator = new LinkComparator();
        PriorityQueue<Link> queue = new PriorityQueue<Link>(5, comparator);
		
		Link std = new Link("http://std", true, false);
		Link boldQs = new Link("http://bold?qs=1", true, true);
		Link bold = new Link("http://bold", true, true);
		
		queue.add(std);
		queue.add(boldQs);
		queue.add(bold);
		
		assertSame(bold, queue.poll());
		assertSame(boldQs, queue.poll());
		assertSame(std, queue.poll());
	}
}
