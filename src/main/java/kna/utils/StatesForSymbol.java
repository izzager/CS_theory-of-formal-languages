package kna.utils;

import java.util.List;
import java.util.Objects;

public class StatesForSymbol {
    private Character symbol;
    private List<String> states;

    public StatesForSymbol(Character symbol, List<String> states) {
        this.symbol = symbol;
        this.states = states;
    }

    public Character getSymbol() {
        return symbol;
    }

    public void setSymbol(Character symbol) {
        this.symbol = symbol;
    }

    public List<String>  getStates() {
        return states;
    }

    public void setState(List<String>  state) {
        this.states = state;
    }

    public void addState(String state) {
        states.add(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatesForSymbol pair = (StatesForSymbol) o;
        return Objects.equals(symbol, pair.symbol) && Objects.equals(states, pair.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, states);
    }

    @Override
    public String toString() {
        return "{" +
                "symbol=" + symbol +
                ", states='" + states + '\'' +
                '}';
    }
}
