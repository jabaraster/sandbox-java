(function() {

$(initialize);

var maps = google.maps;

function initialize() {
    restraintDocumentScrollForIPad();
    initializeControl();
    startMap();
}

function initializeControl() {
    var menu = $('#menu');
    $('#menu-bar').click(function() {
        menu.toggle('normal');
    });
}

function restraintDocumentScrollForIPad() {
    // 原則touchmoveイベントは全てキャンセルするが、
    // UI上、touchmoveが必要なコントロールに限ってはキャンセルしない.
    $(document).on('touchmove', function(e) {
        if ($(e.originalEvent.target).attr('type') === 'range') {
            return;
        }
        e.preventDefault();
    });
}

function startMap() {
    navigator.geolocation.getCurrentPosition(showMap, failGetCurrentPosition);
}

function showMap(pPosition) {
    var position = new maps.LatLng(pPosition.coords.latitude, pPosition.coords.longitude);
    var opts = {
        zoom: 18,
        center: position
    };
    var map = new maps.Map(document.getElementById("map"), opts);
    var markers = new Markers({ map: map });

    var timer = null;
    var clearTimer = function() {
        if (timer !== null) {
            clearTimeout(timer.key);
            timer = null;
        }
    };
    maps.event.addListener(map, 'mousedown', function(e) {
        if (timer != null) {
            clearTimeout(timer.key);
        }
        var key = setTimeout(function() {
            markers.addMarker({
                position: new maps.LatLng(e.latLng.mb, e.latLng.nb)
            });
        }, 1000);
        timer = {
            key: key
        };
    });
    maps.event.addListener(map, 'mousemove', function(e) {
        clearTimer();
    });
    maps.event.addListener(map, 'mouseup', function(e) {
        clearTimer();
    });

    $('button.current-pin').click(function() {
        navigator.geolocation.getCurrentPosition(function(e) {
            markers.addMarker({
                position: new maps.LatLng(e.coords.latitude, e.coords.longitude)
            });
        });
    });
}

function addMarker(pArgs) {
    new Marker(pArgs);
    return;
    var marker = new maps.Marker({
        position: pArgs.position,
        map: pArgs.map,
        title: 'Marker'
    });
    var content = $('#templates > .balloon-content').clone();
    var iw = new maps.InfoWindow({
        content: content.get(0)
    });
    maps.event.addListener(marker, 'click', function() {
        iw.open(pArgs.map, marker);
        content.find('.marker-delete').click(function() {
            marker.setMap(null);
            return false;
        });
    });
    new maps.Geocoder().geocode({ latLng: pArgs.position }, function(pResult, pStatus) {
        var address = content.find('input[class="address"]');
        if (pResult.length == 0) {
            address.attr('placeholder', '住所が取得できませんでした');
        } else {
            address.val(pResult[0].formatted_address);
        }
    });
    content.find('button').click(function() {
        iw.close();
        return false;
    });
}

function failGetCurrentPosition() {
    alert('現在地表示はサポートされません.');
}

function Marker(pArgs) {
    var marker = new maps.Marker(pArgs);
    var content = $('#templates > .balloon-content').clone();
    var balloon = new maps.InfoWindow({
        content: content.get(0)
    });
    var self = this;
    maps.event.addListener(marker, 'click', function() {
        balloon.open(pArgs.map, marker);
        self.publish('balloonOpen', self);
    });
    maps.event.addListener(marker, 'dragend', function(e) {
    });

    content.find('.marker-delete').click(function() {
        marker.setMap(null);
        return false;
    });
    content.find('button').click(function() {
        self.closeBalloon();
        return false;
    });

    this.openBalloon = function() {
        balloon.open(pArgs.map, marker);
        self.publish('balloonOpen', self);
    };

    this.publish = function(pEventName, pEventArgs) {
        pArgs[pEventName + 'Listener'](pEventArgs);
    }

    this.closeBalloon = function() {
        balloon.close();
    }

    // ピン位置の住所の取得を試みる.
    new maps.Geocoder().geocode({ latLng: pArgs.position }, function(pResult, pStatus) {
        var address = content.find('input[class="address"]');
        if (pResult.length == 0) {
            address.attr('placeholder', '住所が取得できませんでした');
        } else {
            address.val(pResult[0].formatted_address);
        }
    });

    // バルーンを開く.
    // ただし、ピンが落ちるアニメーションをきれいに見せるために、１秒遅延させる.
    setTimeout(function() { self.openBalloon(); }, 1000);
}

function Markers(pArgs) {
    var map = pArgs.map;
    var markers = []; /* googleのものでではないMarker型 */
    var balloonOpeningMarker = null;
    var balloonOpenListener = function(pMarker) {
        if (balloonOpeningMarker === pMarker) {
            return;
        }
        if (balloonOpeningMarker !== null) {
            balloonOpeningMarker.closeBalloon();
        }
        balloonOpeningMarker = pMarker;
    };

    this.addMarker = function(pArgs) {
        pArgs.balloonOpenListener = balloonOpenListener;
        pArgs.map = map;
        if (!('animation' in pArgs)) pArgs.animation = maps.Animation.DROP;
        if (!('draggable' in pArgs)) pArgs.draggable = true;
        markers.push(new Marker(pArgs));
    }
}

})();
