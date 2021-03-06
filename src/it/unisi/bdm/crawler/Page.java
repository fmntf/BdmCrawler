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

public class Page
{
	private String html;
	private Link[] links;
	
	public Page(String html, Link[] links)
	{
		this.html = html;
		this.links = links;
	}
	
	public Link[] getLinks()
	{
		return this.links;
	}
	
	/**
	 * Validates the HTML and gives a score.
	 * 0 means "no errors in HTML", 0.5 means "please, uninstall MS FrontPage"
	 * 
	 * @param validator
	 * @return float
	 */
	public float getContextMess(HtmlValidatorInterface validator)
	{
		float errors = (float)validator.validate(this.html);
		
		if (errors/100 > 0.5f) {
			return 0.5f;
		}
		
		return errors/100;
	}
	
	@Override
	public String toString()
	{
		return this.html;
	}
}
