package ru.task.currency.service;

import java.util.TimerTask;

public class LoaderBarService extends TimerTask {
    public void run() {
        System.out.print(".");
    }
}
