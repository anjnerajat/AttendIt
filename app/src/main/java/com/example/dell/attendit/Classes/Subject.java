package com.example.dell.attendit.Classes;

/**
 * Created by dell on 24/06/2016.
 */

public class Subject {
    public String name;
    public int credits, hours, total, present, absent, cancelled, minimum, status;
    public double  percentage;
    public boolean isPresent, isAbsent, isCancelled;

    public Subject(String name, int minimum) {
        this.name = name;
        this.minimum = minimum;
        this.total=0;
        this.present=0;
        this.absent=0;
        this.cancelled=0;
        this.percentage=0.00f;
    }

    public Subject(String name, int total, int present, int absent, int cancelled, int minimum, int status) {
        this.name = name;
        this.total = total;
        this.present = present;
        this.absent = absent;
        this.cancelled = cancelled;
        this.minimum = minimum;
        this.status=status;
        if(total!=0){
            this.percentage = (double)present*100/total;
        }
        else
            this.percentage=0;
        isAbsent=false;
        isPresent=false;
        isCancelled=false;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setIsAbsent(boolean isAbsent) {
        this.isAbsent = isAbsent;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPresent() {
        return present;
    }

    public void setPresent(int present) {
        this.present = present;
    }

    public int getAbsent() {
        return absent;
    }

    public void setAbsent(int absent) {
        this.absent = absent;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
