package com.bj.zhaoyun.view.chart;

/**
 * Created by Buuu 2017/11/2.
 */

public class PieChartPart {
    private int color;
    private float numerical;

    public PieChartPart() {
    }

    public PieChartPart(int color, float numerical) {
        this.color = color;
        this.numerical = numerical;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getNumerical() {
        return numerical;
    }

    public void setNumerical(int numerical) {
        this.numerical = numerical;
    }
}
