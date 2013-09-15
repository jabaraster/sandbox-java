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
    // $B86B'(Btouchmove$B%$%Y%s%H$OA4$F%-%c%s%;%k$9$k$,!"(B
    // UI$B>e!"(Btouchmove$B$,I,MW$J%3%s%H%m!<%k$K8B$C$F$O%-%c%s%;%k$7$J$$(B.
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
            address.attr('placeholder', '$B=;=j$,<hF@$G$-$^$;$s$G$7$?(B');
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
    alert('$B8=:_COI=<($O%5%]!<%H$5$l$^$;$s(B.');
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

    // $B%T%s0LCV$N=;=j$N<hF@$r;n$_$k(B.
    new maps.Geocoder().geocode({ latLng: pArgs.position }, function(pResult, pStatus) {
        var address = content.find('input[class="address"]');
        if (pResult.length == 0) {
            address.attr('placeholder', '$B=;=j$,<hF@$G$-$^$;$s$G$7$?(B');
        } else {
            address.val(pResult[0].formatted_address);
        }
    });

    // $B%P%k!<%s$r3+$/(B.
    // $B$?$@$7!"%T%s$,Mn$A$k%"%K%a!<%7%g%s$r$-$l$$$K8+$;$k$?$a$K!"#1ICCY1d$5$;$k(B.
    setTimeout(function() { self.openBalloon(); }, 1000);
}

function Markers(pArgs) {
    var map = pArgs.map;
    var markers = []; /* google$B$N$b$N$G$G$O$J$$(BMarker$B7?(B */
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
