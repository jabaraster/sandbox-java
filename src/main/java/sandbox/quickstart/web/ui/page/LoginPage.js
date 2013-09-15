(function() {

$(initialize);

var maps = google.maps;

function initialize() {
    startMap();
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
    new maps.Map(document.getElementById("map"), opts);
}

})();