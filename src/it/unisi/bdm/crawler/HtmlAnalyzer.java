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

import org.w3c.tidy.Tidy;
import org.w3c.tidy.DOMElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 * HtmlAnalyzer
 * 
 * Tidies the given HTML markup and extracts the link tags (<a href=""></a>)
 */
public class HtmlAnalyzer
{
	private Document page;
	
	/**
	 * @param String html 
	 */
	public HtmlAnalyzer(String html)
	{
		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setTidyMark(false);
		
		Reader input = new StringReader(html);
		this.page = tidy.parseDOM(input, null);
	}
	
	/**
	 * Extracts a list of links found in the <body> of the document.
	 * 
	 * @return ArrayList<HtmlLink>
	 */
	public ArrayList<HtmlLink> getLinks()
	{
		ArrayList list = new ArrayList<HtmlLink>();
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		String pattern = "//body/a";
		try {
			NodeList nodes = (NodeList) xpath.evaluate(pattern, this.page, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
					DOMElementImpl node = (DOMElementImpl) nodes.item(i);
					list.add(new HtmlLink(node.getAttribute("href")));
			}
		}
		catch (XPathExpressionException e) {
			System.err.println("Invalid XPath pattern: " + pattern);
		}
		
		return list;
	}
	
	public String toString()
	{
		try{
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource src = new DOMSource(this.page);
			StringWriter stringWriter = new StringWriter();
			Result res = new StreamResult(stringWriter);
			t.transform(src, res);
			
			return stringWriter.getBuffer().toString();
		} catch (Exception e) {
			return "Exception: " + e.getMessage();
		}
	}
}
