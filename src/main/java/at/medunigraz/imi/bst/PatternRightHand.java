package at.medunigraz.imi.bst;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PatternRightHand implements Comparable<PatternRightHand>{


    public String relationship;

    public TopLevelConcept.SUBHIERARCHY range;


    public PatternRightHand(String relationship, TopLevelConcept.SUBHIERARCHY range) {
        this.relationship = relationship;
        this.range = range;
    }

    public int compareTo(PatternRightHand other){
        if (this.relationship.equals(other.relationship)) {
            return this.range.compareTo(other.range);
        }
        else {
            return this.relationship.compareTo(other.relationship);
        }
    }

    @Override
    public String toString() {
        return relationship + "-" + range;
    }

    @Override
    public boolean equals(Object toBeCompared) {
        if (!(toBeCompared instanceof PatternRightHand))
            return false;
        if (toBeCompared == this)
            return true;

        PatternRightHand PRHtoBeCompared = (PatternRightHand) toBeCompared;

        return new EqualsBuilder().
                append(this.relationship, PRHtoBeCompared.relationship).
                append(this.range, PRHtoBeCompared.range).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(this.relationship).
                append(this.range).
                toHashCode();
    }
}
