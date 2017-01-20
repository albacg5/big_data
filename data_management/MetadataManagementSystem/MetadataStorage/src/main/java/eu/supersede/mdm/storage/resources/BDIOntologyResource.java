package eu.supersede.mdm.storage.resources;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import eu.supersede.mdm.storage.model.bdi_ontology.generation.BDIOntologyGenerationStrategies;
import eu.supersede.mdm.storage.model.bdi_ontology.generation.Strategy_CopyFromSources;
import eu.supersede.mdm.storage.util.Utils;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.jena.query.*;
import org.bson.Document;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by snadal on 22/11/16.
 */
@Path("")
public class BDIOntologyResource {

    private MongoCollection<Document> getReleasesCollection(MongoClient client) {
        return client.getDatabase(context.getInitParameter("system_metadata_db_name")).getCollection("releases");
    }

    private MongoCollection<Document> getBDIOntologyCollection(MongoClient client) {
        return client.getDatabase(context.getInitParameter("system_metadata_db_name")).getCollection("bdi_ontologies");
    }

    @Context
    ServletContext context;

    @GET
    @Path("bdi_ontology/")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response GET_BDI_ontology() {
        System.out.println("[GET /bdi_ontology/]");

        MongoClient client = Utils.getMongoDBClient(context);
        // Non-complete ontologies means the ones where the release data is not populated
        List<String> nonCompleteOntologies = Lists.newArrayList();
        List<String> completeOntologies = Lists.newArrayList();
        getBDIOntologyCollection(client).find().iterator().forEachRemaining(document -> nonCompleteOntologies.add(document.toJson()));

        nonCompleteOntologies.forEach(strOntology -> {
            JSONObject objOntology = (JSONObject) JSONValue.parse(strOntology);
            JSONArray arrayReleases = (JSONArray) objOntology.get("releases");

            JSONArray releases = new JSONArray();
            arrayReleases.forEach(releaseID -> {
                Document query = new Document("releaseID",releaseID);
                Document res = getReleasesCollection(client).find(query).first();
                releases.add((JSONObject)JSONValue.parse(res.toJson()));
            });

            objOntology.put("releasesData",releases);
            completeOntologies.add(objOntology.toJSONString());
        });

        client.close();
        return Response.ok(new Gson().toJson(completeOntologies)).build();
    }

    @GET
    @Path("bdi_ontology/{bdi_ontologyID}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response GET_BDI_ontology(@PathParam("bdi_ontologyID") String bdi_ontologyID) {
        System.out.println("[GET /bdi_ontology/] bdi_ontologyID = "+bdi_ontologyID);

        MongoClient client = Utils.getMongoDBClient(context);
        Document query = new Document("bdi_ontologyID",bdi_ontologyID);
        Document res = getBDIOntologyCollection(client).find(query).first();
        client.close();

        return Response.ok((res.toJson())).build();
    }


    /**
     * POST a BDI Ontology
     */
    @POST @Path("bdi_ontology/")
    @Consumes("text/plain")
    public Response POST_BDI_ontology(String body) throws FileNotFoundException {
        System.out.println("[POST /bdi_ontology/] body = "+body);
        JSONObject objBody = (JSONObject) JSONValue.parse(body);

        MongoClient client = Utils.getMongoDBClient(context);

        objBody.put("bdi_ontologyID", UUID.randomUUID().toString());
        getBDIOntologyCollection(client).insertOne(Document.parse(objBody.toJSONString()));

        client.close();
        return Response.ok(objBody.toJSONString()).build();
    }

    @POST @Path("bdi_ontology/generationStrategy")
    @Consumes("text/plain")
    public Response POST_BDI_ontology_G_and_M(String body) throws FileNotFoundException {
        System.out.println("[POST /bdi_ontology/generationStrategy] body = "+body);
        JSONObject objBody = (JSONObject) JSONValue.parse(body);

        JSONObject out = new JSONObject();

        MongoClient client = Utils.getMongoDBClient(context);
        if (objBody.get("generationStrategy").equals(BDIOntologyGenerationStrategies.COPY_FROM_SOURCE.toString())) {
            JSONObject graphs = Strategy_CopyFromSources.copyFromSourcesStrategy(this.context,getReleasesCollection(client),(JSONArray)objBody.get("releases"));

            out.put("G", graphs.get("G"));
            out.put("M", graphs.get("M"));
            out.put("O", graphs.get("O"));
        }

        System.out.println(out.toJSONString());
        client.close();
        return Response.ok(out.toJSONString()).build();
    }


    @GET
    @Path("bdi_ontology_generation_strategies")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response GET_BDI_ontology_generationStrategies() {
        System.out.println("[GET /bdi_ontology_generation_strategies/]");

        JSONArray out = new JSONArray();
        for (BDIOntologyGenerationStrategies t : BDIOntologyGenerationStrategies.values()) {
            JSONObject inner = new JSONObject();
            inner.put("key",t.name());
            inner.put("val",t.val());
            out.add(inner);
        }

        return Response.ok(new Gson().toJson(out)).build();
    }
}
