package com.carlchenxq.zidong;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Menu> menuInfo = new ArrayList<>();
    int flag;
    public static Dialog bottomDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        initDB();
    }


    /**
     * 初始化ArrayList和DataBase
     */
    public void initDB() {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor cursor = db.query("menu", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            //String _id = cursor.getString(cursor.getColumnIndex("_id"));
            String tempName = cursor.getString(cursor.getColumnIndex("name"));
            String tempLoc = cursor.getString(cursor.getColumnIndex("loc"));
            Menu tempMenu = new Menu();
            tempMenu.setName(tempName);
            tempMenu.setLoc(tempLoc);
            menuInfo.add(tempMenu);
        }
    }

    /**
     * 完成添加按钮实现的操作
     */
    public void add(View view) {
        //初始化两个EditTExt对象
        EditText edit_name = (EditText) findViewById(R.id.edit_name);
        EditText edit_loc = (EditText) findViewById(R.id.edit_loc);

        if (edit_name.getText().length() == 0 || edit_loc.getText().length() == 0) {

            final Toast fToast = Toast.makeText(MainActivity.this, "要么没吃的，要么去不了", Toast.LENGTH_LONG);
            fToast.show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    fToast.cancel();
                }
            }, 1000);

            return;

        }

        final Toast sToast = Toast.makeText(MainActivity.this, "添加成功，去看看吧！:)", Toast.LENGTH_LONG);
        //获取EditText控件中的值并存储到String对象中
        String tempName = edit_name.getText().toString().trim();
        String tempLoc = edit_loc.getText().toString().trim();

        //创建一个中间Menu对象，用来暂存获取到的值
        Menu tempMenu = new Menu();
        tempMenu.setName(tempName);
        tempMenu.setLoc(tempLoc);

        //将暂存的Menu对象添加到ArrayList中
        menuInfo.add(tempMenu);

        //将新添加的项写入数据库
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", tempMenu.getName());
        values.put("loc", tempMenu.getLoc());
        db.insert("menu", null, values);

        sToast.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sToast.cancel();
            }
        }, 1000);

        //最后清空输入框内容
        edit_name.setText("");
        edit_loc.setText("");


    }

    /**
     * 完成随机选择按钮实现的操作
     */
    public void choose(View view) {

        if (menuInfo.size() == 0) {

            final Toast fToast = Toast.makeText(MainActivity.this, "啥也没有，左转添加 :(", Toast.LENGTH_SHORT);
            fToast.show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    fToast.cancel();
                }
            }, 1000);
            return;

        }

        int arrayListSize = menuInfo.size();
        Random random = new Random();
        TextView nameTextView = (TextView) findViewById(R.id.text_dashboard_name);
        TextView locTextView = (TextView) findViewById(R.id.text_dashboard_loc);
        TextView hintTextView = (TextView) findViewById(R.id.test_text_view);
        final Toast suToast = Toast.makeText(MainActivity.this, "选完了，就是这么快 :D", Toast.LENGTH_LONG);

        //quantityTextView.setText("" + number);

        for (int i = 0; i < 100; i++) {
            flag = random.nextInt(arrayListSize);
            nameTextView.setText(menuInfo.get(flag).getName());
            locTextView.setText(menuInfo.get(flag).getLoc());

            //set a hint
            hintTextView.setText("不喜欢这道菜？");
        }

        suToast.show();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                suToast.cancel();
            }
        }, 1000);

    }


    /**
     * the method to awake the dialog
     */
    public void deleteOnClick(View view) {
        //Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_LONG).show();
        //Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }


    /**
     * the method to delete the selected item of the ArrayList
     */
    public void delete(View view) {

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "delete from menu where name='" + menuInfo.get(flag).getName() + "'";
        menuInfo.remove(flag);
        db.execSQL(sql);
        bottomDialog.cancel();
        Toast.makeText(MainActivity.this, "删除成功，再也没有啦！:)", Toast.LENGTH_LONG).show();

    }

    /**
     * the method to cancel the dialog
     * */

    public void cancelDialog(View view) {
        bottomDialog.cancel();
    }


}
