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
