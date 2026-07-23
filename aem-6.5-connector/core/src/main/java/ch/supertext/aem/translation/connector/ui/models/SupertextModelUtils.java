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

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Utility class for Supertext connector sling model
 */

public class SupertextModelUtils {

    // Helper for fetching properties from content node
    static String getStringPropertyFromContent(Resource resource, String property, Logger logger) {
        try {
            if (resource != null) {
                Resource contentResource = resource.getChild(JcrConstants.JCR_CONTENT);
                if (contentResource != null) {
                    Node content = contentResource.adaptTo(Node.class);
                    if (content != null && content.hasProperty(property)) {
                        return content.getProperty(property).getString();
                    }
                }
            }
        } catch (RepositoryException e) {
            logger.error("Error fetching Property {} from {}", property, resource.getPath());
        }
        return "";
    }
}
