package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.apptentive.android.sdk.Apptentive;
import com.evenucleus.client.CategoryRepo;
import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.ICategoryRepo;
import com.evenucleus.client.IJournalRepo;
import com.evenucleus.client.JournalRepo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@EFragment(R.layout.fragment_financials)
public class FinancialsFragment extends android.support.v4.app.Fragment {
    @ViewById(R.id.transactionList)
    ListView mListView;

    @Bean
    JournalListAdapter mAdapter;

    @Bean(CategoryRepo.class)
    ICategoryRepo _categoryRepo;

    @Bean(JournalRepo.class)
    IJournalRepo _journalRepo;


    @AfterViews
    public void AfterViews() {
        mListView.setAdapter(mAdapter);

        registerForContextMenu(mListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select category");

        try {
            List<String> categories = _categoryRepo.Get();
            Collections.sort(categories);
            for(String s: categories)
                menu.add(s);
        }
        catch (Exception e) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }
        menu.add("____");
        menu.add("New category");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();

        String categorySelected = item.getTitle().toString();

        if (categorySelected == "New category")
        {
            Apptentive.engage(getActivity(), "new_category");

            final EditText txtUrl = new EditText(getActivity());
            new AlertDialog.Builder(getActivity())
                    .setTitle("Add category")
                    .setMessage("Category name")
                    .setView(txtUrl)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {
                                _categoryRepo.AddCategory(txtUrl.getText().toString());
                            } catch (Exception e) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Error")
                                        .setMessage(e.toString())
                                        .show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();
            return true;
        }

        try {
            Apptentive.engage(getActivity(), "category_set");

            EnrichedJournalEntry journalEntry = ((JournalItemView)info.targetView)._journalEntry;
            _journalRepo.AssignCategory(journalEntry.RefID, categorySelected);
            journalEntry.Selected = false;
            journalEntry.Category = categorySelected;
            journalEntry.Suggested = false;
            ((JournalItemView)info.targetView).SetCategory(categorySelected, false);

            int c = mListView.getChildCount();
            for(int i = 0; i < c; ++i) {
                JournalItemView jiw = (JournalItemView) mListView.getChildAt(i);
                if (jiw.checkBox.isChecked()) {
                    _journalRepo.AssignCategory(jiw._journalEntry.RefID, item.getTitle().toString());
                    jiw._journalEntry.Selected = false;
                    jiw._journalEntry.Category = categorySelected;
                    jiw._journalEntry.Suggested = false;
                    jiw.checkBox.setChecked(false);
                    jiw.SetCategory(categorySelected, false);
                }
            }

            for(EnrichedJournalEntry je: mAdapter._entries)
                if (je.Selected) {
                    je.Selected = false;
                    je.Suggested = false;
                    _journalRepo.AssignCategory(je.RefID, categorySelected);
                    je.Category = categorySelected;
                }

            //mAdapter.afterInject();
            //mAdapter.notifyDataSetChanged();
            updateAdapterWithAnimation();

            getActivity().sendBroadcast(new Intent(Alarm.CategorySetIntent));

        }
        catch (Exception e)
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage(e.toString())
                    .show();
        }

        return true;
    }

    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
    private static final int MOVE_DURATION = 150;

    /**
     * This method animates all other views in the ListView container (not including removed view)
     * into their final positions. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void updateAdapterWithAnimation() {
        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        for (int i = 0; i < mListView.getChildCount(); ++i) {
            View child = mListView.getChildAt(i);
            int position = firstVisiblePosition + i;
            long itemId = mAdapter.getItemId(position);
            mItemIdTopMap.put(itemId, child.getTop());
        }

        // store ids before removal
        HashSet<Long> before = new HashSet<Long>();
        for(EnrichedJournalEntry j:mAdapter._entries)
            before.add(j.RefID);

        // possibly remove items
        mAdapter.afterInject();
        mAdapter.notifyDataSetChanged();

        // detect removed items & update mItemIdTopMap
            // first we need ids of new items
        HashSet<Long> current = new HashSet<Long>();
        for(EnrichedJournalEntry j: mAdapter._entries) current.add(j.RefID);
            // then we calculate removed
        HashSet<Long> removed = new HashSet<Long>();
        for(Long id:before)
            if (!current.contains(id)) {
                removed.add(id);
                mItemIdTopMap.remove(id);
            }

        final ViewTreeObserver observer = mListView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = mListView.getFirstVisiblePosition();
                for (int i = 0; i < mListView.getChildCount(); ++i) {
                    final View child = mListView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {
//                                        mBackgroundContainer.hideBackground();
//                                        mSwiping = false;
                                        mListView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + mListView.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {
//                                    mBackgroundContainer.hideBackground();
//                                    mSwiping = false;
                                    mListView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

}