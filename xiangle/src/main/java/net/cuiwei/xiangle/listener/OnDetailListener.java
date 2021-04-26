package net.cuiwei.xiangle.listener;


public interface OnDetailListener<T> {
    void onSuccess(T field);
    void onError();
}
