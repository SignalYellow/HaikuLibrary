package jp.signalyellow.haiku;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shohei on 15/10/26.
 */
public class HaikuGeneratorByGooAPI {


    //first
    final String NOUN = "名詞";
    final String CROWN = "冠";
    final String STEM = "語幹";
    final String RENTAISHI = "連体詞";
    final String RENYOSHI = "連用詞";
    final String DOKURITSUSHI = "独立詞";
    final String CONJUNCTION = "接続詞";
    final String YEAR = "Year";
    final String DAY = "Day";
    final String HOUR = "Hour";
    final String MINUTE = "Minute";
    final String SECOND ="Second";
    final String PREHOUR = "PreHour";
    final String POSTHOUR = "PostHour";
    final String NUMBER = "Number";
    final String ROMAN = "Roman";

    final String[] FirstPosList = {NOUN, CROWN, STEM, RENTAISHI, RENYOSHI, DOKURITSUSHI,
    CONJUNCTION,PREHOUR,POSTHOUR,NUMBER,ROMAN,YEAR,DAY,HOUR,MINUTE,SECOND};


    //last
    final String TAIL = "接尾辞";
    final String SHUJOSHI = "終助詞";
    final String KANTOSHI = "間投詞";
    final String JYOSUSHI = "助数詞";
    final String JYOJYOSUSHI = "助助数詞";
    final String JUDGEMENT = "判定詞";

    final String[] LastPosList = {TAIL,SHUJOSHI,KANTOSHI,JYOSUSHI,JYOJYOSUSHI,JUDGEMENT
    ,NOUN};

    //other
    final String PARTICLE = "助詞";

    //skip
    final String SLASH = "MonthDay";
    final String DOT = "点";
    final String SPACE = "空白";
    final String SYMBOL = "Symbol";
    final String UNDEF = "Undef";
    final String BRACE = "括弧";
    final String HOURMINUTE = "HourMinute";
    final String MINUTESECOND = "MunuteSecond";
    final String YEARMONTH = "YearMonth";

    List<Word> wordList;



    List<String> mPhrase5All = new ArrayList<>();
    List<String> mPhrase7All = new ArrayList<>();

    List<String> mBestPhrase5 = new ArrayList<>();
    List<String> mBestPhrase6 = new ArrayList<>();
    List<String> mBestPhrase7 = new ArrayList<>();

    List<String> mGoodPhrase5 = new ArrayList<>();
    List<String> mGoodPhrase7 = new ArrayList<>();

    List<String> mPhrase5 = new ArrayList<>();
    List<String> mPhrase7 = new ArrayList<>();



    static final String CANT_GENERATE_MESSAGE = "できませんでした";

    public HaikuGeneratorByGooAPI(List<Word> words){
        this.wordList = words;
        prepare();
    }

    /**
     *
     * @return 俳句を生成できそうかどうか
     */
    public boolean canGenerateHaiku(){
        return (mBestPhrase5.size() + mGoodPhrase5.size() + mPhrase5.size())> 0 &&
                (mBestPhrase7.size() + mGoodPhrase7.size() + mPhrase7.size()) > 0;
    }



    public String generateHaikuStrictly(){
        if(!canGenerateHaiku()){
            return CANT_GENERATE_MESSAGE;
        }

        String first5;
        String second7;
        String last5;

        Random r = new Random();

        if(mBestPhrase7.size() > 0){
            second7 = mBestPhrase7.get(r.nextInt(mBestPhrase7.size()));
        }else if(mGoodPhrase7.size() > 0){
            second7 = mGoodPhrase7.get(r.nextInt(mGoodPhrase7.size()));
        }else if(mPhrase7.size() > 0){
            second7 = mPhrase7.get(r.nextInt(mPhrase7.size()));
        }else{
            return CANT_GENERATE_MESSAGE;
        }

        if(mBestPhrase5.size() > 1){
            int index = r.nextInt(mBestPhrase5.size());
            first5 = mBestPhrase5.get(index);
            int t = index;
            while(t == index){
                t = r.nextInt(mBestPhrase5.size());
            }
            last5 = mBestPhrase5.get(t);
        }else if(mBestPhrase5.size() == 1){
            first5 = mBestPhrase5.get(0);

            if(mGoodPhrase5.size() > 0){
                last5 = mGoodPhrase5.get(r.nextInt(mGoodPhrase5.size()));
            }else if(mPhrase5.size()>0){
                last5 = mPhrase5.get(r.nextInt(mPhrase5.size()));
            }else{
                last5 = mBestPhrase5.get(0);
            }
        }else if(mGoodPhrase5.size() > 1){
            int index = r.nextInt(mGoodPhrase5.size());
            first5 = mGoodPhrase5.get(index);
            int t = index;
            while(t == index){
                t = r.nextInt(mGoodPhrase5.size());
            }
            last5 = mGoodPhrase5.get(t);
        }else if(mGoodPhrase5.size() == 1){
            first5 = mGoodPhrase5.get(0);

            if(mPhrase5.size() > 0){
                last5 = mPhrase5.get(r.nextInt(mPhrase5.size()));
            }else{
                last5 = mGoodPhrase5.get(0);
            }
        }else if(mPhrase5.size() >1){
            int index = r.nextInt(mPhrase5.size());
            first5 = mPhrase5.get(index);
            int t = index;
            while(t == index){
                t = r.nextInt(mPhrase5.size());
            }
            last5 = mPhrase5.get(t);

        }else{
            first5 = last5 = mPhrase5.get(0);
        }

        return connectWordsToHaiku(first5,second7,last5);
    }

