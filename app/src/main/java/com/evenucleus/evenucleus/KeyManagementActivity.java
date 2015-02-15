package com.evenucleus.evenucleus;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@EActivity(R.layout.activity_key_management)
@OptionsMenu(R.menu.menu_key_management)
public class KeyManagementActivity extends MyActivityBase {
    final Logger logger = LoggerFactory.getLogger(KeyManagementActivity.class);

    @ViewById(R.id.keyList)
    ListView keyList;

    @Bean
    KeyListAdapter adapter;

    @AfterViews
    void afterUpdate() {
        logger.debug("afterupdate");
        keyList.setAdapter(adapter);
    }

    @OptionsItem(R.id.add_key)
    void key_management() {
        AddKey_.intent(this).start();
    }

}
