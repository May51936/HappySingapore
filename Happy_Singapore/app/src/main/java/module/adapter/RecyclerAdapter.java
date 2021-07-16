package module.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.WangTianyu.HappySingapore.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import module.data.Picture;
import module.url.NewsRsp;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<NewsRsp> list;
    private View inflater;

    public RecyclerAdapter(Context context, ArrayList<NewsRsp> list){
        this.context = context;
        this.list = list;
    }

    public int getItemCount(){
        return list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        inflater = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        NewsRsp item = list.get(position);
        String title = item.get_title().substring(0,item.get_title().lastIndexOf(" -"));
        String date = item.get_date().substring(0,10);
        holder.date.setText(date);
        holder.name.setText(item.get_name());
        holder.title.setText(title);
        Picture picture = new Picture((Activity) context, holder);
        try {
            picture.getFromURL(item.get_pic());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(item.get_url()));
                context.startActivity(intent);
            }
        });
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public ImageView img;
        TextView title;
        TextView name;
        TextView date;
        public MyViewHolder(View itemView){
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.news_item_img);
            title = (TextView) itemView.findViewById(R.id.news_item_title);
            name = (TextView) itemView.findViewById(R.id.news_item_name);
            date = (TextView) itemView.findViewById(R.id.news_item_date);
        }
    }
}
