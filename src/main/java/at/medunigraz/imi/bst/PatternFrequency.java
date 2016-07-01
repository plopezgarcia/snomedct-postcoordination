package at.medunigraz.imi.bst;


public class PatternFrequency {

    public Pattern pattern;
    public int frequency;

    public PatternFrequency(Pattern pattern, int frequency){
        this.pattern = pattern;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Freq: " + frequency + " - Pattern: " + pattern.toString();
    }
}
