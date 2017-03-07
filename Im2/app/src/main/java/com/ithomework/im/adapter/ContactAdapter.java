package com.ithomework.im.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ithomework.im.util.StringUtils;
import com.ithomework.im.R;
import java.util.List;

/**
 * Created by Administrator on 2017/3/4.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements IContactAdapter{
    private List<String> data;

    public ContactAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final String contact = (String) data.get(position);
        holder.mTvUsername.setText(contact);
        String initial = StringUtils.getInitial(contact);
        holder.mTvSection.setText(initial);
        if (position==0){
            holder.mTvSection.setVisibility(View.VISIBLE);
        }else {
            //获取上一个首字母
            String preContact = (String) data.get(position - 1);
            String preInitial = StringUtils.getInitial(preContact);
            if (preInitial.equals(initial)){
                holder.mTvSection.setVisibility(View.GONE);
            }else {
                holder.mTvSection.setVisibility(View.VISIBLE);
            }
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener !=null){
                    mOnItemClickListener.onItemLongClick(contact,position);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(contact,position);
                }
            }
        });

    }
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemLongClick(String contact, int position);
        void onItemClick(String contact, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }




    public List<String> getData() {
        return data;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView mTvSection;
        TextView mTvUsername;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mTvSection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }

}
