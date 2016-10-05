package cn.jungu009.mynews;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by jungu009 on 2016/10/5.
 *
 */

public class MyBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {//CoordinatorLayout.Behavior<ListView> {


    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return dependency instanceof RelativeLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {

        ListView listView = (ListView) dependency.findViewById(R.id.list_item);
        Toolbar toolbar = (Toolbar) child.findViewById(R.id.toolbar);
        if(listView.getCount() > 0) {
            View firstItem = listView.getChildAt(0);
            int top = firstItem.getTop();
            toolbar.setTop(top + toolbar.getHeight());
            toolbar.setBottom(top);
        }
        return true;
    }
}
