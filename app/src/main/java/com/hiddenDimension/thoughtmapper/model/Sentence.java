package com.hiddenDimension.thoughtmapper.model;

public class Sentence {

    private String japaneseSentence;
    private String englishSentence;


    public String getJapaneseSentence() {
        return japaneseSentence;
    }

    public void setJapaneseSentence(String japaneseSentence) {
        this.japaneseSentence = japaneseSentence;
    }

    public String getEnglishSentence() {
        return englishSentence;
    }

    public void setEnglishSentence(String englishSentence) {
        this.englishSentence = englishSentence;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "japaneseSentence='" + japaneseSentence + '\'' +
                ", englishSentence='" + englishSentence + '\'' +
                '}';
    }
}
