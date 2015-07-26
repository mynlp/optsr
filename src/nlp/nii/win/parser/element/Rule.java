/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.nii.win.parser.element;

import java.util.Objects;

/**
 *
 * @author lelightwin
 * @param <T>
 */
public class Rule<T> {

    private int id;
    private T cons;
    private T child1;
    private T child2;
    private T action;
    private int head;

    public Rule(T cons, T child1, T child2, int head) {
        this.cons = cons;
        this.child1 = child1;
        this.child2 = child2;
        this.head = head;
    }

    public Rule(T cons, T child1, int head) {
        this.cons = cons;
        this.child1 = child1;
        this.child2 = null;
        this.head = head;
    }

    public int head() {
        return head;
    }

    /**
     * @return the cons
     */
    public T cons() {
        return cons;
    }

    public void setAction(T action) {
        this.action = action;
    }

    public T action() {
        return action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T child1() {
        return child1;
    }

    public T child2() {
        return child2;
    }

    public boolean isBinary() {
        return (child2 != null);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.cons);
        hash = 59 * hash + Objects.hashCode(this.child1);
        hash = 59 * hash + Objects.hashCode(this.child2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rule<?> other = (Rule<?>) obj;
        if (!Objects.equals(this.cons, other.cons)) {
            return false;
        }
        if (!Objects.equals(this.child1, other.child1)) {
            return false;
        }
        if (!Objects.equals(this.child2, other.child2)) {
            return false;
        }
        return true;
    }
}
