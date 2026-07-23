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

package ch.supertext.aem.translation.connector.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.license.ProductInfoProvider;
import com.adobe.granite.translation.api.TranslationConfig;
import com.adobe.granite.translation.api.TranslationConstants.TranslationMethod;
import com.adobe.granite.translation.api.TranslationException;
import com.adobe.granite.translation.api.TranslationService;
import com.adobe.granite.translation.api.TranslationServiceFactory;

import ch.supertext.aem.translation.connector.core.ProxyConfiguration;
import ch.supertext.aem.translation.connector.core.SupertextTranslationCloudConfig;
import com.adobe.granite.translation.core.TranslationCloudConfigUtil;

@Component(service = TranslationServiceFactory.class, immediate = true, configurationPid = "ch.supertext.aem.translation.connector.core.impl.SupertextTranslationServiceFactoryImpl", property = {
		Constants.SERVICE_DESCRIPTION + "=Configurable settings for the Supertext Translation connector",
		"label" + "=Supertext Translation Connector Factory" })

@Designate(ocd = SupertextServiceConfiguration.class)
public class SupertextTranslationServiceFactoryImpl implements TranslationServiceFactory {
	private static final Logger log = LoggerFactory.getLogger(SupertextTranslationServiceFactoryImpl.class);

	protected String factoryName;

	protected Boolean isPreviewEnabled;

	protected Boolean isPseudoLocalizationDisabled;

	@Reference
	TranslationCloudConfigUtil cloudConfigUtil;

	@Reference
	TranslationConfig translationConfig;

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Reference
	ProxyConfiguration networkConfiguration;

	@Reference
	ProductInfoProvider productInfoProvider;

	private List<TranslationMethod> supportedTranslationMethods;

	public SupertextTranslationServiceFactoryImpl() {
		log.trace("SupertextTranslationServiceFactoryImpl.");

		supportedTranslationMethods = new ArrayList<TranslationMethod>();
		supportedTranslationMethods.add(TranslationMethod.HUMAN_TRANSLATION);
	}

	@Override
	public TranslationService createTranslationService(TranslationMethod translationMethod, String cloudConfigPath)
			throws TranslationException {
		log.debug("SupertextTranslationServiceFactoryImpl.createTranslationService: {}", cloudConfigPath);

		SupertextTranslationCloudConfig supertextCloudConfg = (SupertextTranslationCloudConfig) cloudConfigUtil
				.getCloudConfigObjectFromPath(
						SupertextTranslationCloudConfig.class, cloudConfigPath);

		String serverUrl = "";
		String username = "";
		String apiKey = "";
		JcrOperator jcrOperator = new JcrOperator(resourceResolverFactory);

		if (supertextCloudConfg != null) {
			serverUrl = supertextCloudConfg.getServerUrl();
			username = supertextCloudConfg.getUsername();
			apiKey = supertextCloudConfg.getApiKey();
		}

		if (serverUrl == null || serverUrl.isEmpty()) {
			jcrOperator.startSession();

			try {
				serverUrl = jcrOperator.getFallbackServerUrl();
				username = jcrOperator.getFallbackUsername();
				apiKey = jcrOperator.getFallbackApiKey();
			} catch (Exception e) {
				log.error("Getting fallback configuration failed.", e);
			} finally {
				jcrOperator.endSession();
			}
		}

		Map<String, String> availableLanguageMap = new HashMap<String, String>();
		Map<String, String> availableCategoryMap = new HashMap<String, String>();

		return new SupertextTranslationServiceImpl(
				availableLanguageMap,
				availableCategoryMap,
				factoryName,
				translationConfig,
				new SupertextApiClient(
						serverUrl,
						username,
						apiKey,
						new HttpClientFactoryImpl(networkConfiguration),
						productInfoProvider.getProductInfo()),
				jcrOperator);
	}

	@Override
	public List<TranslationMethod> getSupportedTranslationMethods() {
		log.trace("SupertextTranslationServiceFactoryImpl.getSupportedTranslationMethods");
		return supportedTranslationMethods;
	}

	@Override
	public Class<?> getServiceCloudConfigClass() {
		log.trace("SupertextTranslationServiceFactoryImpl.getServiceCloudConfigClass");
		return SupertextTranslationCloudConfig.class;
	}

	@Activate
	protected void activate(SupertextServiceConfiguration config) {
		log.trace("Starting function: activate");

		factoryName = config.getTranslationFactory();

		isPreviewEnabled = config.isPreviewEnabled();

		isPseudoLocalizationDisabled = config.isPseudoLocalizationDisabled();

		if (log.isTraceEnabled()) {
			log.trace("Activated TSF with the following:");
			log.trace("Factory Name: {}", factoryName);
			log.trace("Preview Enabled: {}", isPreviewEnabled);
			log.trace("Psuedo Localization Disabled: {}", isPseudoLocalizationDisabled);
		}
	}

	@Override
	public String getServiceFactoryName() {
		log.trace("Starting function: getServiceFactoryName");
		return factoryName;
	}
}
