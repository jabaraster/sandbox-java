$(initialize);

function initialize() {
    console.log(navigator.geolocation.getCurrentPosition);
    navigator.geolocation.getCurrentPosition(function(e) {
        console.log(e);
    });
}

function showMap(pPosition) {
    console.log(pPosition);
    var latlng = pPosition.coords;
    var opts = {
        zoom: 6,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        center: latlng
    };
    var map = new google.maps.Map(document.getElementById("map"), opts);
}
