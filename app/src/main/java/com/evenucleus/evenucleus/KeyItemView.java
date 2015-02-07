package com.evenucleus.evenucleus;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by tomeks on 2015-01-12.
 */
@EViewGroup(R.layout.key_list_row)
public class KeyItemView extends LinearLayout {
    @ViewById(R.id.keyId)
    TextView keyId;

    @ViewById(R.id.keySublist)
    LinearLayout keySublist;

    @ViewById(R.id.deleteButton)
    ImageButton deleteButton;

    KeyListAdapter adapter;

    Context _context;

    public KeyItemView(Context context) {
        super(context);
        _context = context;
    }

    public void bind(KeyListAdapter.MyKeyInfo info) {
        keyId.setText(info.Key);
        keySublist.removeAllViews();
        for(KeyListAdapter.ItemInfo item: info.Infos) {
            KeySubItemView v = KeySubItemView_.build(getContext());
            v.bind(item);
            keySublist.addView(v);
        }
        deleteButton.setTag(R.string.tag_button, info.KeyId);
    }

    @Click(R.id.deleteButton)
    public void onClick(View clickedView) {
        int key = (Integer) clickedView.getTag(R.string.tag_button);
        adapter.onDeleteKey(key);
    }
}
