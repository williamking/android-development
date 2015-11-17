package com.example.william.homework_3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
            final Fruit fruit = getItem(position);
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
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBundle.putString("Data", "I love " + fruit.getName().toString());
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }
        @Override
        public boolean isEnabled(int position) {
            return true;
        }

    }

    public FruitAdapter adapter;
    public List<Fruit> fruitList;

    private Intent intent;
    private Bundle mBundle = new Bundle();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, FruitActivitty.class);
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
        listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                fruitList.remove(position);
                adapter.notifyDataSetChanged();
                listView.invalidate();
                return false;
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Fruit fruit = fruitList.get(position);
//                mBundle.putString("Data", "I love " + fruit.getName().toString());
//                intent.putExtras(mBundle);
//                startActivity(intent);
//            }
//        });
        RelativeLayout window = (RelativeLayout)findViewById(R.id.window);
        window.addView(listView);
    }
}
