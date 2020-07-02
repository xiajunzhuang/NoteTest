package com.xiajunzhuang.hmstest.notetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xiajunzhuang.hmstest.notetest.adapter.OnItemClickListener;
import com.xiajunzhuang.hmstest.notetest.adapter.OnItemLongClickListener;
import com.xiajunzhuang.hmstest.notetest.adapter.RecordRecyclerAdapter;
import com.xiajunzhuang.hmstest.notetest.bean.Record;
import com.xiajunzhuang.hmstest.notetest.database.MyDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MyDB myDB;
    private RecyclerView recyclerview;
    private RecordRecyclerAdapter myRecordAdapter;
    private FloatingActionButton fab;
    private List<Record> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        init();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

    }
    //初始化控件
    public void init(){
        recordList = new ArrayList<>();
        myDB = new MyDB(this);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor cursor = db.query(MyDB.TABLE_NAME_RECORD,null,
                null,null,null,
                null,MyDB.NOTICE_TIME+","+MyDB.RECORD_TIME+" DESC");
        if(cursor.moveToFirst()){
            Record record;
            while (!cursor.isAfterLast()){
                record = new Record();
                record.setId(
                        Integer.valueOf(cursor.getString(cursor.getColumnIndex(MyDB.RECORD_ID))));
                record.setTitleName(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_TITLE))
                );
                record.setTextBody(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_BODY))
                );
                record.setCreateTime(
                        cursor.getString(cursor.getColumnIndex(MyDB.RECORD_TIME)));
                record.setNoticeTime(
                        cursor.getString(cursor.getColumnIndex(MyDB.NOTICE_TIME)));
                recordList.add(record);
                cursor.moveToNext();
            }


        }
        cursor.close();
        db.close();
        System.out.println(recordList);
        // 创建一个Adapter的实例
        myRecordAdapter = new RecordRecyclerAdapter(recordList,MainActivity.this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview = findViewById(R.id.recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(myRecordAdapter);
        // 设置点击监听
        myRecordAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.d("onItemClick: ","short"+position);

//                Toast.makeText(MainActivity.this,"short",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Record record = recordList.get(position);
                intent.putExtra(MyDB.RECORD_TITLE,record.getTitleName().trim());
                intent.putExtra(MyDB.RECORD_BODY,record.getTextBody().trim());
                intent.putExtra(MyDB.RECORD_TIME,record.getCreateTime().trim());
                intent.putExtra(MyDB.RECORD_ID,record.getId().toString().trim());
                if (record.getNoticeTime()!=null) {
                    intent.putExtra(MyDB.NOTICE_TIME, record.getNoticeTime().trim());
                }
                startActivity(intent);
                MainActivity.this.finish();
                myRecordAdapter.notifyDataSetChanged();
            }
        });

        myRecordAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("onItemLongClick: ","long"+position);
                Record record =recordList.get(position);
                showDialog(record,position);
            }
        });
    }
    void showDialog(final Record record, final int position){

        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("确认要删除？");
        String textBody = "所选的记录"+record.getTitleName();
        dialog.setMessage(
                textBody.length()>150?textBody.substring(0,150)+"...":textBody);
        dialog.setPositiveButton("删除",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = myDB.getWritableDatabase();
                        db.delete(MyDB.TABLE_NAME_RECORD,
                                MyDB.RECORD_ID +"=?",
                                new String[]{String.valueOf(record.getId())});
                        db.close();
                        recordList.remove(position);
                        myRecordAdapter.notifyDataSetChanged();
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
            default:
                break;
        }
    }
}