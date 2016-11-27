package eu.supersede.mdm.storage.bdi_ontology;

import com.google.common.io.Files;
import eu.supersede.mdm.storage.bdi_ontology.metamodel.SourceLevel;
import eu.supersede.mdm.storage.parsers.JSON_to_SourceLevel;
import eu.supersede.mdm.storage.util.HTTPRequests;
import eu.supersede.mdm.storage.util.RDFUtil;
import eu.supersede.mdm.storage.util.TempFiles;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import javax.ws.rs.core.Response;
import javax.xml.transform.Source;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Created by snadal on 22/11/16.
 */
public class Release {

    public static String newRelease(String Evt, String V, String JSON_artifact) throws FileNotFoundException {
        JSONObject J = (JSONObject) JSONValue.parse(JSON_artifact);
        //Line 2 @ Algorithm 2
        String Evt_uri = SourceLevel.EVENT.val()+"/"+Evt;

        /**
         * TODO: line 3,4,5 Algorithm 2
         */
        OntModel S = ModelFactory.createOntologyModel();
        RDFUtil.addTriple(S,Evt_uri,Namespaces.rdf.val()+"type",SourceLevel.EVENT.val());

        //Line 6 @ Algorithm 2
        String V_uri = SourceLevel.SCHEMA_VERSION.val()+"/"+V;
        //Line 7 @ Algorithm 2
        RDFUtil.addTriple(S,V_uri,Namespaces.rdf.val()+"type",SourceLevel.SCHEMA_VERSION.val());
        //Line 8 @ Algorithm 2
        RDFUtil.addTriple(S,Evt_uri,SourceLevel.PRODUCES.val(),V_uri);
        //Line 9 @ Algorithm 2
        RDFUtil.addTriple(S,V_uri,SourceLevel.FORMAT.val(), "https://www.iana.org/assignments/media-types/application/json");

        //Line 10 @ Algorithm 2
        //OntModel Snew = ModelFactory.createOntologyModel();
        JSON_to_SourceLevel.extractRec(S,J,V_uri);

        // Generate a Kafka topic for the new release
        String kafka_topic = UUID.randomUUID().toString();
        String kafka_topic_uri = SourceLevel.KAFKA_TOPIC.val()+"/"+kafka_topic;
        RDFUtil.addTriple(S,kafka_topic_uri,Namespaces.rdf.val()+"type",SourceLevel.KAFKA_TOPIC.val());
        RDFUtil.addTriple(S, V_uri, SourceLevel.HAS_KAFKA_TOPIC.val(), kafka_topic_uri);


        // Output RDF
        String tempFileForS = TempFiles.getTempFile();
        S.write(new FileOutputStream(tempFileForS),"RDF/XML-ABBREV");


        System.out.println("Release stored in file "+tempFileForS);

        String content = "";
        try {
            content = new String(java.nio.file.Files.readAllBytes(new java.io.File(tempFileForS).toPath()));
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        JSONObject response = new JSONObject();
        response.put("rdf",content);
        response.put("kafkaTopic", kafka_topic);

        return response.toJSONString();
    }

}
