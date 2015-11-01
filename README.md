# HaikuLibrary
HaikuLibraryは文章から俳句を作るクラスを提供します。形態素解析には[Yahoo](http://developer.yahoo.co.jp/webapi/jlp/ma/v1/parse.html)や[gooラボ](https://labs.goo.ne.jp/api/2015/334/)の提供するAPIを利用しています。
形態素解析の実行には各々のサービスを利用するためのIDが必要となるので各サイトから取得をお願いします。

## Example
```sample
MorphologicalAnalysisByGooAPI analyzer = new MorphologicalAnalysisByGooAPI("配布されるコードを入力");
String text = "~~~~文章";

List<Word> list = analyzer.analyze(text); //形態素解析
String haiku = new HaikuGeneratorByGooAPI(list).generate(); //俳句を出力

```

## 注意
HaikuGeneratorByGooAPIのコンストラクタの引数にはMorphologicalAnalysisByGooAPIでの解析結果をお使いください。
HaikuGeneratorByYahooAPIはMorphologicalAnalysisByYahooAPI。
