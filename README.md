# Wmn4j late-breaking-demo for ISMIR 2022

The purpose of the code in this repository is to demonstrate how
[wmn4j](https://github.com/otsob/wmn4j) can be used to run analyses and searches on a score in parallel.
All code can be found in the [Main file](./src/main/java/org/wmn4j/ismir/demo/Main.java).

## How to run the code

1. You need to have Java 17 and [gradle](https://gradle.org) available. The Gradle wrapper contained in the repository
   should take care of installing the correct version of gradle.
2. Download a suitable score in MusicXML. This has been tested with
   the [4th movement](https://musescore.com/user/158781/scores/5120953) of Beethoven's Symphony nr. 9.
3. The easiest way to run the code and experiment with it, is to import this project to
   your IDE. Otherwise follow steps 4. and 5.
4. The project can be built with `./gradlew build`. An executable is packaged in
   the `build/distributions/wmn4j-ismir-demo-0.1.0.zip` file.
5. Unzip the packaged application zip, e.g., with `unzip build/distributions/wmn4j-ismir-demo-0.1.0.zip`. To run the
   executable, run `./wmn4j-ismir-demo-0.1.0/bin/wmn4j-ismir-demo <path-to-musicxml-file>`.
