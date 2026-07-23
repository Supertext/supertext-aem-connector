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

package ch.supertext.aem.translation.connector.core;

public interface SupertextTranslationCloudConfig {

    public static final String PROPERTY_SERVER_URL = "serverurl";
    public static final String PROPERTY_USERNAME = "username";
    public static final String PROPERTY_API_KEY = "apikey";

    public static final String RESOURCE_TYPE = "cq/translation/components/mt-cloudconfig";
    public static final String ROOT_PATH = "/etc/cloudservices/supertext-translation";

    String getServerUrl();

    String getUsername();
   
    String getApiKey();
}