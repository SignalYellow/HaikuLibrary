package jp.signalyellow.haiku;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shohei on 15/10/24.
 *
 */
public class MorphologicalAnalysisByGooAPI {
    private String gooId;

    static final String GOO_URL = "https://labs.goo.ne.jp/api/morph";

    public MorphologicalAnalysisByGooAPI(String gooId) {
        this.gooId = gooId;
    }

    public List<Word> analyze(String sentence)throws IOException{
        if(sentence == null || sentence.equals("")){
            return new ArrayList<>();
        }

        return parseJson(post(removeInappropreateTexts(sentence)));
    }

    public List<Word> parseJson(String jsonText){
        List<Word> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonText);
            JSONArray jsonArray = jsonObject.getJSONArray("word_list");
            for(int i=0;i<jsonArray.length();i++){
                JSONArray subArray = jsonArray.getJSONArray(i);
                for(int j=0;j<subArray.length();j++){
                    JSONArray wordArray = subArray.getJSONArray(j);
                    Word w = new Word(wordArray.get(0).toString(),wordArray.get(2).toString(),wordArray.get(1).toString());
                    list.add(w);
                }
            }

            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String removeInappropreateTexts(String text){
        return removeURLText(removeNewLine(text));
    }

    public static String removeNewLine(String text){
        return text.replaceAll("\n","");
    }

    public static String removeURLText(String text){
        return text.replaceAll("https?://([\\w-]+\\.)+[\\w-]+(/[0-9a-zA-Z_\\.?%&=]*)*", "");
    }

    private String post(String sentence) throws IOException {

        URL url;

        url = new URL(GOO_URL);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        //ヘッダ
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStream os = connection.getOutputStream();

        //jsonの中身書く
        String postStr = "{\"app_id\": \"" + gooId + "\",\"sentence\": \"" + sentence + "\"}";


        PrintStream ps = new PrintStream(os);
        ps.print(postStr); //データをPOSTする
        ps.close();

        InputStream is = connection.getInputStream(); //結果を取得
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));


        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();


    }

}
