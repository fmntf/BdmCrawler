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
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.PrintWriter;

public class HtmlValidator implements HtmlValidatorInterface
{
	@Override
	/**
	 * Gets a number which is proportional to the mess in the HTML code.
	 * 
	 * @param html
	 * @return int
	 */
	public int validate(String html)
	{
		Tidy tidy = new Tidy();

		// suppress ugly output
		PrintWriter errorWriter = new PrintWriter(new StringWriter(), true);
		tidy.setErrout(errorWriter);
		
		tidy.parse(this.stringToStream(html), new StringWriter());
		
		return 
				tidy.getParseWarnings() +
			2 * tidy.getParseErrors();
	}
	
	private InputStreamReader stringToStream(String string)
	{
		byte[] bytes = string.getBytes();
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		
		return new InputStreamReader(bais);
	}
}
