package ch.supertext.aem.translation.connector.core.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Supertext Translation Service", description = "Supertext Translation Service Configuration")
public @interface SupertextServiceConfiguration {
	
	@AttributeDefinition(name = "Supertext Translation Factory Name", description = "The Unique ID associated with this Translation Factory Connector")
	String getTranslationFactory() default "Supertext Connector";
	
	@AttributeDefinition(name = "Enable Preview", description="Preview Enabled for Translation Objects")
	boolean isPreviewEnabled() default false;
	
	@AttributeDefinition(name = "Disable Psuedo L10n", description = "Disable Pseudo localization for Machine translations and use a simple Language prefix instead")
	boolean isPseudoLocalizationDisabled() default false;
}