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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HtmlTidyValidatorTest
{
	@Test
	public void wontReportErrorsOnValidPage()
	{
		HtmlTidyValidator validator = new HtmlTidyValidator();
		
		int mess = validator.validate(this.readFile("test/fixtures/html/w3.org.html"));
		assertEquals(0, mess);
	}
	
	@Test
	public void repostsErrorsOnInvalidPage()
	{
		HtmlTidyValidator validator = new HtmlTidyValidator();
		
		int mess = validator.validate(this.readFile("test/fixtures/html/iveco.com.html"));
		assertEquals(12, mess);
	}
	
	private String readFile( String file )
	{
		StringBuilder stringBuilder = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader( new FileReader (file));
			String line  = null;
			while( ( line = reader.readLine() ) != null ) {
				stringBuilder.append(line).append("\n");
			}
		}
		catch (IOException e) {
			fail(e.getMessage());
		}
		
		return stringBuilder.toString();
	}
}
