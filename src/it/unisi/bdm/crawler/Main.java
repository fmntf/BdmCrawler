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
	/**
	 * Parses command line params and starts the crawler.
	 * 
	 * @param args Command line arguments
	 */
	public static void main(String[] args)
	{
		if (args.length == 0) {
			Main.usage();
		}
		
		Crawler crawler = new Crawler();
		crawler.setBrowser(new PhantomjsBrowser());
		
		for (int i=1; i<args.length; i++) {
			if (args[i].equals("--verbose")) {
				crawler.setVerbose(true);
			} else {
				if (args[i].equals("--accurate")) {
					crawler.setUrlInspector(new NetworkUrlInspector());
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
		System.out.println("   java -jar crawler.jar <StartUrl> [--verbose] [--accurate]");
		System.out.println("\n");
		System.out.println("Options:");
		System.out.println("  --verbose   writes to console when fetching pages and when adding links");
		System.out.println("  --accurate  sends an HEAD request to discover mime-type instead of");
		System.out.println("              detecting it by URL extension");
		System.exit(1);
	}
}
