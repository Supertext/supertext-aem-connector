/*
*************************************************************************
Supertext AG
Copyright 2020 Supertext AG
Copyright [first year code created] Adobe Systems Incorporated
All Rights Reserved.
 
NOTICE:  Adobe permits you to use, modify, and distribute this file in accordance with the
terms of the Adobe license agreement accompanying it.  If you have received this file from a
source other than Adobe, then your use, modification, or distribution of it requires the prior
written permission of Adobe.
*************************************************************************
 */

package ch.supertext.aem.translation.connector.core.impl.config;

import com.adobe.granite.translation.api.TranslationException;
import ch.supertext.aem.translation.connector.core.SupertextTranslationCloudConfig;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupertextTranslationCloudConfigImpl implements SupertextTranslationCloudConfig {
    private static final Logger log = LoggerFactory.getLogger(SupertextTranslationCloudConfigImpl.class);

    private String serverUrl;
    private String username;
    private String apiKey;
	
    public SupertextTranslationCloudConfigImpl(Resource translationConfigResource) throws TranslationException {
        log.trace("SupertextTranslationCloudConfigImpl.");

        Resource configContent;
        if (JcrConstants.JCR_CONTENT.equals(translationConfigResource.getName())) {
            configContent = translationConfigResource;
        } else {
            configContent = translationConfigResource.getChild(JcrConstants.JCR_CONTENT);
        }

        if (configContent != null) {
            ValueMap properties = configContent.adaptTo(ValueMap.class);

            this.serverUrl = properties.get(PROPERTY_SERVER_URL, "");
            this.username = properties.get(PROPERTY_USERNAME, "");
            this.apiKey = properties.get(PROPERTY_API_KEY, ""); 

            if (log.isTraceEnabled()) {
                log.trace("Created Supertext Cloud Config with the following:");
                log.trace("serverUrl: {}", serverUrl);
                log.trace("username: {}", username);
                log.trace("apiKey: {}", apiKey);
            }
        } else {
            throw new TranslationException("Error getting Cloud Config credentials",
                TranslationException.ErrorCode.MISSING_CREDENTIALS);
        }
    }

    public String getServerUrl() {
        log.trace("SupertextTranslationCloudConfigImpl.getServerUrl");
        return serverUrl;
    }

    public String getUsername() {
        log.trace("SupertextTranslationCloudConfigImpl.getUsername");
        return username;
    }
    
    public String getApiKey(){
        log.trace("SupertextTranslationCloudConfigImpl.getApiKey");
        return apiKey;
    }
}