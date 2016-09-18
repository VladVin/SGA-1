## How to use it
There are some examples that explain how to use Text Style Analyzer. For all of them you need to be in the root project directory.

### Training
`java -Dfile.encoding=UTF-8 -cp .\out\production\TextStyleAnalyzer\;.\lib\weka-3-9-0\weka.jar text_style_analyzer.TextStyleAnalyzer train --pos ..\data\train\Lermontov\Demon.txt --neg ..\data\train\Pushkin\Dubrovskij.txt`

### Testing
`java -Dfile.encoding=UTF-8 -cp .\out\production\TextStyleAnalyzer\;.\lib\weka-3-9-0\weka.jar text_style_analyzer.TextStyleAnalyzer test --docs ..\data\test\Lermontov\Korsar.txt`