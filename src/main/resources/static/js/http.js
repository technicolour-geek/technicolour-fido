var hsecure = hsecure || {};
hsecure.Namespace = function (namespace) {
    var sections = namespace.split('.');
    var parent = hsecure;
    var i;
    
    if(sections[0] === 'hsecure' ){
       sections = sections.slice(1);
    }
    
    for (i = 0; i < sections.length; i += 1) {
        if (typeof parent[sections[i]] === "undefined") {
            parent[sections[i]] = {};
        }
        
        parent = parent[sections[i]];
    }

    return parent;
};

hsecure.Namespace("hsecure.xpass.api.http");

// OpenAPI Call
hsecure.xpass.api.http = (function(){
	var xhr;
	return {
		doAction : function (url, params, sucessCallBack, errorCallBack) {
			$.support.cors = true;	// IE 8
			xhr = $.ajax({
                'method': 'POST',
			    'url': url,
			    'dataType' : 'json',
			    'jsonpCallback' : "myCallback",
			    'contentType': 'application/json; charset=utf-8',
			    'data': params,
				'xhrFields': {
					withCredentials: true
				},
			    'success': sucessCallBack,
			    'error' : errorCallBack
			});
		},
		
		doAbort : function () {
			xhr.abort();
		}
	}
})();