/**
 * Created by snadal on 17/05/16.
 */

var config = {};

config.PORT = 3000;
config.METADATA_FRONTEND_URL = "http://localhost:"+config.PORT+"/";
config.METADATA_DATA_LAYER_URL = "http://localhost:8081/metadataDataLayer/";
config.ONTO_MATCH_MERGE_URL = "http://localhost:8082/ontoMatchMerge/";
config.DEFAULT_NAMESPACE = "http://supersede/";

module.exports = config;