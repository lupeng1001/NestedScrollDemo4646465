package com.tuacy.nestedscrolldemo.newtype_views;

/**
 * Created by Administrator on 2018/9/7.
 */

public class SaveData {
    private int id;
    private float percent;//动态变化的要绘制的角度
    private float all_percent;//要绘制的总角度
    private String color_value;
    private float current_percent;//动态变化的当前的角度
    private float total_percent;//要绘制的当前的角度

    public float getAll_percent() {
        return all_percent;
    }

    public void setAll_percent(float all_percent) {
        this.all_percent = all_percent;
    }

    public float getTotal_percent() {
        return total_percent;
    }

    public void setTotal_percent(float total_percent) {
        this.total_percent = total_percent;
    }

    public float getCurrent_percent() {
        return current_percent;
    }

    public void setCurrent_percent(float current_percent) {
        this.current_percent = current_percent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getColor_value() {
        return color_value;
    }

    public void setColor_value(String color_value) {
        this.color_value = color_value;
    }
}
