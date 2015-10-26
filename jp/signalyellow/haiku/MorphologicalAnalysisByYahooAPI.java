package jp.signalyellow.haiku;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shohei on 15/08/08.
 */
public class MorphologicalAnalysisByYahooAPI {

    static final String YAHOO_BASE_URL = "http://jlp.yahooapis.jp/MAService/V1/parse?appid=";
    static final String YAHOO_TAIL_URL = "&results=ma&sentence=";

    String mApplicationId;

    public MorphologicalAnalysisByYahooAPI(String yahooApplicationId){
        this.mApplicationId = yahooApplicationId;
    }

    public List<Word> analyze(String text) throws XmlPullParserException, IOException {
        return parseYahooXML(getYahooURL(removeURLText(text)));
    }

    public static String removeURLText(String text){
        return text.replaceAll("https?://([\\w-]+\\.)+[\\w-]+(/[0-9a-zA-Z_\\.?%&=]*)*", "");
    }

    /**
     * エンコードしないと上手くいかない
     * @param text 形態素解析をしたい文章
     * @return yahooAPIの形態素解析のurl
     */
    public String getYahooURL(String text) throws UnsupportedEncodingException {
        return YAHOO_BASE_URL + mApplicationId + YAHOO_TAIL_URL + URLEncoder.encode(text,"utf-8");
    }

    /**
     *
     * xmlのパースを行う
     * @param urlString:xmlが記述されたurl この場合はyahooのurl
     * @return xmlを解析しそれぞれの単語の形態素解析を含んだWordのリスト
     * @throws IOException
     * @throws XmlPullParserException
     */
    private List<Word> parseYahooXML(String urlString)
            throws IOException,XmlPullParserException {


        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        connection.setDoInput(true);
        InputStream stream = connection.getInputStream();


        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(stream,"UTF-8");


        List<Word> wordList = new ArrayList<Word>();

        int eventType;
        eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                if(parser.getName().equals("word")){
                    wordList.add(parseWordNode(parser));
                }
            }
            eventType = parser.next();
        }

        return wordList;
    }

    /**
     * parseYahooXMLで利用する
     * 単語単位のパース
     * @param parser ぱーさー
     * @return 一つの単語に対してのWordクラス
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Word parseWordNode(XmlPullParser parser) throws IOException, XmlPullParserException {
        String surface = null;
        String reading = null;
        String pos = null;

        parser.next(); parser.next();

        surface = parser.getText();

        parser.next(); parser.next(); parser.next();

        reading = parser.getText();

        parser.next(); parser.next(); parser.next();

        pos = parser.getText();

        return new Word(surface,reading,pos);
    }
}
