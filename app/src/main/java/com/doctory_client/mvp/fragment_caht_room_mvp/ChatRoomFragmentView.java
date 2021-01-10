package com.doctory_client.mvp.fragment_caht_room_mvp;

import com.doctory_client.models.UserRoomModelData;

public interface ChatRoomFragmentView {
    void onFinished();
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);

    void ondata(UserRoomModelData body);
}
