package com.egco428.a13281;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CommentsDataSource dataSource;
    private ArrayAdapter<Comment> arrayAdapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dataSource = new CommentsDataSource(this);
        dataSource.open();
        List<Comment> values = dataSource.getAllComments();
        listview = (ListView) findViewById(R.id.listView);
        arrayAdapter = new CourseArrayAdapter(this,0,values);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int position, long l) {
                if (arrayAdapter.getCount()>0){
                    final Comment comment = arrayAdapter.getItem(position);
                    dataSource.deleteComment(comment);
                    view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            arrayAdapter.remove(comment);
                            view.setAlpha(1);
                        }
                    });
                }
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, NewFortuneCookies.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
        List<Comment> values = dataSource.getAllComments();
        arrayAdapter = new CourseArrayAdapter(this,0,values);
        listview.setAdapter(arrayAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

    class CourseArrayAdapter extends ArrayAdapter<Comment>{
        Context context;
        List<Comment> objects;
        public CourseArrayAdapter(Context context,int resource,List<Comment> objects){
            super(context,resource,objects);
            this.context = context;
            this.objects = objects;
        }
        @Override public View getView(int position, View convertView, ViewGroup parent){
            Comment comment = objects.get(position);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.rowlist,null);

            TextView txt = (TextView)view.findViewById(R.id.cookieName);
            if (comment.getComment().equals("Don't Panic") || comment.getComment().equals("Work Harder")){
                txt.setTextColor(Color.parseColor("#FF7800"));
            }else {
                txt.setTextColor(Color.parseColor("#1300FF"));
            }
            txt.setText(comment.getComment());

            TextView txt1 = (TextView)view.findViewById(R.id.cookiesDate);
            txt1.setText(comment.getDate());

            int res = context.getResources().getIdentifier(comment.getImg(),"drawable",context.getPackageName());
            ImageView image = (ImageView)view.findViewById(R.id.cookiesImg);
            image.setImageResource(res);
            return view;
        }
    }
}