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

var page = new WebPage();

// penalize links over 5 scrolls of 1024 pixels.
page.viewportSize = { width: 1024, height: 1024*5 }

// a little bit of ego
var baseAgent = page.settings.userAgent.split('PhantomJS')[0];
page.settings.userAgent = baseAgent + "BdmCrawler/1.0";


page.open(encodeURI(phantom.args[0]), function (status) {
	if (status === "success") {
		var links = page.evaluate(function() {
			
			function isVisible(element) {
				if (element.offsetWidth === 0 || element.offsetHeight === 0) return false;
				var height = document.documentElement.clientHeight,
				rects = element.getClientRects(),
				on_top = function(r) {
					for (var x = Math.floor(r.left), x_max = Math.ceil(r.right); x <= x_max; x++)
					for (var y = Math.floor(r.top), y_max = Math.ceil(r.bottom); y <= y_max; y++) {
						if (document.elementFromPoint(x, y) === element) return true;
					}
					return false;
				};
				for (var i = 0, l = rects.length; i < l; i++) {
					var r = rects[i],
					in_viewport = r.top > 0 ? r.top <= height : (r.bottom > 0 && r.bottom <= height);
					if (in_viewport && on_top(r)) return true;
				}
				
				return true;
			}
			
			function isBold(element) {
				if (element.style.fontWeight == 'bold') return true;

				if (element.childNodes.length == 1) {
					var child = element.childNodes[0];
					if (child.nodeName == 'B' || child.nodeName == 'STRONG') return true;
				}

				var parent = element.parentNode;
				if (parent.nodeName == 'B' || parent.nodeName == 'STRONG') return true;

				return false;
			}

			
            var list = document.querySelectorAll('a');
			var links = new Array();
            for (var i = 0; i < list.length; ++i) {
				links.push({
					url: list[i].href,
					visible: isVisible(list[i]),
					bold: isBold(list[i])
				});
            }
			
			return links;
        });
		
		var response = {
			html: page.content,
			links: links
		};
		
		console.log(JSON.stringify(response));
    }
    phantom.exit();
});
