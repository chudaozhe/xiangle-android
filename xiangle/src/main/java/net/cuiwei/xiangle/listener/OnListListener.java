package net.cuiwei.xiangle.listener;


import net.cuiwei.xiangle.bean.BaseList2Response;

public interface OnListListener<T> {
    void onSuccess(BaseList2Response<T> list);
    void onError();
}
