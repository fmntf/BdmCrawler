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

public class ExtensionUrlInspectorTest
{
	@Test
	public void allowsUrlThatHopefullyWillContainHtml()
	{
		ExtensionUrlInspector inspector = new ExtensionUrlInspector();
		assertTrue(inspector.isLegal("http://www.google.it"));
		assertTrue(inspector.isLegal("http://www.google.it/"));
		assertTrue(inspector.isLegal("http://www.google.it/index.html"));
		assertTrue(inspector.isLegal("http://www.google.it/index.htm"));
		assertTrue(inspector.isLegal("http://www.google.it/index.php"));
		assertTrue(inspector.isLegal("http://www.google.it/index.asp"));
		assertTrue(inspector.isLegal("http://www.google.it/index.aspx"));
		assertTrue(inspector.isLegal("http://www.google.it/index.jsp"));
	}
	
	@Test
	public void blocksUrlsWithNonHtmlContent()
	{
		ExtensionUrlInspector inspector = new ExtensionUrlInspector();
		assertFalse(inspector.isLegal("http://www.google.it/logo.png"));
		assertFalse(inspector.isLegal("http://www.google.it/logo.jpg"));
		assertFalse(inspector.isLegal("http://www.google.it/file.zip"));
		assertFalse(inspector.isLegal("http://www.google.it/file.tar"));
		assertFalse(inspector.isLegal("smb://file"));
	}
}