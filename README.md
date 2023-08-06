# Interpals View Booster

Interpals View Booster is an easy-to-use GUI application to increase your page views on Interpals. 
This boost is achieved by visiting pages that satisfy the user query. 

This application uses primitive script indeed that send continuous http requests. Thus, account may be subject to temporal suspension due to requests of high frequency.

# Usage

1. Make sure you have [Java SE 20](https://www.oracle.com/java/technologies/downloads/) (or higher) installed and classpath is set correctly. Download [JavaFX 20](https://jdk.java.net/javafx20/) and set path to its lib folder: 
```bash
    #windows
    SETX JavaFX "<path to JavaFX SDK>/lib"
    #ubuntu
    export JavaFX=<path to JavaFX SDK>/lib
```
3. Download jar archive [here](https://disk.yandex.ru/d/fiecQFyI7yB16w) or compile this project on your machine. 
4. Run the following command in your Terminal under the app directory:
```bash
    java -jar --module-path $JavaFX --add-modules javafx.controls,javafx.fxml interpalsviewbooster.jar
```
4. Follow the GUI.

*Windows users can download these [files](https://disk.yandex.ru/d/DFQmIA8FIBCREQ) and simply start executable InterpalsViewBooster.exe (JAVA SE 20 or higher required)*





