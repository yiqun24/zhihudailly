package com.example.zhihudailly;

import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public void setTitle(int type){
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        switch (type){
            case 1:
                toolbar.setNavigationIcon(R.drawable.home);
                break;
            case 2:
                toolbar.setNavigationIcon(R.drawable.md_nav_back);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        }
    }
   public void showToast(String content){
      Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
   }
}
