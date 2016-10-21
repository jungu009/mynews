package cn.jungu009.mynews.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.jungu009.mynews.R;

/**
 *  分类管理
 */
public class CategoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private CheckBox tt, cj, kj, sh, ss, yl, gj, gn, js, ty;
    private List<String> allCategories;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        allCategories = Arrays.asList(getResources().getStringArray(R.array.category));
        categories =  getIntent().getStringArrayListExtra("categories");

        tt = (CheckBox)findViewById(R.id.checkbox_toutiao);
        tt.setOnCheckedChangeListener(this);
        cj = (CheckBox)findViewById(R.id.checkbox_caijing);
        cj.setOnCheckedChangeListener(this);
        kj = (CheckBox)findViewById(R.id.checkbox_keji);
        kj.setOnCheckedChangeListener(this);
        sh = (CheckBox)findViewById(R.id.checkbox_shehui);
        sh.setOnCheckedChangeListener(this);
        ss = (CheckBox)findViewById(R.id.checkbox_shishang);
        ss.setOnCheckedChangeListener(this);
        yl = (CheckBox)findViewById(R.id.checkbox_yule);
        yl.setOnCheckedChangeListener(this);
        gj = (CheckBox)findViewById(R.id.checkbox_guoji);
        gj.setOnCheckedChangeListener(this);
        gn = (CheckBox)findViewById(R.id.checkbox_guonei);
        gn.setOnCheckedChangeListener(this);
        js = (CheckBox)findViewById(R.id.checkbox_junshi);
        js.setOnCheckedChangeListener(this);
        ty = (CheckBox)findViewById(R.id.checkbox_tiyu);
        ty.setOnCheckedChangeListener(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent data = new Intent(this, MainActivity.class);
        data.putStringArrayListExtra("", (ArrayList<String>) categories);
        setResult(RESULT_OK, data);
        finish();
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.checkbox_toutiao:
                if(isChecked) {
                    categories.add(allCategories.get(0));
                }else {
                    categories.remove(allCategories.get(0));
                }
                break;
            case R.id.checkbox_shehui:
                if(isChecked) {
                    categories.add(allCategories.get(1));
                }else {
                    categories.remove(allCategories.get(1));
                }
                break;
            case R.id.checkbox_caijing:
                if(isChecked) {
                    categories.add(allCategories.get(8));
                }else {
                    categories.remove(allCategories.get(8));
                }
                break;
            case R.id.checkbox_keji:
                if(isChecked) {
                    categories.add(allCategories.get(7));
                }else {
                    categories.remove(allCategories.get(7));
                }
                break;
            case R.id.checkbox_guoji:
                if(isChecked) {
                    categories.add(allCategories.get(3));
                }else {
                    categories.remove(allCategories.get(3));
                }
                break;
            case R.id.checkbox_guonei:
                if(isChecked) {
                    categories.add(allCategories.get(2));
                }else {
                    categories.remove(allCategories.get(2));
                }
                break;
            case R.id.checkbox_tiyu:
                if(isChecked) {
                    categories.add(allCategories.get(5));
                }else {
                    categories.remove(allCategories.get(5));
                }
                break;
            case R.id.checkbox_junshi:
                if(isChecked) {
                    categories.add(allCategories.get(6));
                }else {
                    categories.remove(allCategories.get(6));
                }
                break;
            case R.id.checkbox_yule:
                if(isChecked) {
                    categories.add(allCategories.get(4));
                }else {
                    categories.remove(allCategories.get(4));
                }
                break;
            case R.id.checkbox_shishang:
                if(isChecked) {
                    categories.add(allCategories.get(9));
                }else {
                    categories.remove(allCategories.get(9));
                }
                break;
        }

    }
}
