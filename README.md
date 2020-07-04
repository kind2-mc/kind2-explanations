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
3. Pass kind2 json output as a string to `Kind2Result.analyzeJsonResult`. For example: 
```java
String json = new String(Files.readAllBytes(Paths.get("file.json")));
Kind2Result result = Kind2Result.analyzeJsonResult(json);
System.out.println(result.toString());
```
4. `Kind2Result` features:
    - `getValidProperties`, `getFalsifiedProperties`, and `getUnknownProperties` return properties for all components. 
    - `getNodeResult` returns an object of `Kind2NodeResult` that summarizes all analyses done by kind2 for a 
    given component. 
    
5. `Kind2NodeResult` features:
    - `getSuggestions` returns an object of `Kind2Suggestion` that provides explanations and suggestions 
    based on kind2 analyses for the current component.
    - `getValidProperties`, `getFalsifiedProperties`, and `getUnknownProperties` return properties for the current 
    component, and all its subcomponents. 
   
6. `Kind2Suggestion` contains explanations and a suggestion for the associated component. If `N` is the current 
    component, and `M` is possibly a subcomponent of `N`, then the suggestion is one of the following:
    
    * `noActionRequired`: no action required because all components of the system satisfy their contracts, and no 
    component of the system was refined.
    - `strengthenSubComponentContract`: fix `M`s contract because `N` is correct after refinement, but `M`'s contract 
    is too weak to prove `N`'s contract, but `M`'s definition is strong enough.
    - `completeSpecificationOrRemoveComponent`: Either complete specification of `N`'s contract, or remove 
    component `M`, because component `N` satisfies its current contract and one or more assumptions of `M` are 
    not satisfied by `N`.
    - `makeWeakerOrFixDefinition`: either make assumption `A` weaker, or fix `N`'s definition to satisfy `A`, because
    component `N` doesn't satisfy its contract after refinement, and assumption `A` of `M` is not satisfied by `N`.
    - `makeAssumptionStrongerOrFixDefinition`: Either make `N`'s assumptions stronger, or fix `N`'s definition to 
       satisfy `N`'s guarantees, because component `N` doesn't satisfy its contract after refinement, and 
       either `N` has no subcomponents, or all its subcomponents satisfy their contract.
    - `fixSubComponentIssues`: fix reported issues for `N`'s subcomponents, because component `N` doesn't satisfy its 
    contract after refinement, and One or more subcomponents of N don't satisfy their contract.
    - `fixOneModeActive`: define all modes of component `N`, because kind2 found a state that is not covered by any 
    of the modes in `N`'s contract. 
    - `increaseTimeout`: increase the timeout for kind2, because it fails to prove or disprove one of the properties. 