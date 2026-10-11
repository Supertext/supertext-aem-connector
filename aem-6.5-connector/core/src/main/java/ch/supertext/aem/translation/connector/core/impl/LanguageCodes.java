package ch.supertext.aem.translation.connector.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Turns AEM language codes ({@code de}, {@code de_ch}, {@code en-us}) into the codes Supertext accepts.
 *
 * <p>Source: the bare language ({@code en_us} → {@code en}). Target: a regional code
 * ({@code de_ch} → {@code de-CH}); a bare target gets a regional default ({@code de} →
 * {@code de-DE}), because Supertext rejects bare targets with {@code INVALID_LANGUAGE_PAIR}.
 * Unknown bare codes are sent unchanged.
 */
public final class LanguageCodes {

    /** Regional defaults for bare target languages (same table in every Supertext plugin). */
    public static final Map<String, String> DEFAULT_REGIONS;

    static {
        Map<String, String> m = new HashMap<String, String>();
        String[] pairs = { "ar-SA", "bg-BG", "cs-CZ", "da-DK", "de-DE", "el-GR", "en-US", "es-ES", "et-EE", "fi-FI",
                "fr-FR", "hr-HR", "hu-HU", "it-IT", "ja-JP", "ko-KR", "lt-LT", "lv-LV", "nb-NO", "nl-NL", "pl-PL",
                "pt-PT", "ro-RO", "ru-RU", "sk-SK", "sl-SI", "sr-RS", "sv-SE", "th-TH", "tr-TR", "uk-UA", "vi-VN",
                "zh-CN" };
        for (String pair : pairs) {
            m.put(pair.substring(0, 2), pair);
        }
        DEFAULT_REGIONS = Collections.unmodifiableMap(m);
    }

    private LanguageCodes() {
    }

    /** {@code de-ch} or {@code de_CH} → {@code de-CH}, {@code zh_hant} → {@code zh-Hant}, {@code es-419} stays. */
    public static String format(String tag) {
        List<String> out = new ArrayList<String>();
        String[] parts = (tag == null ? "" : tag.trim()).split("[-_]");
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }
            if (i == 0) {
                out.add(part.toLowerCase(Locale.ROOT));
            } else if (part.length() == 2) {
                out.add(part.toUpperCase(Locale.ROOT));
            } else if (part.matches("\\d+")) {
                out.add(part);
            } else {
                out.add(part.substring(0, 1).toUpperCase(Locale.ROOT) + part.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String part : out) {
            if (sb.length() > 0) {
                sb.append('-');
            }
            sb.append(part);
        }
        return sb.toString();
    }

    /** Source language: the bare language ({@code en_us} → {@code en}). */
    public static String source(String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return "";
        }
        return tag.trim().split("[-_]")[0].toLowerCase(Locale.ROOT);
    }

    /** Target language: a regional code ({@code de_ch} → {@code de-CH}, {@code de} → {@code de-DE}). */
    public static String target(String tag) {
        String code = format(tag);
        if (!code.isEmpty() && !code.contains("-")) {
            String regional = DEFAULT_REGIONS.get(code);
            return regional != null ? regional : code;
        }
        return code;
    }
}
