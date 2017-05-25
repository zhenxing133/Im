package com.ithomework.im.adapter;
import com.ithomework.im.mode.User;
import com.ithomework.im.R;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{

    private List<User> list ;
    private List<String> contactsList;
    public AddFriendAdapter(List<User> list,List<String> contacts) {
        this.list = list;
        contactsList = contacts;
    }

    @Override
    public AddFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        AddFriendViewHolder holder = new AddFriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddFriendViewHolder holder, int position) {
        User user = list.get(position);
        final String username = user.getUsername();
        holder.mTvUsername.setText(username);
        holder.mTvTime.setText(user.getCreatedAt());
        //判断当前username是不是已经在好友列表中了
        if (contactsList.contains(username)){
            holder.mBtnAdd.setText("已经是好友");
            holder.mBtnAdd.setEnabled(false);
        }else{
            holder.mBtnAdd.setText("添加");
            holder.mBtnAdd.setEnabled(true);
        }
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddFriendClickListener!=null){
                    mOnAddFriendClickListener.onAddClick(username);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }



    class AddFriendViewHolder extends RecyclerView.ViewHolder{
        TextView mTvUsername;
        TextView mTvTime;
        Button mBtnAdd;

        public AddFriendViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }

    public interface OnAddFriendClickListener{
        void onAddClick(String username);
    }
    private OnAddFriendClickListener mOnAddFriendClickListener;
    public void setOnAddFriendClickListener(OnAddFriendClickListener addFriendClickListener){
        this.mOnAddFriendClickListener = addFriendClickListener;
    }

}
