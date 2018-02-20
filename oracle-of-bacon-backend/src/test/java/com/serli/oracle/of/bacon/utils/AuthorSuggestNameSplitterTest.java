package com.serli.oracle.of.bacon.utils;

        import java.util.List;

public class AuthorSuggestNameSplitterTest {
    public static void main(String[] args) {
        String[] test = AuthorSuggestNameSplitter.createSuggestArray("Robert, De Niro");
        for (String s : test) {
            System.out.println(s);
        }

        System.out.println("----- NANOU ----");
        test = AuthorSuggestNameSplitter.getAllCombinations("Robert, De Niro").toArray(new String[0]);
        for (String s : test) {
            System.out.println(s);
        }

    }
}
