package com.superscores.android.common.utils;

import android.content.Context;
import android.util.Xml;
import android.webkit.WebView;

public class WebViewUtils {

    public static String createHeader(String style) {
        return "<!DOCTYPE html><html><head><meta charset='utf-8' /><style type='text/css'>" +
                (style == null ? "" : style) + "</style></head><body>";
    }

    public static String createFooter() {
        return "</body></html>";
    }

    public static String getDefaultMimeType() {
        return "text/html; charset=utf-8";
    }

    public static String getDefaultEncoding() {
        return "UTF-8";
    }

    public static String getDefaultBaseUrl() {
        return "file:///android_asset/";
    }

    public static String createFontFace(String fontName, String assetPath, String fontType,
            String fontWeight) {
        return "@font-face {font-family: '" + fontName + "'; src: url('" + assetPath + "') format" +
                "('" + (fontType == null ? "truetype" : fontType) + "'); font-weight: " +
                (fontWeight == null ? "normal" : fontWeight) + ";}";
    }

    public static void loadCustomWebView(Context context, WebView webView, CharSequence text) {
        // int padding =
        // context.getResources().getDimensionPixelSize(R.dimen.padding_normal);

        String mimeType = "text/html";

        String htmlCss = "<head><style type='text/css'>@font-face {font-family: 'MyFont'; " +
                "src: url('file:///android_asset/fonts/Roboto-Light.ttf'); } " + "body " +
                "{font-family: MyFont; text-align: justify;}</style></head>";

        String htmlText = "<html>" + htmlCss + "<body>" + text + "</body></html>";

        webView.setBackgroundColor(0x00000000);
        webView.loadData(htmlText, mimeType, Xml.Encoding.UTF_8.toString());
    }
}