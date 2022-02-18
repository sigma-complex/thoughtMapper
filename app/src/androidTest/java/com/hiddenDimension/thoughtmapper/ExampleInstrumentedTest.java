package com.hiddenDimension.thoughtmapper;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
      //  Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
       // assertEquals("com.hiddenDimension.thoughtmapper", appContext.getPackageName());

        try {
            Document doc = Jsoup.connect("https://ja.wikipedia.org/wiki/%E6%99%82%E9%96%93").get();
            System.out.println(doc.getElementsByTag("p").get(0).text());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void divideJapneseWords(){
        String sen = "時間（じかん、羅: tempus 英: time）とは、出来事や変化を認識するための基礎的な概念である。芸術、哲学、自然科学、心理学などで重要なテーマとして扱われることもある。分野ごとに定義が異なる。";
        System.out.println(sen.split("[をがはから]"));
    }
}