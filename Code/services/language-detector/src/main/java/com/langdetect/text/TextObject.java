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

package com.langdetect.text;

import com.google.common.annotations.Beta;
import com.langdetect.LanguageDetector;
import com.langdetect.cybozu.util.CharNormalizer;
import com.langdetect.profiles.LanguageProfile;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

@Beta
public class TextObject implements CharSequence, Appendable {

    @NotNull
    private final TextFilter textFilter;

    @NotNull
    private final StringBuilder stringBuilder;

    private final int maxTextLength;


    /**
     * @param maxTextLength 0 for no limit
     */
    public TextObject(@NotNull TextFilter textFilter, int maxTextLength) {
        this.textFilter = textFilter;
        this.maxTextLength = maxTextLength;
        this.stringBuilder = new StringBuilder();
    }


    /**
     * Append the target text for language detection.
     * This method read the text from specified input reader.
     * If the total size of target text exceeds the limit size,
     * the rest is ignored.
     *
     * @param reader the input reader (BufferedReader as usual)
     * @throws java.io.IOException Can't read the reader.
     */
    public TextObject append(Reader reader) throws IOException {
        char[] buf = new char[1024];
        while (reader.ready() && (maxTextLength==0 || stringBuilder.length()<maxTextLength)) {
            int length = reader.read(buf);
            append(String.valueOf(buf, 0, length));
        }
        return this;
    }

    /**
     * Append the target text for language detection.
     * If the total size of target text exceeds the limit size ,
     * the rest is cut down.
     *
     * @param text the target text to append
     */
    @Override
    public TextObject append(CharSequence text) {
        if (maxTextLength>0 && stringBuilder.length()>=maxTextLength) return this;

        text = textFilter.filter(text);

        //unfortunately this code can't be put into a TextFilter because:
        //1) the limit could not be detected early, a lot of work would be done to waste time and memory
        //2) the last character of the existing string builder could not be seen. if it is a space, we don't want
        //   to add yet another space.
        char pre = stringBuilder.length()==0 ? 0 : stringBuilder.charAt(stringBuilder.length()-1);
        for (int i=0; i<text.length() && (maxTextLength==0 || stringBuilder.length()<maxTextLength); i++) {
            char c = CharNormalizer.normalize(text.charAt(i));
            if (c != ' ' || pre != ' ') {
                stringBuilder.append(c);
            }
            pre = c;
        }

        return this;
    }

    @Override
    public Appendable append(CharSequence csq, int start, int end) throws IOException {
        return append(csq.subSequence(start, end));
    }

    @Override
    public Appendable append(char c) throws IOException {
        return append(Character.toString(c));
    }



    @Override
    public int length() {
        return stringBuilder.length();
    }

    @Override
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }

    @Override @NotNull
    public String toString() {
        return stringBuilder.toString(); //only correct impl, see interface CharSequence!
    }

}
