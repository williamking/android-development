package com.example.william.homework_3;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    public class FruitAdapter extends ArrayAdapter<Fruit> {
        private int resourceId;
        public FruitAdapter(Context context, int textViewResourceId, List<Fruit> objects) {
            super(context, textViewResourceId, objects);
            this.resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Fruit fruit = getItem(position);
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(resourceId, null, false);
            }
            ImageView img = (ImageView)convertView.findViewById(R.id.img);
            TextView name = (TextView)convertView.findViewById(R.id.name);
            img.setImageResource(fruit.getImageId());
            //ViewGroup.LayoutParams para = name.getLayoutParams();
            //para.width = img.getWidth();
            //para.height = img.getHeight();
            //name.setLayoutParams(para);
            name.setText(fruit.getName());
            return convertView;
        }
    }

    public FruitAdapter adapter;
    public List<Fruit> fruitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fruitList = new ArrayList<Fruit>();
        fruitList.add(new Fruit("apple", R.mipmap.apple));
        fruitList.add(new Fruit("banana", R.mipmap.banana));
        fruitList.add(new Fruit("cherry", R.mipmap.cherry));
        fruitList.add(new Fruit("coco", R.mipmap.coco));
        fruitList.add(new Fruit("kiwi", R.mipmap.kiwi));
        fruitList.add(new Fruit("orange", R.mipmap.orange));
        fruitList.add(new Fruit("pear", R.mipmap.pear));
        fruitList.add(new Fruit("strawberry", R.mipmap.strawberry));
        fruitList.add(new Fruit("watermelon", R.mipmap.watermelon));
        adapter = new FruitAdapter(this, R.layout.fruit, fruitList);
        ListView listView = new ListView(this);
        listView.setAdapter(adapter);
        RelativeLayout window = (RelativeLayout)findViewById(R.id.window);
        window.addView(listView);
    }
}
