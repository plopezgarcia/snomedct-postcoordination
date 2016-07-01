package at.medunigraz.imi.bst;


import au.com.bytecode.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternFrequency {

    public static final String PATTERNS_FILE = "src/main/resources/data/pattern-frequencies.csv";

    public ConceptPattern pattern;
    public int frequency;

    public PatternFrequency(ConceptPattern pattern, int frequency){
        this.pattern = pattern;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Freq: " + frequency + " - ConceptPattern: " + pattern.toString();
    }
}
