package com.xiajunzhuang.hmstest.notetest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xiajunzhuang.hmstest.notetest.R;
import com.xiajunzhuang.hmstest.notetest.bean.Record;

import java.util.List;

public class RecordRecyclerAdapter extends RecyclerView.Adapter<RecordRecyclerAdapter.ItemViewHolder>{
    private List<Record> ss = null;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecordRecyclerAdapter(List<Record> ss, Context context) {
        this.ss = ss;
        this.context = context;
        this.layoutInflater=LayoutInflater.from(context);
    }

    //单击事件
    private OnItemClickListener onItemClickListener;
    //长事件
    private OnItemLongClickListener onItemLongClickListener;


    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecordRecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.note_item,parent,false);
            return new ItemViewHolder(view);
    }


    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        if(getItemViewType(position)==0) {
            ((ItemViewHolder) holder).update();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.itemView,position);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemLongClick(holder.itemView,position);
                return true;
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if(position<ss.size()){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return ss.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout llBg;
        private TextView tvNoteId;
        private TextView tvLocktype;
        private TextView tvNoteBody;
        private ImageView ivImge;
        private TextView tvNoteTitle;
        private TextView tvNoteTime;
        private ImageView imDatatime;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            llBg = (RelativeLayout) itemView.findViewById(R.id.ll_bg);
            tvNoteId = (TextView) itemView.findViewById(R.id.tv_note_id);
            tvLocktype = (TextView) itemView.findViewById(R.id.tv_locktype);
            tvNoteBody = (TextView) itemView.findViewById(R.id.tv_note_body);
            ivImge = (ImageView) itemView.findViewById(R.id.iv_imge);
            tvNoteTitle = (TextView) itemView.findViewById(R.id.tv_note_title);
            tvNoteTime = (TextView) itemView.findViewById(R.id.tv_note_time);
            imDatatime = (ImageView) itemView.findViewById(R.id.im_datatime);

        }
        public void update(){
            int position = this.getLayoutPosition();
            Record record = ss.get(position);
            tvNoteTitle.setText(record.getTitleName());
            tvNoteTime.setText(record.getCreateTime());
            tvNoteBody.setText(record.getTextBody());
        }
    }
}
