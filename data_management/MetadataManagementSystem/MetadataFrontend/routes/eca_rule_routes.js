/**
 * Created by snadal on 7/06/16.
 */
var fs = require('fs'),
    config = require(__dirname+'/../config'),
    request = require('request'),
    randomstring = require("randomstring"),
    async = require('async');

exports.getEcaRule = function (req, res, next) {
    request.get(config.METADATA_DATA_LAYER_URL + "eca_rule/"+req.params.eca_ruleID, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.status(200).json(JSON.parse(body));
        } else {
            res.status(500).send("Error retrieving ECA Rule");
        }
    });
};

exports.getAllEcaRules = function (req, res, next) {
    request.get(config.METADATA_DATA_LAYER_URL + "eca_rule/", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.status(200).json(JSON.parse(body));
        } else {
            res.status(500).send("Error retrieving list of ECA rules");
        }
    });
};

exports.postEcaRule = function (req, res, next) {
    if (!(req.body.hasOwnProperty('name')) || req.body.name == null
        || !(req.body.hasOwnProperty('globalLevel')) || req.body.globalLevel == null
        || !(req.body.hasOwnProperty('graph')) || req.body.graph == null
        || !(req.body.hasOwnProperty('simpleClauses')) || req.body.simpleClauses == null
        || !(req.body.hasOwnProperty('action')) || req.body.action == null) {
        res.status(400).json({msg: "(Bad Request) data format: {name, globalLevel, graph, simpleClauses, action}"});
    } else {
        var rule = new Object();
        rule.name = req.body.name;
        rule.globalLevel = req.body.globalLevel;
        rule.graph = req.body.graph;
        rule.simpleClauses = req.body.simpleClauses;
        //rule.windowTime = req.body.windowTime;
        //rule.windowSize = req.body.windowSize;
        rule.action = req.body.action;

        console.log("Posting "+JSON.stringify(rule));

        request.post({
            url: config.METADATA_DATA_LAYER_URL + "eca_rule/",
            body: JSON.stringify(rule)
        }, function done(error, response, body) {
            console.log("Got response "+error+" - "+response+" - "+body);
            if (!error && response.statusCode == 200) {
                res.status(200).json("ok");
            } else {
                res.status(500).send("Error posting rule");
            }
        });
    }
};

exports.getEcaRuleOperatorTypes = function (req, res, next) {
    request.get(config.METADATA_DATA_LAYER_URL + "eca_rule_operator_types/", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.status(200).json(JSON.parse(body));
        } else {
            res.status(500).send("Error retrieving list of ECA Rule operator types");
        }
    });
};

exports.getEcaRulePredicateTypes = function (req, res, next) {
    request.get(config.METADATA_DATA_LAYER_URL + "eca_rule_predicate_types/", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.status(200).json(JSON.parse(body));
        } else {
            res.status(500).send("Error retrieving list of ECA Rule predicate types");
        }
    });
};

exports.getEcaRuleActionTypes = function (req, res, next) {
    request.get(config.METADATA_DATA_LAYER_URL + "eca_rule_action_types/", function (error, response, body) {
        if (!error && response.statusCode == 200) {
            res.status(200).json(JSON.parse(body));
        } else {
            res.status(500).send("Error retrieving list of ECA Rule action types");
        }
    });
};
