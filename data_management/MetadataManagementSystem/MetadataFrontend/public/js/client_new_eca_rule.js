/**
 * Created by snadal on 07/06/16.
 */

var tabCount = 1;
function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function registerCloseEvent() {
    $(".closeTab").click(function () {

        var tabContentId = $(this).parent().attr("href");
        $(this).parent().parent().remove(); //remove li of tab
        $('#tabPanel a:last').tab('show'); // Select first tab
        $(tabContentId).remove(); //remove respective tab content
         --tabCount;
    });
}

function getLeftOperators() {
    $.get("/bdi_ontology/"+$("#event"+tabCount).val(), function(ontology) {
        alert("jhfn");
        console.log("/global_level/"+encodeURIComponent(ontology.globalLevel)+"/features");
        $.get("/global_level/"+encodeURIComponent(ontology.globalLevel)+"/features", function(data) {
            //$("#feature").val(null);
            $("#leftOperator"+tabCount).empty().trigger('change');

            _.each(JSON.parse(data), function(element,index,list) {
                $("#leftOperator"+tabCount).append($('<option value="'+element.iri+'">').text(element.name +" ("+element.iri+")"));
            });
            $("#leftOperator"+tabCount).select2({
                theme: "bootstrap"
            });
        });
    });
 }

function getEvents() {
    $("#bdiOntology").on("change", function() {
        var id = $("#bdiOntology option:selected").last().val();
        $.get("/bdi_ontology", function (data) {
            _.each(data, function(element,index,list) {
                var obj = JSON.parse(element);
                if (id == obj.bdi_ontologyID) {
                    $("#event"+tabCount).append($('<option value="' + obj.bdi_ontologyID + '">').text(obj.name));
                    getLeftOperators();
                    return;
                }
            });
            $("#event"+tabCount).select2({
                theme: "bootstrap"
            });
        });
    });
}

function getComparators() {
    $.get("/eca_rule_predicate_types", function(data) {
        _.each(data, function(element,index,list) {
            $("#comparator"+tabCount).append($('<option value="'+element.key+'">').text(element.key + " (" + element.val + ")"));
        });
        $("#comparator"+tabCount).select2({
            theme: "bootstrap"
        });
    });
}

$(window).load(function() {
    $('#tabPanel li:first').tab('show'); // Select first tab

    getComparators();
    getEvents();

    $.get("/bdi_ontology", function(data) {
        _.each(data, function(element,index,list) {
            var obj = JSON.parse(element);
            $("#bdiOntology").append($('<option value="'+obj.bdi_ontologyID+'">').text(obj.name));
        });
        $("#bdiOntology").select2({
            theme: "bootstrap"
        })
        $("#bdiOntology").trigger('change');
    });

    $.get("/eca_rule_action_types", function(data) {
        _.each(data, function(element,index,list) {
            $("#action").append($('<option value="'+element.key+'">').text(element.val));
        });
        $("#action").select2({
            action: "bootstrap"
        });
    });

    $('#addSimpleClause').on("click", function(e) {
        e.preventDefault();
        ++tabCount;
        $("#tabPanel").append($('<li role="presentation"><a id="button_tab_'+(tabCount)+'" href="#tab_'+(tabCount)+'" aria-controls="settings" role="tab" data-toggle="tab">'+'SC'+(tabCount)+'<button type="button" class="close closeTab">&nbsp &times;</button></a></li>'));
        $("#tabContent").append($('<div id="tab_'+(tabCount)+'" role="tabpanel" class="tab-pane fill">'+'<div class="form-group"> <label class="col-lg-2 control-label">'+'Name '+(tabCount)+'</label><div class="col-lg-10"><input class="form-control" id="name'+(tabCount)+'" type="text" required="required"> </input></div></div>'+
            '<div class="form-group"> <label class="col-lg-2 control-label">'+'Event '+(tabCount)+'</label><div class="col-lg-10"><select class="event" id="event'+(tabCount)+'" style="width:100%"></select></div></div>' +
            '<div class="form-group"> <label class="col-lg-2 control-label">'+'Left Operator '+(tabCount)+'</label><div class="col-lg-10"><select class="leftOperator" id="leftOperator'+(tabCount)+'" style="width:100%"></select></div></div>' +
            '<div class="form-group"> <label class="col-lg-2 control-label">'+'Comparator '+(tabCount)+'</label><div class="col-lg-10"><select class="comparator" id="comparator'+(tabCount)+'" style="width:100%"> </select></div></div>' +
            '<div class="form-group"> <label class="col-lg-2 control-label">'+'Right Operator '+(tabCount)+'</label><div class="col-lg-10"><input class="rightOperator form-control" id="rightOperator'+(tabCount)+'" type="text" required="required"> </input></div></div></div>'));
        registerCloseEvent();
        getComparators();
    });

    $('#submitEcaRule').on("click", function(e){
        e.preventDefault();

        $.get("/bdi_ontology/"+$("#bdiOntology").val(), function(ontology) {
            var Eca_Rule = new Object();
            Eca_Rule.name = $("#name").val();
            Eca_Rule.globalLevel = ontology.globalLevel;
            Eca_Rule.graph = ontology.rules;
        /*
            Eca_Rule.windowTime = $("#windowTime").val();
            Eca_Rule.windowSize = $("#windowSize").val();
        */
            console.log($("#feature"+tabCount).val());
            Eca_Rule.simpleClauses = new Array();
            for (i = 1; i <= tabCount; ++i) {
                var sc = new Array(5);
                sc[0] = $("#feature"+i).val();
                sc[1] = $("#operator"+i).val();
                sc[2] = $("#predicate"+i).val();
                sc[3] = $("#value"+i).val();
                sc[4] = $("#filter"+i).val();
                Eca_Rule.simpleClauses.push(sc);
            }

            Eca_Rule.action = $("#action").val();

            $.ajax({
                url: '/eca_rule',
                type: 'POST',
                data: Eca_Rule
            }).done(function() {
                window.location.href = '/manage_eca_rules';
            }).fail(function(err) {
                alert("error "+JSON.stringify(err));
            });

        });

    });

});