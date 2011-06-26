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

public class Main
{
	public static void main(String[] args)
	{
		if (args.length == 0) {
			Main.usage();
		}
		
		Crawler crawler = new Crawler();
		crawler.setBrowser(new Browser());
		
		for (int i=1; i<args.length; i++) {
			if (args[i].equals("--quiet")) {
				crawler.setVerbose(false);
			} else {
				if (args[i].equals("--accurate")) {
					crawler.setUrlInspector(new UrlInspector());
				} else {
					System.out.println("Invalid option: " + args[i]);
					Main.usage();
				}
			}
		}

		try {
			crawler.unleash(args[0]);
		}
		catch (IllegalArgumentException e) {
			System.out.println("The provided URL " + args[0] + " appears to be invalid.");
			Main.usage();
		}
	}
	
	private static void usage()
	{
		System.out.println("Usage:");
		System.out.println("   java -jar crawler.jar <StartUrl> [--quiet] [--accurate]");
		System.out.println("\n");
		System.out.println("Options:");
		System.out.println("  --quiet     no output at all");
		System.out.println("  --accurate  sends an HEAD request to discover mime-type");
		System.out.println("              instead of detecting it by URL extension");
		System.exit(1);
	}
}
