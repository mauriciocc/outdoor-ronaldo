package br.univates.domain;

import java.util.Comparator;

public abstract class RoutedElement extends Model {

    public static final Comparator<RoutedElement> ByOrder = (RoutedElement o1, RoutedElement o2) -> Integer.valueOf(o1.getOrder()).compareTo(o2.getOrder());

    protected int timeOnScreen = 10;
    protected int order;

    public int getTimeOnScreen() {
        return timeOnScreen;
    }

    public void setTimeOnScreen(int timeOnScreen) {
        this.timeOnScreen = timeOnScreen;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
