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
import java.net.URLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.MalformedURLException;

/**
 * WebRetriever
 * 
 * Downloads an HTML page from the Network.
 * Throws exceptions if the document is not HTML.
 */
public class WebRetriever
{
	/**
	 * @param String url
	 * @return String HTML content
	 */
	public String fromUrl(String url)
		throws java.io.IOException, InvalidUrlException
	{
		URL u;
		try {
			u = new URL(url);
		}
		catch (java.net.MalformedURLException e) {
			throw new InvalidUrlException("Invalid schema");
		}
		
		URLConnection connection = u.openConnection();
		String contentType = connection.getContentType().split(";")[0]; // removes encoding
		
		if (!contentType.equals("text/html")) {
			throw new InvalidUrlException("Unexpected content type: " + contentType);
		}
		
		InputStream stream = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		String line;
		StringBuilder result = new StringBuilder();
		
		while ((line = reader.readLine()) != null) {
			result.append(line);			
		}

		return result.toString();
	}
}
