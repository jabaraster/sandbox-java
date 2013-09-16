$(initialize);

var maps = google.maps;
var markers;

function initialize() {
    App.restraintDocumentScrollForIPad();
    initializeControl();
    startMap();
}

function initializeControl() {
    var menu = $('#menu');
    $('.show-search').click(function() {
        $('.buildings-container').show('normal', function() {
            document.getElementById(buildingsSearcherId).click();
        });
    });
}

function startMap() {
    var success = function(e) {
        showMap(e.coords);
    };
    var fail = function() {
        showMap({ latitude: 33.592421, longitude: 130.354093 });
    };
    navigator.geolocation.getCurrentPosition(success, fail);
}

function showMap(pPosition) {
    var position = new maps.LatLng(pPosition.latitude, pPosition.longitude);
    var opts = {
        zoom: 18,
        center: position
    };
    var map = new maps.Map(document.getElementById("map"), opts);
    $('button.function').show('slow');

    markers = new Markers({ map: map });

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
            markers.addNewMarker({
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
            markers.addNewMarker({
                position: new maps.LatLng(e.coords.latitude, e.coords.longitude)
            });
        }, failGetCurrentPosition);
    });

    $('div.BuildingListPanel').on('click', 'a.address-link', function() {
        var id = parseInt($(this).next().val());
        var marker = markers.getMarker(id);
        if (marker) marker.openBalloon();
    });
}

function failGetCurrentPosition() {
    alert('現在地が取得できません！');
}

function Marker(pArgs) {
    var marker = new maps.Marker(pArgs);
    var content = $('#templates > .balloon-content').clone();
    var balloon = new maps.InfoWindow({
        content: content.get(0)
    });
    var savedData = null;
    var self = this;
    maps.event.addListener(marker, 'click', function() {
        balloon.open(pArgs.map, marker);
        self.publish('balloonOpen', self);
    });

    content.find('.delete-marker').click(function() {
        if (!confirm('物件情報を削除してよろしいですか？')) {
            return false;
        }
        marker.setMap(null);
        return false;
    });
    var addressInput = content.find('input[name="address"]');
    content.find('.search-address').click(function() {
        new maps.Geocoder().geocode({ latLng: marker.getPosition() }, function(pResult, pStatus) {
            if (pResult.length == 0) {
                addressInput.attr('placeholder', '住所が取得できませんでした.');
            } else {
                addressInput.val(pResult[0].formatted_address);
            }
        });
        return false;
    });
    content.find('.save').click(function() {
        if (!window.FormData) {
            alert('お使いのブラウザでは保存機能はサポートされません！');
            return;
        }
        var fd = new FormData(content.find('form').get(0));
        var pos = marker.getPosition();
        fd.append('latitude', pos.mb);
        fd.append('longitude', pos.nb);
        $.ajax({
            async: true,
            type: 'post',
            data: fd,
            contentType: false,
            processData: false,
            url: contextPath + '/rest/building',
            success: function(pData) {
                savedData = pData;
                self.closeBalloon();
                self.publish('saved', self);
                alert('保存しました！');
            },
            error: function() {
                alert('保存に失敗しました...');
            },
            dummy: null
        });
        return false;
    });

    this.openBalloon = function() {
        pArgs.map.panTo(marker.getPosition());
        self.publish('willOpenBalloon', self);
        balloon.open(pArgs.map, marker);
        self.publish('balloonOpen', self);
    };

    this.publish = function(pEventName, pEventArgs) {
        pArgs[pEventName + 'Listener'](pEventArgs);
    };

    this.closeBalloon = function() {
        balloon.close();
    };

    this.getId = function() {
        return savedData == null ? null : savedData.id;
    };

    // 吹き出しを開くが、少し時間を置かないとピンが落ちるアニメが見えなくなる.
    setTimeout(function() { self.openBalloon(); }, 1000);
}

function Markers(pArgs) {
    var map = pArgs.map;
    var markers = [];
    var id2Marker = {};
    var self = this;
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
    var savedListener = function(pMarker) {
        id2Marker[pMarker.getId()] = pMarker;
    };
    var willOpenBalloonListener = function(pMarker) {
        self.hideBuildingsPanel();
    };

    this.addNewMarker = function(pArgs) {
        pArgs.balloonOpenListener = balloonOpenListener;
        pArgs.savedListener = savedListener;
        pArgs.willOpenBalloonListener = willOpenBalloonListener;
        pArgs.map = map;
        if (!('animation' in pArgs)) pArgs.animation = maps.Animation.DROP;
        if (!('draggable' in pArgs)) pArgs.draggable = true;
        markers.push(new Marker(pArgs));
    }

    this.getMarker = function(pId) {
        return id2Marker[pId];
    };

    var buildingsPanel = $('.buildings-container');
    this.showBuildingsPanel = function() {
        buildingsPanel.show('normal');
    };
    this.hideBuildingsPanel = function() {
        buildingsPanel.hide('normal');
    };
}
