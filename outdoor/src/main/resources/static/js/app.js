function loadPage(uri) {
    $.get(uri)
        .then(function (data) {
            $('#container').html(data);
        });
}

$(function () {
    var uri = window.location.pathname;
    if (uri.length < 2) {
        uri = "/view/outdoor.html"
    }
    loadPage(uri);
});

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$.fn.fillForm = function (obj) {
    var fform = function (props, prevKey) {
        for (var key in props) {
            var value = props[key];
            if(typeof value === 'object') {
                fform(value, key+".")
            } else {
                this.find("input[name='" + prevKey+key + "']").val(value)
            }
        }
    }.bind(this);
    fform(obj, "");
};

$.delete = function (url, data) {
    return jQuery.ajax({
        url: url,
        type: 'delete',
        data: data || {}
    });
};
$.put = function (url, data) {
    return jQuery.ajax({
        url: url,
        type: 'put',
        data: data || {}
    });
};

Array.prototype.find = Array.prototype.find || function(callback) {
        if (this === null) {
            throw new TypeError('Array.prototype.find called on null or undefined');
        } else if (typeof callback !== 'function') {
            throw new TypeError('callback must be a function');
        }
        var list = Object(this);
        // Makes sures is always has an positive integer as length.
        var length = list.length >>> 0;
        var thisArg = arguments[1];
        for (var i = 0; i < length; i++) {
            var element = list[i];
            if ( callback.call(thisArg, element, i, list) ) {
                return element;
            }
        }
    };