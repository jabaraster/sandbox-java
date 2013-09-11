(function() {

$(initialize);

function initialize() {
    document.addEventListener("touchmove", function(e){e.preventDefault();}, false);
    navigator.geolocation.getCurrentPosition(showMap, failGetCurrentPosition);
}

function showMap(pPosition) {
    console.log(pPosition);
    var latlng = new google.maps.LatLng(pPosition.coords.latitude, pPosition.coords.longitude);
    var opts = {
        zoom: 18,
//        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: latlng
    };
    var map = new google.maps.Map(document.getElementById("map"), opts);
    google.maps.event.addListener(map, "click", function(e) {
        console.log(e);
//        var ll = new googl.maps.LatLng(e.
    });
}

function failGetCurrentPosition() {
    console.log(arguments);
}

})();
