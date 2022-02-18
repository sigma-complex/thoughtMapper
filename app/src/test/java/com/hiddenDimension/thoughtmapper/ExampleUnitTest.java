package com.hiddenDimension.thoughtmapper;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        String x = "やあ こんな 所(ところ) で 会う[01]{お会い} 為る(する)[12]{する} なんて[01] 偶然 です ね[01] 世の中 って 狭い 物(もの){もの} です ね[01]";
        Log.d("hhh", x.replaceAll("\\[(0-9)+\\]",""));
    }
}