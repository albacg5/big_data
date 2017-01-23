package eu.supersede.bdma.sa;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Maps;
import eu.supersede.integration.api.mdm.proxies.IMetadataManagement;
import eu.supersede.integration.api.mdm.proxies.MetadataManagementProxy;
import eu.supersede.integration.api.mdm.types.Release;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by snadal on 12/01/17.
 */
public class MDMProxy {

    public static Map<String, Release> getReleasesIndexedPerKafkaTopic() throws Exception {
        IMetadataManagement proxy = new MetadataManagementProxy<Object, Object>();

        Map<String, Release> m = Maps.newConcurrentMap();
        for (Release r : proxy.getAllReleases()) {
            m.put(r.getKafkaTopic(),r);
        }

        // TODO obtain that from IF MDM Proxy
        //Release R = new Release();
        //R.setKafkaTopic("f7b78d75-21b3-4c6b-82d9-4d59b4f92f1d");
        // TODO update once Yosu changes Release class
        //R.setReleaseID("/home/snadal/Bolster/DispatcherData/b3defa3b-ba7b-457e-bc5c-87cd284dd2b3.txt");


        //m.put(R.getKafkaTopic(),R);

        return Collections.unmodifiableMap(m);
    }

    public static Map<String, Tuple2<Boolean,String>> getReleasesIndexedPerKafkaTopic2() throws Exception {
        IMetadataManagement proxy = new MetadataManagementProxy<Object, Object>();

        Map<String, Tuple2<Boolean,String>> m = Maps.newConcurrentMap();
        for (Release r : proxy.getAllReleases()) {
            m.put(r.getKafkaTopic(),new Tuple2<Boolean,String>(r.isDispatch(),r.getDispatcherPath()));
        }

        return Collections.unmodifiableMap(m);
    }

    public static Collection<String> getAllReleases() throws Exception {
        /*IMetadataManagement proxy = new MetadataManagementProxy<Object, Object>();

        Collection<String> topics = Lists.newArrayList();
        for (Release r : proxy.getAllReleases()) {
            topics.add(r.getKafkaTopic());
        }*/

        Collection<String> topics = Arrays.asList("e7d9a569-f750-4200-b94e-ccd66df9e76e","a2e0b35b-85c3-4774-bff5-48f397691cfa");
        return topics;
    }

}
