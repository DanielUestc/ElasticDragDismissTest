package com.hzw.elasticdragdismiss;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by User on 2017/8/2.
 */

public class ElasticActivity extends AppCompatActivity implements ElasticDragDismissView.DragDismissListener{

    Toolbar toolbar;
    ElasticDragDismissView elasticDragDismissView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elastic_activity_layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        elasticDragDismissView=(ElasticDragDismissView)findViewById(R.id.elasticview);
        elasticDragDismissView.registerListener(this);
    }

    @Override
    public void dragDismiss() {
        this.finish();
    }
}
