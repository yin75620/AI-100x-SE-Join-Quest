# How to runs
  - Build: mvn -q -DskipTests package
  - Launch UI: java -cp target/classes io.ordrex.chess.ui.ChineseChessUI
  -  or : JAVA_HOME=vendor/jdk-17 java -cp target/classes io.ordrex.chess.ui.ChineseChessUI
  - If you see “Unable to locate a Java Runtime”, install JDK 17 and ensure java -version
  works.