    public String generate(){

        if(!canGenerateHaiku() ){
            return CANT_GENERATE_MESSAGE;
        }

        if(mPhrase5All.size() ==0 || mPhrase7All.size() == 0){
            mPhrase7All.addAll(mBestPhrase7);
            mPhrase7All.addAll(mGoodPhrase7);
            mPhrase7All.addAll(mPhrase7);

            mPhrase5All.addAll(mBestPhrase5);
            mPhrase5All.addAll(mGoodPhrase5);
            mPhrase5All.addAll(mPhrase5);
        }

        int first5=0;
        int second7=0;
        int last5=0;

        int size5 = mPhrase5All.size();
        int size7 = mPhrase7All.size();

        Random r = new Random();

        second7 = r.nextInt(size7);

        if(size5 > 1) {
            first5 = r.nextInt(size5);
            last5 = r.nextInt(size5);
            while (first5 == last5) {
                last5 = r.nextInt(size5);
            }
        }

        return connectWordsToHaiku(mPhrase5All.get(first5),mPhrase7All.get(second7),mPhrase5All.get(last5));
    }


    private void prepare(){
        if(wordList == null || wordList.size() < 2){
            return;
        }

        for(int i=0;i<wordList.size();i++){
            Word word = wordList.get(i);
            if(!canBeFirstWord(word)){
                continue;
            }

            if(isGoodFirst(word)){
                findPhrase(new StringBuilder(),i,0,true);
                continue;
            }
            findPhrase(new StringBuilder(),i,0,false);
        }
    }

    private void findPhrase(StringBuilder builder,int index,int length, boolean isNiceFirst){
        if(index >= wordList.size()){
            return;
        }
        Word w = wordList.get(index);

        if(!canBeWord(w)){
            return;
        }

        if(isSkipWord(w)){
            findPhrase(builder,index+1,length,isNiceFirst);
            return;
        }

        length += w.getReadingLength();
        builder.append(w.getSurface());

        if(length > 7){
            return;
        }

        if(length == 7){
            if(isNiceFirst){
                if(isGoodLast(w)){
                    mBestPhrase7.add(builder.toString());
                    return;
                }
                mGoodPhrase7.add(builder.toString());
                return;
            }
            mPhrase7.add(builder.toString());
            return;
        }

        if (length == 6){
            if (isNiceFirst && isGoodLast(w)){
                mBestPhrase6.add(builder.toString());
            }
        }

        if(length == 5){
            if(isNiceFirst){
                if(isGoodLast(w)){
                    mBestPhrase5.add(builder.toString());
                }else {
                    mGoodPhrase5.add(builder.toString());
                }
            }else {
                mPhrase5.add(builder.toString());
            }
        }

        findPhrase(builder,index+1,length,isNiceFirst);
    }

    /**
     * 先頭の語として良いものかどうか判定
     * @param word
     * @return
     */
    private boolean isGoodFirst(Word word){
        String pos = word.pos;

        if(pos.contains(TAIL)){
            return false;
        }

        for (String s: FirstPosList){
            if(pos.contains(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * 最後の語として良いかどうか
     * @param word
     * @return
     */
    private boolean isGoodLast(Word word){
        String pos = word.pos;
        for (String s:LastPosList) {
            if (pos.contains(s)) {
                return true;
            }
        }
        return false;
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
     * @param first5 最初の５文字
     * @param second7 真ん中
     * @param last5 最後の５文字
     * @return 俳句の文章の感じになるように5/7/5をつなげた文章を返す
     */
    private String connectWordsToHaiku(String first5, String second7, String last5){
        return first5 + "   " + second7 + "   " + last5;
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



}
