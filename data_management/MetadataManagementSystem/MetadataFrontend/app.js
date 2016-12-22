var config = require(__dirname+'/config');
var port = config.PORT;
var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var methodOverride = require('method-override');
var session = require('express-session');
var app = express();
var io = require('socket.io').listen(app.listen(port));
var flash = require('express-flash');
var multer = require('multer');
var passport = require('passport');


/*****************************************************************************************/
/*****************************************************************************************/
/*          Routing files                                                                */
/*****************************************************************************************/
/*****************************************************************************************/

var user_routes = require(__dirname+'/routes/user_routes');
var dataset_routes = require(__dirname+'/routes/dataset_routes');
var artifact_routes = require(__dirname+'/routes/artifact_routes');
var bdi_ontology_routes = require(__dirname+'/routes/bdi_ontology_routes');
var source_level_routes = require(__dirname+'/routes/source_level_routes');
var three_level_ontology_routes = require(__dirname+'/routes/three_level_ontology_routes');
var release_routes = require(__dirname+'/routes/release_routes');

/*****************************************************************************************/
/*****************************************************************************************/
/*          Server configuration                                                         */
/*****************************************************************************************/
/*****************************************************************************************/
// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');
app.use(logger('dev'));
app.use(methodOverride());
app.use(session({ secret: 'uwotm8', proxy: true, resave: true, saveUninitialized: true }));
app.use(bodyParser.urlencoded({extended: true}));
app.use(cookieParser());
app.use(bodyParser.json());                          // parse application/json
app.use(bodyParser.urlencoded({ extended: true }));  // parse application/x-www-form-urlencoded
app.use(multer());                                   // parse multipart/form-data
app.use(express.static(path.join(__dirname, 'public')));

app.use(flash());
app.use(passport.initialize());
app.use(passport.session());
//app.use(multer({ dest: './uploads/'}));

//Sergi: Avoid cache and 304 errors
app.disable('etag');

require('./auth');

/*****************************************************************************************/
/*****************************************************************************************/
/*          Resources                                                                    */
/*****************************************************************************************/
/*****************************************************************************************/


/********** User resource ****************************************************************/

app.post('/users', user_routes.postUser);

app.post('/login', passport.authenticate('local', {
    successRedirect: '/',
    failureRedirect: '/login',
    failureFlash: true
}));

app.get('/logout', function(req, res){
    req.logout();
    res.redirect('/');
});


/********** Dataset resource *************************************************************/

app.get('/datasets', dataset_routes.getDatasets);
app.get('/datasets/:datasetID', dataset_routes.getDataset);
app.post('/datasets/xml', dataset_routes.postXMLDataset);
app.post('/datasets/json', dataset_routes.postJSONDataset);
//app.post('/datasets/relational', dataset_routes.postRelationalDataset);
app.delete('/datasets/:datasetID', dataset_routes.deleteDataset);

/********** Generic Artifact resource *****************************************************/

app.get('/artifacts/:artifactType', artifact_routes.getArtifacts);
app.get('/artifacts/:artifactType/:artifactID', artifact_routes.getArtifact);
app.get('/artifacts/:artifactType/:artifactID/content', artifact_routes.getArtifactContent);
app.get('/artifacts/:artifactType/:artifactID/graphical', artifact_routes.getArtifactGraphical);
app.delete('/artifacts/:artifactType/:artifactID', artifact_routes.deleteArtifact);
app.post('/artifacts/:artifactType/:artifactID/triple', artifact_routes.postTriple);

/********** BDI Ontology resource ********************************************************/

app.get('/bdi_ontology/:bdi_ontologyID', bdi_ontology_routes.getBDIOntology);
app.get('/bdi_ontology', bdi_ontology_routes.getAllBDIOntologies);
app.post('/bdi_ontology', bdi_ontology_routes.postBDIOntology);

/********** Global Level resource ********************************************************/

//app.post('/globalLevel', global_level_routes.postGlobalLevel);

/********** Source Level resource ********************************************************/

app.post('/sourceLevel', source_level_routes.postSourceLevel);

/********** Three-level Ontology resource ************************************************/

app.post('/three_level_ontology', three_level_ontology_routes.postThreeLevelOntology);

