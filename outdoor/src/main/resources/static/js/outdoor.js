var Message = function (time, dynamic, fn) {
    return {
        fn: fn,
        time: time,
        dynamic: dynamic
    };
};


var createDate = function() {
    var today = new Date();
    var day = checkTime(today.getDay());
    var month = checkTime(today.getMonth());
    var year = today.getFullYear();
    return day + "/" + month + "/" + year;
};

var createTime = function() {
    var today = new Date();
    var h = today.getHours();
    var m = checkTime(today.getMinutes());
    var s = checkTime(today.getSeconds());
    return h + ":" + m + ":" + s;
};

var checkTime = function (i) {
    if (i < 10) {
        i = "0" + i
    }
    return i;
};

var MessageRotator = function (items) {
    this.items = items;
    this.idx = 0;
    this.time = items[0].time + 1;
    this.rendered = false;
    this.tick();
    this.paused = false;
};

MessageRotator.prototype = {
    tick: function () {
        if(this.paused) {
            return;
        }
        this.time--;
        if (!this.time) {
            this.idx++;
            if (this.idx >= this.items.length) {
                this.idx = 0;
            }
            this.time = this.items[this.idx].time;
            this.rendered = false;
        }
        if (!this.rendered || this.items[this.idx].dynamic) {
            this.items[this.idx].fn(this.time);
            this.rendered = true;
        }
    },
    togglePause: function () {
        this.paused = !this.paused;
    }
};

var ajustHeight = function($p, $m) {
	var h = $(window).height();
	$p.height(h-150);
	$m.height(100);
};

