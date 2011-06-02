package it.unisi.bdm.crawler;

import org.w3c.tidy.Tidy;
import org.w3c.tidy.*;
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
import javax.xml.transform.OutputKeys;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 *
 * @author francesco
 */
public class HtmlAnalyzer
{
	private Document page;
	
	public HtmlAnalyzer(String html)
	{
		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setShowWarnings(false);
		tidy.setTidyMark(false);
		
		Reader input = new StringReader(html);
		this.page = tidy.parseDOM(input, null);
	}
	
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
