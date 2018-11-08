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

package com.langdetect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.langdetect.LanguageDetector;
import com.langdetect.LanguageDetectorBuilder;
import com.langdetect.ngram.NgramExtractors;
import com.langdetect.profiles.LanguageProfile;
import com.langdetect.profiles.LanguageProfileReader;
import com.langdetect.text.CommonTextObjectFactories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataLanguageDetectorImplTest {

    private final LanguageDetector shortDetector;
    private final LanguageDetector longDetector;


    private static final Logger log = LoggerFactory.getLogger(DataLanguageDetectorImplTest.class);

    public DataLanguageDetectorImplTest() throws IOException {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        shortDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(100)
                .withProfiles(languageProfiles)
                .build();

        longDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .shortTextAlgorithm(0)
                .withProfiles(new LanguageProfileReader().readAllBuiltIn())
                .build();
    }

    @Test(dataProvider = "shortCleanTexts")
    public void shortTextAlgo(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(shortDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        //the detect() method doesn't have enough confidence for all these short texts.
    }

    @Test(dataProvider = "shortCleanTexts")
    public void longTextAlgoWorkingOnShortText(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        //the detect() method doesn't have enough confidence for all these short texts.
    }

    @Test(dataProvider = "longerWikipediaTexts")
    public void longTextAlgoWorkingOnLongText(String expectedLanguage, CharSequence text) throws IOException {
        assertEquals(longDetector.getProbabilities(text).get(0).getLocale().getLanguage(), expectedLanguage);
        assertEquals(longDetector.detect(text).get().getLanguage(), expectedLanguage);

//        System.out.println("testing text: "+ text.subSequence(0,100));
//        System.out.println("found language: "+longDetector.detect(text).get().getLanguage());
        System.out.println("probabilities: "+longDetector.getProbabilities(text).toString());

        System.out.println("1probabilities: "+longDetector.getProbabilities("BBC").toString());
    }

    @DataProvider
    protected Object[][] shortCleanTexts() {
        return new Object[][] {
                {"en", shortCleanText("This is some English text.")},
                {"fr", shortCleanText("Ceci est un texte français.")},
                {"nl", shortCleanText("Dit is een Nederlandse tekst.")},
                {"de", shortCleanText("Dies ist eine deutsche Text")},
                {"km", shortCleanText("សព្វវចនាធិប្បាយសេរីសម្រាប់អ្នកទាំងអស់គ្នា។" + "នៅក្នុងវិគីភីឌាភាសាខ្មែរឥឡូវនេះមាន ១១៩៨រូបភាព សមាជិក១៥៣៣៣នាក់ និងមាន៤៥៨៣អត្ថបទ។")},
                {"bg", shortCleanText("Европа не трябва да стартира нов конкурентен маратон и изход с приватизация")},
                {"wa", shortCleanText("Çouchal c' est on tecse pår e walon.")},
        };
    }
    private CharSequence shortCleanText(CharSequence text) {
        return CommonTextObjectFactories.forDetectingShortCleanText().forText( text );
    }

    @DataProvider
    protected Object[][] longerWikipediaTexts() {
        return new Object[][] {
                {"de", largeText(readText("/texts/de-wikipedia-Deutschland.txt"))},
                {"fr", largeText(readText("/texts/fr-wikipedia-France.txt"))},
                {"it", largeText(readText("/texts/it-wikipedia-Italia.txt"))},
        };
    }

    private CharSequence readText(String path) {
        try (InputStream inputStream = DataLanguageDetectorImplTest.class.getResourceAsStream(path)) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String str;
                while ((str = in.readLine()) != null) {
                    sb.append(str);
                }
                return sb.toString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CharSequence largeText(CharSequence text) {
        return CommonTextObjectFactories.forDetectingOnLargeText().forText( text );
    }




}
