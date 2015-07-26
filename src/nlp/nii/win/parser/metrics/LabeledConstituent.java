/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.metrics;

/**
 *
 * @author lelightwin
 */
public class LabeledConstituent<L> {

    L label;
    int start;
    int end;

    public L getLabel() {
        return label;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LabeledConstituent)) {
            return false;
        }

        final LabeledConstituent labeledConstituent = (LabeledConstituent) o;

        if (end != labeledConstituent.end) {
            return false;
        }
        if (start != labeledConstituent.start) {
            return false;
        }
        if (label != null ? !label.equals(labeledConstituent.label)
                : labeledConstituent.label != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (label != null ? label.hashCode() : 0);
        result = 29 * result + start;
        result = 29 * result + end;
        return result;
    }

    @Override
    public String toString() {
        return label + "[" + start + "," + end + "]";
    }

    public LabeledConstituent(L label, int start, int end) {
        this.label = label;
        this.start = start;
        this.end = end;
    }
}
