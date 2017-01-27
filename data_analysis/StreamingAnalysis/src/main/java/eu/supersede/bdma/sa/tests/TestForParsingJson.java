package eu.supersede.bdma.sa.tests;

import eu.supersede.bdma.sa.utils.Utils;

/**
 * Created by snadal on 27/01/17.
 */
public class TestForParsingJson {

    public static void main(String[] args) {
        String feature = "http://www.BDIOntology.com/global/Feature/textFeedbacks/text";
        String JSON = "{\"id\":1,\"title\":\"Feedback 1\",\"userIdentification\":\"u8102390\",\"language\":\"EN\",\"createdAt\":1469701384,\"updatedAt\":1469701384,\"applicationId\":1,\"configurationId\":77,\"contextInformation\":{\"resolution\":\"1440x900\",\"androidVersion\":\"24.0\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:48.0) Gecko/20100101 Firefox/48.0\",\"localTime\":\"9:51 am\",\"timeZone\":\"-2 hours\",\"devicePixelRatio\":2,\"country\":\"CH\",\"region\":\"Graubunden\"},\"textFeedbacks\":[{\"text\":\"This is the feedback text\",\"mechanismId\":1},{\"text\":\"This is a second text mechanisms's text\",\"mechanismId\":2}],\"ratingFeedbacks\":[{\"rating\":5,\"mechanismId\":5},{\"rating\":1,\"mechanismId\":6}],\"audioFeedbacks\":[{\"part\":\"audio1\",\"duration\":25,\"fileExtension\":\"mp3\",\"mechanismId\":911},{\"part\":\"audio2\",\"duration\":20,\"fileExtension\":\"mp3\",\"mechanismId\":912}],\"screenshotFeedbacks\":[{\"name\":\"screenshot-website\",\"part\":\"screenshot1\",\"mechanismId\":3,\"fileExtension\":\"png\"},{\"name\":\"screenshot-2016-08-17\",\"part\":\"screenshot2\",\"mechanismId\":4,\"fileExtension\":\"jpg\"}],\"attachmentFeedbacks\":[{\"part\":\"attachment1\",\"name\":\"my_feedback\",\"extension\":\"pdf\",\"mechanismId\":812},{\"part\":\"attachment2\",\"name\":\"hey_read_this\",\"extension\":\"xdoc\",\"mechanismId\":812},{\"part\":\"attachment3\",\"name\":\"image_senercon_page\",\"extension\":\"png\",\"mechanismId\":812}],\"categoryFeedbacks\":[{\"mechanismId\":12,\"categories\":[{\"text\":null,\"category_types_id\":2},{\"text\":null,\"category_types_id\":3},{\"text\":\"my own category\",\"category_types_id\":null}]},{\"mechanismId\":13,\"categories\":[{\"text\":null,\"category_types_id\":10},{\"text\":null,\"category_types_id\":11},{\"text\":\"Praise\",\"category_types_id\":null}]}]}";

        System.out.println(Utils.extractFeatures(JSON,feature));
    }
}
