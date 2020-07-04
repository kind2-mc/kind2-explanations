# Kind2-Explanations

## Build instructions

```shell script
$ java -version
 java -version
 openjdk version "11.0.7" 2020-04-14
 OpenJDK Runtime Environment (build 11.0.7+10-post-Ubuntu-2ubuntu218.04)
 OpenJDK 64-Bit Server VM (build 11.0.7+10-post-Ubuntu-2ubuntu218.04, mixed mode, sharing)

 $ git clone https://github.com/kind2-mc/kind2-explanations
 $ cd kind2-explanations     
 $ ./gradlew build 
 $ java -jar build/libs/kind2-explanations-all.jar files/a1.json
```
To import the API, use the jar file `build/libs/kind2-explanations.jar`. 

## API usage

1. Add `build/libs/kind2-expalnations.jar` to your java class path.
2. Import package `edu.uiowa.kind2`;
3. Pass kind2 json output as a string to `Kind2Result.analyzeJsonResult`. For example 
```java
String json = new String(Files.readAllBytes(Paths.get("file.json")));
Kind2Result result = Kind2Result.analyzeJsonResult(json);
System.out.println(result.toString());
```
4. 