/********** Release resource *************************************************************/

app.get('/release/:releaseID', release_routes.getRelease);
app.get('/release', release_routes.getAllReleases);
app.post('/release', release_routes.postRelease);

/*****************************************************************************************/
/*****************************************************************************************/
/*          Frontend Pages                                                               */
/*****************************************************************************************/
/*****************************************************************************************/

app.get('/', checkAuthenticated, function(req,res) {
    res.render('index', {user:req.session.passport.user});
});

app.get('/login', function (req, res) {
    res.setHeader('Last-Modified', (new Date()).toUTCString());
    res.render('login', {message: req.flash('error')});
});

app.get('/registration', function(req, res) {
    res.setHeader('Last-Modified', (new Date()).toUTCString());
    res.render('register_user');
});

/********** Releases section ***************************************************************/

app.get('/new_release', checkAuthenticated, function(req,res) {
    res.render('new_release', {user:req.session.passport.user});
});

app.get('/manage_releases', checkAuthenticated, function(req,res) {
    res.render('manage_releases', {user:req.session.passport.user});
});

app.get('/view_release', checkAuthenticated, function(req,res) {
    res.render('view_release', {user:req.session.passport.user});
});

/********** BDI Ontology section ***********************************************************/

app.get('/new_bdi_ontology', checkAuthenticated, function(req,res) {
    res.render('new_bdi_ontology', {user:req.session.passport.user});
});

app.get('/manage_bdi_ontologies', checkAuthenticated, function(req,res) {
    res.render('manage_bdi_ontologies', {user:req.session.passport.user});
});


/********** Domain Ontology section ********************************************************/

/*app.get('/upload_domain_ontology', checkAuthenticated, function(req,res) {
    res.render('upload_domain_ontology', {user:req.session.passport.user});
});

app.get('/manage_domain_ontologies', checkAuthenticated, function(req,res) {
    res.render('manage_domain_ontologies', {user:req.session.passport.user});
});

app.get('/view_domain_ontology', checkAuthenticated, function(req,res) {
    res.render('view_domain_ontology', {user:req.session.passport.user});
});*/

/********** Global Level section *******************************************************/
/*
app.get('/new_global_level', checkAuthenticated, function(req,res) {
    res.render('new_global_level', {user:req.session.passport.user});
});

app.get('/manage_global_levels', checkAuthenticated, function(req,res) {
    res.render('manage_global_levels', {user:req.session.passport.user});
});
*/
app.get('/view_global_level', checkAuthenticated, function(req,res) {
    res.render('view_global_level', {user:req.session.passport.user});
});

/********** Source Level section ******************************************************/
/*
app.get('/new_source_level', checkAuthenticated, function(req,res) {
    res.render('new_source_level', {user:req.session.passport.user});
});

app.get('/manage_source_levels', checkAuthenticated, function(req,res) {
    res.render('manage_source_levels', {user:req.session.passport.user});
});*/

app.get('/view_source_level', checkAuthenticated, function(req,res) {
    res.render('view_source_level', {user:req.session.passport.user});
});

/********** Three-level Ontology section ***************************************************/

app.get('/new_three_level_ontology', checkAuthenticated, function(req,res) {
    res.render('new_three_level_ontology', {user:req.session.passport.user});
});

app.get('/manage_three_level_ontologies', checkAuthenticated, function(req,res) {
    res.render('manage_three_level_ontologies', {user:req.session.passport.user});
});

app.get('/view_three_level_ontology', checkAuthenticated, function(req,res) {
    res.render('view_three_level_ontology', {user:req.session.passport.user});
});

/********** Reference Dataset section ********************************************************/

app.get('/new_dataset', checkAuthenticated, function(req,res) {
    res.render('new_dataset', {user:req.session.passport.user});
});

app.get('/manage_datasets', checkAuthenticated, function(req,res) {
    res.render('manage_datasets', {user:req.session.passport.user});
});

app.get('/view_dataset', checkAuthenticated, function(req,res) {
    res.render('view_dataset', {user:req.session.passport.user});
});


/****************************** **********************************************************/



function checkAuthenticated(req, res, next) {
    if (req.isAuthenticated()) { return next(); }
    res.redirect('/login');
}

module.exports = app;
