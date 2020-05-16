package it.cubicworldsimulator.game.utility;

public class Pair<X,T> {

    private X firstValue;
    private T secondValue;

    public Pair() {
    }

    public Pair(X firstValue, T secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public X getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(X firstValue) {
        this.firstValue = firstValue;
    }

    public T getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(T secondValue) {
        this.secondValue = secondValue;
    }

    public boolean hasFirstValue(){
        return firstValue != null;
    }

    public boolean hasSecondValue(){
        return secondValue != null;
    }
}
