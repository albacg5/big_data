package eu.supersede.mdm.storage.util;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;

/**
 * Created by snadal on 24/11/16.
 */
public class RDFUtil {

    public static void addTriple(OntModel model, String s, String p, String o) {
        model.add(new ResourceImpl(s), new PropertyImpl(p), new ResourceImpl(o));
    }

}