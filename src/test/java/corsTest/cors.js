$(document).ready(function() {
    $.ajax({
        url: "http://localhost:8080/printingJson"
    }).then(function(data, status, jqxhr) {
       $('.greeting-id').append(data);
       $('.greeting-content').append(data);
       console.lot(data);
       console.log(jqxhr);
    });
});