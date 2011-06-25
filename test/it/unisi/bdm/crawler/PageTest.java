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

public class PageTest
{
	@Test
	public void wontPenalizeCorrectMarkup()
	{
		Page page = new Page("<!doctype html>", new Link[0]);
		HtmlValidatorMock validator = new HtmlValidatorMock(0);
		
		assertEquals(0, page.getContextMess(validator), 0.01);
	}
	
	@Test
	public void penalizesIncorrectMarkup()
	{
		Page page = new Page("<!doctype html>", new Link[0]);
		HtmlValidatorMock validator = new HtmlValidatorMock(10);
		
		assertEquals(0.10, page.getContextMess(validator), 0.01);
	}
	
	@Test
	public void penalizesIncorrectMarkupUpToHalf()
	{
		Page page = new Page("<!doctype html>", new Link[0]);
		HtmlValidatorMock validator = new HtmlValidatorMock(100);
		
		assertEquals(0.5, page.getContextMess(validator), 0.01);
	}
}
