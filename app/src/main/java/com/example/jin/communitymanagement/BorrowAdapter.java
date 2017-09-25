package com.example.jin.communitymanagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by summe on 2017/9/25.
 */

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.ViewHolder> {
    private List<BorrowItem> m_list;
    private Context mContext;
    private onMyItClickListener m_c_listener;
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView imageView;
        TextView T_start;
        TextView T_end;
        TextView   T_name;
       private onMyItClickListener itemClickListener=null;
        public ViewHolder(View view,onMyItClickListener itemClickListener)
        {
            super(view);
            cardView=(CardView)view;
            imageView=(ImageView) view.findViewById(R.id.img_borrow_item);
            T_start=(TextView)view.findViewById(R.id.text_borrow_start_time);
            T_end=(TextView)view.findViewById(R.id.text_borrow_end_time);
            T_name=(TextView)view.findViewById(R.id.text_borrow_Name);
            this.itemClickListener=itemClickListener;
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(itemClickListener!=null){
                itemClickListener.onItemClick(view,getPosition());
            }
        }
    }
    public void setItemOnClickListener(onMyItClickListener listener){
        m_c_listener=listener;
    }
    public BorrowAdapter(List<BorrowItem> borrow_list )
    {
        this.m_list=borrow_list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext==null)
        {
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.borrow_item_c,parent,false);

        return  new ViewHolder(view,m_c_listener);
    }
    public byte[] get_bit_image(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BorrowItem borrowItem=m_list.get(position);
        holder.T_name.setText(borrowItem.getBorrowName());
        holder.T_start.setText(borrowItem.getStart_time());
        holder.T_end.setText(borrowItem.getEnd_time());
        holder.imageView.setImageBitmap(borrowItem.getBitmap());

    }
    @Override
    public int getItemCount() {
        return m_list.size();
    }
    public void setFilter(List<BorrowItem> borrow_List)
    {
       m_list=new ArrayList<>();
       m_list.addAll(borrow_List);
        notifyDataSetChanged();
    }

    public void addItem(int position,BorrowItem borrowItem)
    {
        m_list.add(position,borrowItem);
        notifyItemInserted(position);
    }
    public BorrowItem removeItem(int position)
    {
        final BorrowItem borrowItem=m_list.remove(position);
        notifyItemRemoved(position);
        return borrowItem;
    }
    public void moveItem(int fromPosition,int toPosition)
    {
        final BorrowItem borrowItem=m_list.remove(fromPosition);
        m_list.add(toPosition,borrowItem);
        notifyItemMoved(fromPosition,toPosition);
    }
    private void applyAndAnimateMovedItems(List<BorrowItem> borrow_List)
    {
        for (int toPosition = borrow_List.size() - 1; toPosition >= 0; toPosition--) {
            final BorrowItem borrowItem = borrow_List.get(toPosition);
            final int fromPosition = m_list.indexOf(borrowItem);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }

    }
    private void applyAndAnimateAdditions(List<BorrowItem> borrow_List) {
        for (int i = 0, count =borrow_List.size(); i < count; i++) {
            final BorrowItem borrowItem = borrow_List.get(i);
            if (!m_list.contains(borrowItem)) {
                addItem(i, borrowItem);
            }
        }
    }
    private void applyAndAnimateRemovals(List<BorrowItem> borrow_List) {
        for (int i = m_list.size() - 1; i >= 0; i--) {
            final BorrowItem borrowItem = m_list.get(i);
            if (!borrow_List.contains(borrowItem)) {
                removeItem(i);
            }
        }
    }

    public void animateTo(List<BorrowItem> borrow_List) {
        applyAndAnimateRemovals(borrow_List);
        applyAndAnimateAdditions(borrow_List);
        applyAndAnimateMovedItems(borrow_List);
    }
    public  interface onMyItClickListener {

        void onItemClick(View v, int position);
    }
}
