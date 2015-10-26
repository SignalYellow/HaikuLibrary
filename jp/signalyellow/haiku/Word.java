package jp.signalyellow.haiku;

/**
 * Created by shohei on 15/08/08.
 */
public class Word {
    String surface;
    String reading;
    String pos;

    public Word(String surface, String reading, String pos){
        this.surface = surface;
        this.reading = reading;
        this.pos = pos;
    }

    public int getReadingLength(){

        int ret = 0;

        for(char c: this.reading.toCharArray()){
            switch (c){
                case 'ょ':
                case 'ゃ':
                case 'ゅ':
                case 'ぁ':
                case 'ぃ':
                case 'ぇ':
                case 'ぉ':
                case 'ャ':
                case 'ュ':
                case 'ョ':
                case 'ァ':
                case 'ィ':
                case 'ェ':
                case 'ォ':

                    continue;
                default:
                    break;
            }

            ret++;
        }

        return ret;
    }

    @Override
    public String toString() {
        return "surface:" + this.surface +
                " reading:" + this.reading +
                " pos:" + this.pos;
    }

    public String getSurface() {
        return surface;
    }

    public String getReading() {
        return reading;
    }

    public String getPos() {
        return pos;
    }
}
