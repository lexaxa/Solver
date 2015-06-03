package ru.solver;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static String getFirstFound(String contents, String regex) {
        List<String> founds = getFound(contents, regex);
        if (isEmpty(founds)) {
            return null;
        }
        return founds.get(0);
    }
    public static List<String> getFound(String contents, String regex) {
        if (isEmpty(regex) || isEmpty(contents)) {
            return null;
        }
        List<String> results = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(contents);

        while (matcher.find()) {
            if (matcher.groupCount() > 0) {
                results.add(matcher.group(1));
            } else {
                results.add(matcher.group());
            }
        }
        return results;
    }
    public static boolean isEmpty(List<String> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        if (list.size() == 1 && isEmpty(list.get(0))) {
            return true;
        }
        return false;
    }
    public static boolean isEmpty(String str) {
        if (str != null && str.trim().length() > 0) {
            return false;
        }
        return true;
    }
}
