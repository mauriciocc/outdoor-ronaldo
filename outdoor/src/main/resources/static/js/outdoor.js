var Message = function (time, dynamic, fn) {
    return {
        fn: fn,
        time: time,
        dynamic: dynamic
    };
};

Message.isStatic = function(message) {
    return message.content.indexOf('static:') !== -1;
};

Message.isDate = function(message) {
    return message.content === 'date';
};

Message.isTime = function(message) {
    return message.content === 'time';
};

Message.isTemperature = function(message) {
    return message.content === 'lm35';
};

Message.isWeather = function (panel) {
    return panel.message.indexOf('pt:') != -1;
};

Message.extractCity = function(panel) {
    return panel.message.substring(panel.message.indexOf(':')+1)
};

Message.createFromData = function ($msgs, msg, rotatorFn) {
    //Date
    if (Message.isDate(msg)) {
        return Message(msg.timeOnScreen, false, function () {
            $msgs.html(createDate());
        })
    }

    //Time
    if (Message.isTime(msg)) {
        return Message(msg.timeOnScreen, true, function () {
            $msgs.html(createTime());
        });
    }

    //Temperature
    if (Message.isTemperature(msg)) {
        return Message(msg.timeOnScreen, false, function () {
            return $.Deferred(function (def) {
                $msgs.html('Carregando...');
                $.get('/api/manage/temperature')
                    .then(function (data) {
                        $msgs.html(data + "ºC");
                        def.resolve(data);
                    }, def.reject);
            }).promise();
        });
    }

    //Geral
    var content = msg.content;
    var isStatic = Message.isStatic(msg);
    return Message(isStatic ? msg.timeOnScreen : -1, false, function () {
        if (isStatic !== -1) {
            content = content.replace('static:', '');
        }
        $msgs.html('<div class="marquee">' + escapeHtml(content) + '</div>');
        if (!isStatic) {
            $('.marquee')
                .bind('finished', function () {
                    $(this).marquee('destroy');
                    rotatorFn().nextMessage();
                }).marquee({
                startVisible: false,
                delayBeforeStart: 0
            });
        }
    });
};


var weekDayDescription = (function () {
    var weekdays = [
        'Domingo',
        'Segunda-feira',
        'Terça-feira',
        'Quarta-feira',
        'Quinta-feira',
        'Sexta-feira',
        'Sábado'
    ];
    return function (day) {
        return weekdays[day];
    };
})();

var createDate = function () {
    var today = new Date();
    var day = checkTime(today.getDate());
    var month = checkTime(today.getMonth()+1);
    var year = today.getFullYear();
    return day + "/" + month + "/" + year;
};

var createTime = function () {
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
    this.idx = -1;
    this.rendered = false;
    this.paused = false;
    this.nextMessage();
};

MessageRotator.prototype = {
    tick: function () {
        if (this.isEmpty() || this.paused || this.time < 0 || !this.rendered) {
            return;
        }
        this.time--;
        if (!this.time) {
            this.nextMessage();
        }
        if (this.items[this.idx].dynamic) {
            this.render();
        }
    },
    isEmpty: function () {
        return this.items.length === 0;
    },
    render: function () {
        if (this.isEmpty()) {
            return;
        }
        var self = this;
        var result = this.items[this.idx].fn(this.time);
        if (typeof result !== 'undefined') {
            result.always(function () {
                self.rendered = true;
            });
        } else {
            this.rendered = true;
        }

    },
    nextMessage: function () {
        if (this.isEmpty()) {
            return;
        }
        this.idx++;
        if (this.idx >= this.items.length) {
            this.idx = 0;
        }
        this.time = this.items[this.idx].time;
        this.rendered = false;
        this.render();
    },
    togglePause: function () {
        this.paused = !this.paused;
    }
};

var ajustHeight = function ($p, $m) {
    var h = $(window).height();
    $p.height(h - 150);
    $m.height(115);
};

var panelImageLink = function (id) {
    return '/api/manage/panels/' + id + '/image?t=' + new Date().getTime();
};

var weatherMapDescriptionToIconSet = function(description) {
    //http://forecastfont.iconvau.lt/
    var icons = [];
    switch (description) {
        case 'Tempo firme': {
            icons.push("icon-sun");
            break;
        }
        case 'Poucas nuvens': {
            icons.push("icon-cloud", "icon-sunny");
            break;
        }
        case 'Muitas nuvens': {
            icons.push("icon-cloud");
            break;
        }
        case 'Alguma nebulosidade': {
            icons.push("icon-mist");
            break;
        }
        case 'Névoa úmida':{
            icons.push("icon-mist");
            break;
        }
        case 'Chuva': {
            icons.push("basecloud", "icon-rainy");
            break;
        }
        case 'Pancada de chuva': {
            icons.push("basecloud", "icon-drizzle icon-sunny");
            break;
        }
        case 'Céu encoberto': {
            icons.push("basecloud", "icon-hail");
            break;
        }
    }
    return icons;
};