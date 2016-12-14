/**
 * Created by snadal on 30/04/16.
 */


$(document).ready( function() {
    var container = document.getElementById("jsoneditor");
    var options = {
        "mode": "code",
        "indentation": 2
    };
    var editor = new JSONEditor(container, options);

    // set json



    var json = {
        "SocialNetworksMonitoredData": {
            "idOutput": "12345",
            "confId": "67890",
            "searchTimeStamp": "2016-07-19 17:23:00.000",
            "numDataItems": 1,
            "DataItems": [
                {
                    "idItem": "6253282",
                    "timeStamp": "2016-05-25 20:03",
                    "message": "Game on. Big ten network in 10 mins. Hoop for water. Flint we got ya back",
                    "author": "@SnoopDogg",
                    "link": "https://twitter.com/SnoopDogg/status/734894106967703552"
                }
            ]
        }
    };
    editor.set(json);

    $('#submitRelease').on("click", function(e){
        e.preventDefault();

        var release = new Object();
        release["event"] = $("#event").val();
        release["schemaVersion"] = $("#schemaVersion").val();
        release["jsonInstances"] = JSON.stringify(editor.get());

        $.ajax({
            url: '/release',
            type: 'POST',
            data: release
        }).done(function() {
            window.location.href = '/manage_releases';
        }).fail(function(err) {
            alert("error "+JSON.stringify(err));
        });
    });
});