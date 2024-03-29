package com.doctory_client.mvp.activity_chat_mvp;

import com.doctory_client.models.MessageDataModel;
import com.doctory_client.models.MessageModel;

public interface ChatActivityView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);


    void ondataload(MessageModel body);

    void ondataloadmore(MessageModel body);

    void onremove();
    void onLoad();
    void onFinishload();

    void ondataload(MessageDataModel body);

    void onfinishloadmore();

    void onloadmore();
}
