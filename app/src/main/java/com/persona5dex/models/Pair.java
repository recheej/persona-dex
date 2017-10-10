package com.persona5dex.models;

/**
 * Created by Rechee on 8/20/2017.
 */

public class Pair<T, U> {
    private T first;
    private U second;

    public Pair(T first, U second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Pair){
            Pair pair = (Pair) obj;

            return (pair.first.equals(this.first) && pair.second.equals(this.second)) ||
                    (pair.first.equals(this.second) && pair.second.equals(this.first));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }
}
