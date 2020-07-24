/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accommodation.system.uitls;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author namtv19
 */
public class Highlighter {

    private final String regex;
    Pattern pat;
    Matcher mat;
    private String preHighlight = "<span style=\"color:#526AE7\">";
    private String postHighlight = "</span>";

    /**
     * @param searchString
     */
    public Highlighter(String searchString) {
        regex = buildRegexFromQuery(searchString);
        pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    /**
     * @param queryStrings
     */
    public Highlighter(Iterable<String> queryStrings) {
        regex = buildRegexFromQuery(queryStrings);
        pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    /**
     * @param text
     * @return
     */
    public String getHighlighted(String text) {
        return buildElementForText(text, null);
    }

    public String getHighlighted(String text, Set<String> tags) {
        return buildElementForText(text, tags);
    }

    private static String buildRegexFromQuery(String queryString) {
        String regex = "";
        String queryToConvert = queryString;

        queryToConvert = queryToConvert.replaceAll("[ \t\r\n]+", " ").toLowerCase();
        regex = "(";

//        regex += "(\\b)" + queryToConvert + "(\\b)|";
        regex += "(\\b)" + queryToConvert + "[a-zA-Z0-9]*?(\\b))";
        return regex;
    }

    private static String buildRegexFromQuery(Iterable<String> queryStrings) {
        if (!queryStrings.iterator().hasNext()) {
            return "";
        }
        StringBuffer regex = new StringBuffer();
        regex.append("(");
        int count = 0;
        String queryToConvert = "";
        for (String s : queryStrings) {
            count++;
            queryToConvert = s;
        }
        count--;

        for (String s : queryStrings) {
            if (count <= 0) {
                continue;
            }
            count--;
            s = s.replaceAll("[ \t\r\n]+", " ").toLowerCase();
            if (s.isEmpty()) {
                continue;
            }
            regex.append("(\\b)").append(s).append("(\\b)|");
        }

        queryToConvert = queryToConvert.replaceAll("[ \t\r\n]+", " ").toLowerCase();
        regex.append("(\\b)").append(queryToConvert).append("[a-zA-Z0-9]*?(\\b))");
        return regex.toString();
    }

    private String buildElementForText(String text, Set<String> tags) {

        if (regex.isEmpty()) {
            return text;
        }

//        text = text.replaceAll("[ \t]+", " ");
        ArrayList<MatchedWord> matchedWordSet = new ArrayList<>();

        mat = pat.matcher(text.toLowerCase());

        while (mat.find()) {
            matchedWordSet.add(new MatchedWord(mat.start(), mat.end()));
        }

        StringBuffer newText = new StringBuffer(text);

//        Set<String> strings = new HashSet<>();
        for (int i = matchedWordSet.size() - 1; i >= 0; i--) {
            String wordToReplace = newText.substring(matchedWordSet.get(i).start, matchedWordSet.get(i).end);
            if (tags != null) tags.add(wordToReplace.trim().toLowerCase());
            wordToReplace = preHighlight + wordToReplace + postHighlight;
            newText = newText.replace(matchedWordSet.get(i).start, matchedWordSet.get(i).end, wordToReplace);
        }

        return newText.toString();
    }

    public static class MatchedWord {

        public int start;
        public int end;

        public MatchedWord(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    public String getPreHighlight() {
        return preHighlight;
    }

    public void setPreHighlight(String preHighlight) {
        this.preHighlight = preHighlight;
    }

    public String getPostHighlight() {
        return postHighlight;
    }

    public void setPostHighlight(String postHighlight) {
        this.postHighlight = postHighlight;
    }

}
