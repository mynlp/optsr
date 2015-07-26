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
public class UnlabeledConstituent<L> {

    int start;
    int end;

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
        if (!(o instanceof UnlabeledConstituent)) {
            return false;
        }

        final UnlabeledConstituent unlabeledConstituent = (UnlabeledConstituent) o;

        if (end != unlabeledConstituent.end) {
            return false;
        }
        if (start != unlabeledConstituent.start) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;

        result = start;
        result = 29 * result + end;
        return result;
    }

    @Override
    public String toString() {
        return "[" + start + "," + end + "]";
    }

    public UnlabeledConstituent(int start, int end) {

        this.start = start;
        this.end = end;
    }
}
