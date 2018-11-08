/*
 * Copyright 2011 Fabian Kessler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.langdetect.i18n;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public final class LdLocale {

    @NotNull
    private final String language;
    @NotNull
    private final Optional<String> script;
    @NotNull
    private final Optional<String> region;

    private LdLocale(@NotNull String language, @NotNull Optional<String> script, @NotNull Optional<String> region) {
        this.language = language;
        this.script = script;
        this.region = region;
    }

    /**
     * @param string The output of the toString() method.
     * @return either a new or possibly a cached (immutable) instance.
     */
    @NotNull
    public static LdLocale fromString(@NotNull String string) {
        if (string==null || string.isEmpty()) throw new IllegalArgumentException("At least a language is required!");

        String language = null;
        Optional<String> script = null;
        Optional<String> region = null;


        List<String> strings = Arrays.asList(string.split("-"));
        for (int i=0; i<strings.size(); i++) {
            String chunk = strings.get(i);
            if (i==0) {
                language = assignLang(chunk);
            } else {
                if (script == null && region == null && looksLikeScriptCode(chunk)) {
                    script = Optional.of(chunk);
                } else if (region==null && (looksLikeGeoCode3166_1(chunk) || looksLikeGeoCodeNumeric(chunk))) {
                    region = Optional.of(chunk);
                } else {
                    throw new IllegalArgumentException("Unknown part: >>>"+chunk+"<<<!");
                }
            }
        }
        assert language != null;
        if (script==null) script = Optional.absent();
        if (region==null) region = Optional.absent();
        return new LdLocale(language, script, region);
    }

    private static boolean looksLikeScriptCode(String string) {
        return string.length() == 4 && string.matches("[A-Z][a-z]{3}");
    }

    private static boolean looksLikeGeoCode3166_1(String string) {
        return string.length()==2 && string.matches("[A-Z]{2}");
    }
    private static boolean looksLikeGeoCodeNumeric(String string) {
        return string.length()==3 && string.matches("[0-9]{3}");
    }

    private static String assignLang(String s) {
        if (!s.matches("[a-z]{2,3}")) throw new IllegalArgumentException("Invalid language code syntax: >>>"+s+"<<<!");
        return s;
    }

    /**
     * The output of this can be fed to the fromString() method.
     * @return for example "de" or "de-Latn" or "de-CH" or "de-Latn-CH", see class header.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(language);

        if (script.isPresent()) {
            sb.append('-');
            sb.append(script.get());
        }

        if (region.isPresent()) {
            sb.append('-');
            sb.append(region.get());
        }

        return sb.toString();
    }


    /**
     * @return ISO 639-1 or 639-3 language code, eg "fr" or "gsw", see class header.
     */
    @NotNull
    public String getLanguage() {
        return language;
    }

    /**
     * @return ISO 15924 script code, eg "Latn", see class header.
     */
    @NotNull
    public Optional<String> getScript() {
        return script;
    }

    /**
     * @return ISO 3166-1 or UN M.49 code, eg "DE" or 150, see class header.
     */
    @NotNull
    public Optional<String> getRegion() {
        return region;
    }



    @Override //generated-code
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LdLocale ldLocale = (LdLocale) o;

        if (!language.equals(ldLocale.language)) return false;
        if (!region.equals(ldLocale.region)) return false;
        if (!script.equals(ldLocale.script)) return false;

        return true;
    }

    @Override //generated-code
    public int hashCode() {
        int result = language.hashCode();
        result = 31 * result + script.hashCode();
        result = 31 * result + region.hashCode();
        return result;
    }
}
