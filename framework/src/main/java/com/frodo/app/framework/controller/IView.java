package com.frodo.app.framework.controller;

/**
 * 用于UI的mvp模式中的v
 * Created by frodo on 2015/4/1.
 */
public interface IView {
    UIViewController getPresenter();

    boolean isOnShown();

    void show(boolean show);
}
