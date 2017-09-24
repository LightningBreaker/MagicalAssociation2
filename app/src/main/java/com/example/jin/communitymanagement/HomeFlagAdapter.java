package com.example.jin.communitymanagement;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by summe on 2017/8/23.
 */

public class HomeFlagAdapter extends RecyclerView.Adapter<HomeFlagAdapter.ViewHolder> {
    private List<HomeFlag> mHomeFlagList;

    public HomeFlagAdapter(List<HomeFlag> homeFlagList)
    {
        mHomeFlagList=homeFlagList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_header_check_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         HomeFlag homeFlag=mHomeFlagList.get(position);
         holder.textName.setText(homeFlag.getFlagName());
        holder.isChoosed.setChecked(homeFlag.getChoosed());

    }

    @Override
    public int getItemCount() {
        return mHomeFlagList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private com.bigmercu.cBox.CheckBox isChoosed;
        private TextView textName;

        public ViewHolder(View itemView) {
            super(itemView);
            isChoosed=( com.bigmercu.cBox.CheckBox)itemView.findViewById(R.id.checkbox_flag_home);
            textName=(TextView)itemView.findViewById(R.id.text_flag_home);
        }
    }



}
