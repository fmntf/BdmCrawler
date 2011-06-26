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

public class Link
{
	private String url;
	private Boolean visible;
	private Boolean bold;
	private float contextMess = 0;
	
	public Link(String url)
	{
		this.url = url;
		this.visible = true;
		this.bold = false;
	}
	
	public Link(String url, Boolean visible, Boolean bold)
	{
		this.url = url;
		this.visible = visible;
		this.bold = bold;
	}
	
	/**
	 * Sets the penality of the page
	 * 
	 * @param contextMess In interval 0-1
	 */
	public void setContextMess(float contextMess)
	{
		this.contextMess = contextMess;
	}
	
	/**
	 * Computes a score for the link based on it's page, bold tag and visiblity.
	 * 1 means very good link, 0 means very bad link.
	 * 
	 * @return Float
	 */
	public Float getScore()
	{
		float score = 1 - this.contextMess;
		
		if (!this.visible) {
			score = score * 0.1f;
		}
		
		if (!this.bold) {
			score = score * 0.8f;
		}
		
		String[] parts = this.url.split("\\?");
		if (parts.length == 2) {
			parts = parts[1].split("&");
			int qsLegth = parts.length;
			
			score = score * (float) Math.pow(0.95f, qsLegth);
		}
		
		
		return score;
	}
	
	/**
	 * Create a string telling how the link is it.
	 * 
	 * @return String
	 */
	public String how()
	{
		String what = "link";
		if (this.bold) {
			if (this.visible) {
				what = "bold+visible link";
			} else {
				what = "bold link";
			}
		} else {
			if (this.visible) {
				what = "visible link";
			}
		}
		
		return what;
	}
	
	@Override
	public String toString()
	{
		return this.url;
	}
	
	@Override
	public boolean equals(Object other)
	{
		return other.toString().equals(this.toString());
	}
	
	@Override
	public int hashCode()
	{
		return this.url.hashCode();
	}
}
