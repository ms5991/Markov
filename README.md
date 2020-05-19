# Markov

Simple program to generate text using a Markov chain an configurable depth N-Grams.

## Usage

Complile and run:

    cd src\main\java
    javac *.java
    java Main generate 200 3 1 .\data\input.txt
    java Main dump 200 3 1 .\data\input.txt

The arguments on the command line are:

    [generate|dump] - either generate text or dump the probabilities
    [number of words to generate]
    [n-gram order]
    [number of input files]
    [file path 1]
    [file path 2]
    ...

The number of file paths supplied must equal the parameter passed in indicating
how many files there are.