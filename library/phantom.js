var page = new WebPage();

page.onConsoleMessage = function(msg) {
    console.log(msg);
};

page.open(encodeURI(phantom.args[0]), function (status) {
    if (status === "success") {
        page.evaluate(function() {
			
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
					else {
						if (! on_top(r)) debugger;
						on_top(r);
					}
				}
			}
			
            var list = document.querySelectorAll('a');
			var response = new Array();
            for (var i = 0; i < list.length; ++i) {
				var visible = isVisible(list[i]);
				var url = list[i].href;
				response.push({
					url: url,
					visible: !!visible
				});
            }
			
			console.log(JSON.stringify(response));
			
        });
    }
    phantom.exit();
});
