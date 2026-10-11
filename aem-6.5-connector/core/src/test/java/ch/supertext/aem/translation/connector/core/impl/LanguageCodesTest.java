package ch.supertext.aem.translation.connector.core.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LanguageCodesTest {

    @Test
    public void bareSourceStaysBare() {
        assertEquals("en", LanguageCodes.source("en"));
    }

    @Test
    public void regionalSourceBecomesBare() {
        assertEquals("en", LanguageCodes.source("en-us"));
        assertEquals("en", LanguageCodes.source("en_GB"));
        assertEquals("de", LanguageCodes.source("DE_CH"));
        assertEquals("", LanguageCodes.source(""));
    }

    @Test
    public void bareTargetGetsDefaultRegion() {
        assertEquals("de-DE", LanguageCodes.target("de"));
        assertEquals("en-US", LanguageCodes.target("EN"));
        assertEquals("sl-SI", LanguageCodes.target("sl"));
    }

    @Test
    public void regionalTargetIsKeptAndNormalized() {
        assertEquals("de-CH", LanguageCodes.target("de_ch"));
        assertEquals("en-US", LanguageCodes.target("en-us"));
        assertEquals("zh-Hant", LanguageCodes.target("zh_hant"));
        assertEquals("es-419", LanguageCodes.target("es-419"));
    }

    @Test
    public void unknownBareCodeIsUnchanged() {
        assertEquals("rm", LanguageCodes.target("rm"));
    }
}
