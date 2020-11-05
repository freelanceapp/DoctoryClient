package com.doctory_client.mvp.activity_shop_details_mvp;

import com.doctory_client.models.PlaceDetailsModel;

public interface ActivityShopDetailsView {
    void onProgressShow();
    void onProgressHide();
    void onFailed(String msg);
    void onSuccess(PlaceDetailsModel model);
}
