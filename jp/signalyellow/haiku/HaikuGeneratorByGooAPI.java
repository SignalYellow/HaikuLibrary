package jp.signalyellow.haiku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shohei on 15/10/26.
 */
public class HaikuGeneratorByGooAPI {


    final String SYMBOL = "Symbol";
    final String UNDEF = "Undef";
    final String BRACE = "括弧";
    final String DOT = "点";
    final String SPACE = "空白";
    final String VERB = "動詞";
    final String VERB_STRICT = "動詞語幹";
    final String NOUN = "名詞";
    final String ADJECTIVE = "形容詞";
    final String ADJECTIVE_STRICT = "形容詞語幹";
    final String PARTICLE = "助詞";
    final String JUDGEMENT = "判定詞";
    final String SLASH = "MonthDay";


    List<Word> wordList;

    List<String> mPhrase5List = new ArrayList<String>();
    List<String> mPhrase7List = new ArrayList<String>();
    List<String> mPhrase5List_last;

    static final String CANT_GENERATE_MESSAGE = "できませんでした";

    public HaikuGeneratorByGooAPI(List<Word> words){
        this.wordList = words;
        findAllphrases();
    }

    /**
     * wordListの中で俳句に使えそうなものを探し出しlistに保存する
     */
    private void findAllphrases(){
        if(wordList == null || wordList.size() < 2){
            return;
        }

        for(int i=0;i<wordList.size();i++){
            Word word = wordList.get(i);
            if(!canBeFirstWord(word)){
                continue;
            }
            findWord("", i, 0);
        }
    }

    /**
     * 再帰的な処理
     * lengthで5か7の判定を行う
     * @param s
     * @param index
     * @param length
     */
    private void findWord(String s , int index, int length){

        if(index >= wordList.size()){
            return;
        }
        Word w  = wordList.get(index);
        if(isSkipWord(w)){
            findWord(s, index + 1, length);
            return;

        }

        if(length == 5){
            mPhrase5List.add(s);}

        if(length == 7){
            mPhrase7List.add(s);
            return;}

        if(length >= 7){
            return;}



        length += w.getReadingLength();

        if(!canBeWord(w)){
            return;
        }

        findWord(s + w.getSurface(), index + 1, length);
    }


    public String generate(){

        if(!canGenerate()){
            return CANT_GENERATE_MESSAGE;
        }

        int first5=0;
        int second7=0;
        int last5=0;

        int size5 = mPhrase5List.size();
        int size7 = mPhrase7List.size();

        Random r = new Random();

        if(size7 > 1){
            second7 = r.nextInt(size7);
        }

        if(size5 > 1){
            first5 = r.nextInt(size5);
            last5 = r.nextInt(size5);
            while(first5 == last5){
                last5 = r.nextInt(size5);
            }
        }

        try{
            return connectWordsToHaiku(mPhrase5List.get(first5),mPhrase7List.get(second7),mPhrase5List.get(last5));
        }catch (Exception e){
            return connectWordsToHaiku(mPhrase5List.get(0),mPhrase7List.get(0),mPhrase5List.get(0));
        }
    }

    /**
     *
     * @param first5
     * @param second7
     * @param last5
     * @return 俳句の文章の感じになるように5/7/5をつなげた文章を返す
     */
    private String connectWordsToHaiku(String first5, String second7, String last5){
        StringBuilder sb = new StringBuilder();
        sb.append(first5).append(" 　").append(second7).append(" 　").append(last5);

        return  sb.toString();
    }


    /**
     * @param w
     * @return 単語が俳句の先頭に来ても良いかどうか
     */
    private boolean canBeFirstWord(Word w){
        String pos = w.pos;

        if(!canBeWord(w) || pos.contains(DOT) || pos.contains(PARTICLE)
                 || pos.contains(BRACE) || pos.contains(SLASH) || pos.contains(JUDGEMENT)){
            return false;
        }

        return true;
    }

    /**
     * @param w　単語
     * @return 単語が俳句に使用できるものかどうか
     */
    private boolean canBeWord(Word w){
        String pos = w.pos;
        if(pos.contains(SPACE) || pos.contains(SYMBOL) || pos.contains(UNDEF)){
            return false;
        }

        return true;
    }


    private boolean isSkipWord(Word w){
        String pos = w.pos;
        if(pos.contains(BRACE) || pos.contains(DOT) ){
            return true;
        }
            return false;
    }

    /**
     *
     * @return 俳句を生成できそうかどうか
     */
    public boolean canGenerate(){
        return mPhrase5List.size() > 0 && mPhrase7List.size() > 0;
    }



}
