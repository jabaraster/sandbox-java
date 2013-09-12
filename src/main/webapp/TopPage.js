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
    $('#menuBar').click(function() {
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
    var timer = null;
    maps.event.addListener(map, 'mousedown', function(e) {
        if (timer != null) {
            clearTimeout(timer.key);
        }
        var key = setTimeout(function() {
            addMarker({
                map: map,
                position: new maps.LatLng(e.latLng.mb, e.latLng.nb)
            });
        }, 1000);
        timer = {
            key: key
        };
    });
    maps.event.addListener(map, 'mouseup', function(e) {
        if (timer != null) {
            clearTimeout(timer.key);
        }
    });
}

function addMarker(pArgs) {
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

})();
