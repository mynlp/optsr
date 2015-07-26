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
public class RuleConstituent<L> {

    L label, lChild, rChild;
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
        if (!(o instanceof RuleConstituent)) {
            return false;
        }

        final RuleConstituent labeledConstituent = (RuleConstituent) o;

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
        if (lChild != null ? !lChild.equals(labeledConstituent.lChild)
                : labeledConstituent.lChild != null) {
            return false;
        }
        if (rChild != null ? !rChild.equals(labeledConstituent.rChild)
                : labeledConstituent.rChild != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (label != null ? label.hashCode() : 0) + 17
                * (lChild != null ? lChild.hashCode() : 0) - 7
                * (rChild != null ? rChild.hashCode() : 0);
        result = 29 * result + start;
        result = 29 * result + end;
        return result;
    }

    @Override
    public String toString() {
        String rChildStr = (rChild == null) ? "" : rChild.toString();
        return label + "->" + lChild + " " + rChildStr + "[" + start + ","
                + end + "]";
    }

    public RuleConstituent(L label, L lChild, L rChild, int start, int end) {
        this.label = label;
        this.lChild = lChild;
        this.rChild = rChild;
        this.start = start;
        this.end = end;
    }
}
