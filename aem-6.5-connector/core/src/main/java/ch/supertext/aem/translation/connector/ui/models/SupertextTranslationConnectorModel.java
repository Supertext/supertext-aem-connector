/*
*************************************************************************
Supertext AG
Copyright 2020 Supertext AG
Copyright 2018 Adobe Systems Incorporated
All Rights Reserved.
 
NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
terms of the Adobe license agreement accompanying it.  If you have received this file from a
source other than Adobe, then your use, modification, or distribution of it requires the prior
written permission of Adobe.
*************************************************************************
 */


package ch.supertext.aem.translation.connector.ui.models;

import ch.supertext.aem.translation.connector.core.SupertextTranslationCloudConfig;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/*
 *  Sling Model used in editform.html sightly file for fetching the supertext cloud config input fields for populating the form
 *  For more info about Sling Model refer https://sling.apache.org/documentation/bundles/models.html#osgi-service-filters
 */

@Model(adaptables = SlingHttpServletRequest.class)
public class SupertextTranslationConnectorModel {

    private static final Logger log = LoggerFactory.getLogger(SupertextTranslationConnectorModel.class);

    @Self
    private SlingHttpServletRequest request;

    private ResourceResolver resourceResolver;
    private String supertextConfigPath;
    private Resource supertextConfigResource;

    @PostConstruct
    public void postConstruct() throws Exception {
       
        supertextConfigPath = request.getRequestPathInfo().getSuffix();
        
        log.debug(supertextConfigPath);
        
        resourceResolver = request.getResourceResolver();
        supertextConfigResource = resourceResolver.getResource(supertextConfigPath);
    }

    /*
     *  Get the server url for the configuration
     */
    public String getServerUrl() {
        return SupertextModelUtils.getStringPropertyFromContent(supertextConfigResource, SupertextTranslationCloudConfig.PROPERTY_SERVER_URL, log);
    }

    public String getUsername() {
        return SupertextModelUtils.getStringPropertyFromContent(supertextConfigResource, SupertextTranslationCloudConfig.PROPERTY_USERNAME, log);
    }

    public String getApiKey() {
        return SupertextModelUtils.getStringPropertyFromContent(supertextConfigResource, SupertextTranslationCloudConfig.PROPERTY_API_KEY, log);
    }

    /*
     *  form action attribute (post path where the configuration input values would be saved), jcr:content node of the configuration for supertext
     */
    public String getFormPostPath() {
        return supertextConfigPath + '/' + JcrConstants.JCR_CONTENT;
    }

}
