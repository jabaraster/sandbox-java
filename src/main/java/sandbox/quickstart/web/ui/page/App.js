var App = {};

App.restraintDocumentScrollForIPad = function() {
    $(document).on('touchmove', function(e) {
        if ($(e.originalEvent.target).attr('type') === 'range') {
            return;
        }
        e.preventDefault();
    });
};
