package com.lifeistech.android.dev_15;

import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;
    EditText wordEditText, meanEditText, searchWordEditText;

    HashMap<String, String> hashMap;
    TreeSet<String> wordSet;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        wordEditText = (EditText) findViewById(R.id.word);
        meanEditText = (EditText) findViewById(R.id.mean);
        searchWordEditText = (EditText) findViewById(R.id.searchWord);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        this.hashMap = new HashMap<String, String>();//thisは必要なのでしょうか？
        wordSet = new TreeSet<String>();//自動並び替えset
        pref = getSharedPreferences("dictionary", MODE_PRIVATE);//dictionaryという倉庫
        editor = pref.edit();

        editor.putString("technology", "科学技術");
        editor.putString("develop", "開発する");
        editor.commit();

        wordSet.add("technology");
        wordSet.add("develop");

        //Setに保存した英語を取得
        wordSet.addAll(pref.getStringSet("wordSet", wordSet));

        for (String word : wordSet) {
            //Mapに格納
            this.hashMap.put(word, pref.getString(word, null));
            //アダプターに入れる
            adapter.add("[" + word + "]" + pref.getString(word, null));
        }
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapter.getItem(i);
                adapter.remove(item);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.clear();

                return false;
            }
        });

    }

    public void add(View v) {
        String entryWord = wordEditText.getText().toString();
        String entryMean = meanEditText.getText().toString();
        String entryObject = "[" + entryWord + "]" + entryMean;

        //Setに入力した単語を保存
        wordSet.add(entryWord);
        //入力単語がkey, 意味を保存
        editor.putString(entryWord, entryMean);
        //wordSetを保存
        editor.putStringSet("wordSet", wordSet);
        editor.commit();

        adapter.add(entryObject);
    }

    public void search(View v) {
        String searchWord = searchWordEditText.getText().toString();
        //Setに保存した単語を取得．
        wordSet.addAll(pref.getStringSet("wordSet", wordSet));//いらないかも
        //Mapに追加
        for (String word : wordSet) {
            hashMap.put(word, pref.getString(word, null));
        }
        if (hashMap.containsKey(searchWord)) {
            Toast.makeText(this, hashMap.get(searchWord), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "この単語は登録されていません", Toast.LENGTH_LONG).show();
        }

    }


}
