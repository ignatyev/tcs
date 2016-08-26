package com.company;

/**
 * Created by AnVIgnatev on 26.08.2016.
 */
public class Stats {
    private long min;
    private long max;
    private float avg;
    private String idOfMax;
    private int count;
    private long sum;

    public Stats(long min, long max, float avg, int count, long sum, String idOfMax) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.count = count;
        this.sum = sum;
        this.idOfMax = idOfMax;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "min=" + min +
                ", max=" + max +
                ", avg=" + avg +
                ", idOfMax='" + idOfMax + '\'' +
                ", count=" + count +
                ", sum=" + sum +
                '}';
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public String getIdOfMax() {
        return idOfMax;
    }

    public void setIdOfMax(String idOfMax) {
        this.idOfMax = idOfMax;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
