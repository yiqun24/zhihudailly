package com.example.zhihudailly;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.zhihudailly.Adapter.InfoListAdapter;
import com.example.zhihudailly.Bean.Item;
import com.example.zhihudailly.Bean.TopStories;
import com.example.zhihudailly.INTERNET.Client;
import com.example.zhihudailly.INTERNET.OkHttpUtils;
import com.example.zhihudailly.INTERNET.OnNetResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.Http2Reader;
import okhttp3.internal.ws.RealWebSocket;

public class MainActivity extends BaseActivity {
    private RecyclerView mInfoList;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<Item> mDatas;
    private InfoListAdapter adapter;
    private int otherdate = 0;
    private boolean isFirst = true;
    private ArrayList<TopStories> bannerList;
    private ArrayList<String> titles;
    private ArrayList<String> images;
    private ArrayList<Integer> ids;
    private ArrayList<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                refreshLayout.setRefreshing(false);
            }
        });
        mInfoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalItemCount;
            private int firstVisibleItem;
            private int visibleItemCount;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                visibleItemCount = recyclerView.getChildCount();
                if (((totalItemCount - visibleItemCount) <= firstVisibleItem)) {
                    onLoadMore();
                }
            }

            private void onLoadMore() {
                getData();
            }
        });
    }

    private void initView() {
        setTitle(1);
        mInfoList = findViewById(R.id.infolist);
        refreshLayout = findViewById(R.id.swipe);
        refreshLayout.setColorSchemeResources(R.color.gold);
        mInfoList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InfoListAdapter(mDatas, MainActivity.this);
        mInfoList.setAdapter(adapter);
    }

    private void initData() {
        mDatas = new ArrayList<>();
        titles = new ArrayList<>();
        ids = new ArrayList<>();
        images = new ArrayList<>();
        bannerList = new ArrayList<>();
        urls = new ArrayList<>();
        getDataAsync();
    }

    public void getDataAsync() {
        String url;
        if (isFirst) {
            url = "http://news-at.zhihu.com/api/3/news/latest";
        } else {

            url = " http://news-at.zhihu.com/api/3/news/before/" + getDate();
        }
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        final Call call = Client.getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("调试信息", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body() != null) {
                    String result = Objects.requireNonNull(response.body()).string();

                    Message message = Message.obtain();
                    if (isFirst) {
                        message.what = 1;
                    } else {
                        message.what = 2;
                    }

                    message.obj = result;
                    handler.sendMessage(message);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    try {
                        //这个地方很坑，如果直接message.toString 会导致Json数据格式错误，原因未知
                        initBanner(message.obj.toString());
                        isFirst = false;
                    } catch (JSONException e) {
                        Log.e("json解析", Log.getStackTraceString(e));
                    }
                case 2:
                    try {
                        parseJson(message.obj.toString());
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    private void parseJson(String json) throws JSONException {
        //通过json获取JSONObject对象
        JSONObject jsonObject = new JSONObject(json);
        JSONArray stories = jsonObject.getJSONArray("stories");
        //遍历这个数组
        for (int i = 0; i < stories.length(); i++) {
            JSONObject jsonStory = stories.getJSONObject(i);
            JSONArray images = jsonStory.getJSONArray("images");
            Item storyBean = new Item();
            storyBean.setId(jsonStory.getString("id"));
            storyBean.setHint(jsonStory.getString("hint"));
            storyBean.setImgurl(images.getString(0));
            storyBean.setUrl(jsonStory.getString("url"));
            storyBean.setTitle(jsonStory.getString("title"));
//            if(!mDatas.get(i).getId().equals(mDatas.get(1).getId())) {
                mDatas.add(storyBean);
        }
    }

    private void initBanner(String json) throws JSONException {
        JSONObject jsonObject2 = new JSONObject(json);
        JSONArray top_stories = jsonObject2.getJSONArray("top_stories");
        for (int i = 0; i < top_stories.length(); i++) {
            JSONObject item = top_stories.getJSONObject(i);
            TopStories item1 = new TopStories();
            item1.setImage(item.getString("image"));
            item1.setTitle(item.getString("title"));
            item1.setId(item.getInt("id"));
            item1.setUrl(item.getString("url"));
            bannerList.add(item1);
            titles.add(item1.getTitle());
            images.add(item1.getImage());
            ids.add(item1.getId());
            urls.add(item1.getUrl());
        }
        setHeader(mInfoList, images, titles, ids, urls);
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH,-otherdate); //获取对应日期
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return date;
    }

    private void setHeader(RecyclerView view, ArrayList<String> urls, ArrayList<String> titles, final ArrayList<Integer>ids, final ArrayList<String> articleUrls){
        View header = LayoutInflater.from(this).inflate(R.layout.headview, view, false);
        //找到banner所在的布局
        BGABanner banner =  header.findViewById(R.id.banner);
        //绑定banner
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(MainActivity.this)
                        .load(model)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                String TopUrl = articleUrls.get(position);
                Intent intent = new Intent(MainActivity.this,ArticleContentActivity.class);
                intent.putExtra("url",TopUrl);
                startActivity(intent);
            }
        });
        banner.setData(urls, titles);
        adapter.setHeadView(header);//向适配器中添加banner
    }

    private void refreshData() {
        int date = otherdate;
        otherdate = 0;
        getDataAsync();
        for(int i = 0; i < mDatas.size(); i++){
            for (int j = i + 1; j < mDatas.size(); j++)
            {
                if(mDatas.get(i).getId().equals(mDatas.get(j).getId()))
                {
                    mDatas.remove(j);
                }
            }
        }
        adapter.notifyItemInserted(1);
        otherdate = date;
//        Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
    }

    private void getData() {
        getDataAsync();
        otherdate++;
    }
}




