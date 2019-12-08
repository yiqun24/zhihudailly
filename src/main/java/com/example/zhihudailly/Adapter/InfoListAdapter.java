package com.example.zhihudailly.Adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.zhihudailly.ArticleContentActivity;
import com.example.zhihudailly.Bean.Item;
import com.example.zhihudailly.MainActivity;
import com.example.zhihudailly.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class InfoListAdapter extends RecyclerView.Adapter<InfoListAdapter.InfoViewHolder> {
    private ArrayList<Item> mData;
    private Context mContext;
    InfoViewHolder holder=null;
    private int mdate = 0;
    public  InfoListAdapter(ArrayList<Item> data,Context context) {
        this.mData = data;
        this.mContext=context;
    }
    @Override
    public InfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headView!=null && viewType==TYPE_HEADER) return new InfoViewHolder(headView);
        holder=new InfoViewHolder(LayoutInflater.from(mContext).inflate(R.layout.info_item,parent,false));
        return holder;
    }
    @Override
    public void onBindViewHolder(@NotNull InfoViewHolder holder, int position) {
        //此方法内可以对布局中的控件进行操作
        if (getItemViewType(position)==TYPE_HEADER) return;
        final int pos=getRealPosition(holder);
        final Item item = mData.get(pos);
        holder.title.setText(mData.get(pos).getTitle());
        holder.hint.setText(mData.get(pos).getHint());
//        if(pos == 3 || (pos - 3) % 5 ==0){
//            holder.time.setVisibility(View.VISIBLE);
//            holder.time.setText(getDate());
//            mdate++;
//        }
        Glide.with(mContext).load(mData.get(pos).getImgurl()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ArticleContentActivity.class);
                intent.putExtra("url",item.getUrl());
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        //获取数据长度
        return headView==null? mData.size():mData.size()+1;
    }
    class InfoViewHolder extends RecyclerView.ViewHolder {
        TextView title;//标题
        ImageView img;//显示的图片
        TextView headTitle;//头部标题
        TextView hint;
        TextView time;

        public InfoViewHolder(View itemView) {
            super(itemView);
            title=  itemView.findViewById(R.id.item_title);
            img= itemView.findViewById(R.id.item_image);
            headTitle=  itemView.findViewById(R.id.item_headtitle);
            hint = itemView.findViewById(R.id.hint);
            time =  itemView.findViewById(R.id.line);
        }
    }
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View headView;

    public void setHeadView(View headView){
        this.headView = headView;
        notifyItemInserted(0);
    }

    public View getHeadView(){
        return headView;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView==null)
            return TYPE_NORMAL;
        if (position==0)
            return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position=holder.getLayoutPosition();
        return headView==null? position:position-1;
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, -mdate); //获取对应日期
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("yyyy年MM月dd日").format(c.getTime());
        return date;
    }
}




















