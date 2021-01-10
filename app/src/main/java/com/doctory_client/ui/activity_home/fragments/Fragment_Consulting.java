package com.doctory_client.ui.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doctory_client.R;
import com.doctory_client.adapters.AppointmentAdapter;
import com.doctory_client.adapters.Room_Adapter;
import com.doctory_client.databinding.FragmentConsultingBinding;
import com.doctory_client.databinding.FragmentHomeBinding;
import com.doctory_client.models.ChatUserModel;
import com.doctory_client.models.UserModel;
import com.doctory_client.models.UserRoomModelData;
import com.doctory_client.mvp.fragment_caht_room_mvp.ChatRoomFragmentView;
import com.doctory_client.mvp.fragment_caht_room_mvp.FragmentChatRoomPresenter;
import com.doctory_client.preferences.Preferences;
import com.doctory_client.ui.activity_home.HomeActivity;
import com.doctory_client.ui.chat_activity.ChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Consulting extends Fragment implements ChatRoomFragmentView {
    private FragmentConsultingBinding binding;
    private HomeActivity activity;
    private FragmentChatRoomPresenter presenter;
    private Preferences preferences;
    private UserModel userModel;
    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Room_Adapter room_adapter;
    private LinearLayoutManager manager;
    private boolean isLoading = false;
    private String lang;

    public static Fragment_Consulting newInstance(){
        return new Fragment_Consulting();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_consulting,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity=(HomeActivity)getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        presenter = new FragmentChatRoomPresenter(this, activity);
        //binding.setLang(lang);
        userRoomModelList = new ArrayList<>();
        room_adapter = new Room_Adapter(activity, userRoomModelList,this);
        manager = new LinearLayoutManager(activity);
        binding.recView.setLayoutManager(manager);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setAdapter(room_adapter);
//        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    int total_items = room_adapter.getItemCount();
//                    int lastItemPos = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//
//                    if (total_items >= 6 && (lastItemPos == total_items - 2) && !isLoading) {
//                        isLoading = true;
//                        userRoomModelList.add(null);
//                        room_adapter.notifyItemInserted(userRoomModelList.size() - 1);
//                        //  loadMore(page);
//                    }
//
//                }
//            }
//        });
        if(userModel!=null){
            presenter.getRooms(userModel);}


    }

    public void setItemData(UserRoomModelData.UserRoomModel userRoomModel, int adapterPosition) {

        // userRoomModel.setMy_message_unread_count(0);
        userRoomModelList.set(adapterPosition, userRoomModel);
        room_adapter.notifyItemChanged(adapterPosition);

        ChatUserModel chatUserModel = new ChatUserModel(userRoomModel.getDoctor_fk().getName(), userRoomModel.getDoctor_fk().getLogo(), userRoomModel.getUser_id()+"", userRoomModel.getId());
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("chat_user_data", chatUserModel);
        startActivityForResult(intent, 1000);
    }


    @Override
    public void onFinished() {

    }

    @Override
    public void onProgressShow() {
        binding.progBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressHide() {
        binding.progBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailed(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void ondata(UserRoomModelData body) {
        userRoomModelList.clear();
        userRoomModelList.addAll(body.getData());
        if (userRoomModelList.size() > 0) {
            room_adapter.notifyDataSetChanged();
            binding.tvNoData.setVisibility(View.GONE);
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
    }
}