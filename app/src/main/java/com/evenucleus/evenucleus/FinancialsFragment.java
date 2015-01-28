package com.evenucleus.evenucleus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.evenucleus.client.CategoryRepo;
import com.evenucleus.client.EnrichedJournalEntry;
import com.evenucleus.client.ICategoryRepo;
import com.evenucleus.client.IJournalRepo;
import com.evenucleus.client.JournalRepo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_financials)
public class FinancialsFragment extends android.support.v4.app.Fragment {
    @ViewById(R.id.transactionList)
    ListView transactionList;

    @Bean
    JournalListAdapter adapter;

    @Bean(CategoryRepo.class)
    ICategoryRepo _categoryRepo;

    @Bean(JournalRepo.class)
    IJournalRepo _journalRepo;


    @AfterViews
    public void AfterViews() {
        transactionList.setAdapter(adapter);

        registerForContextMenu(transactionList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select category");

        try {
            List<String> categories = _categoryRepo.Get();
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
            EnrichedJournalEntry journalEntry = ((JournalItemView)info.targetView)._journalEntry;
            _journalRepo.AssignCategory(journalEntry.JournalEntryId, categorySelected);
            journalEntry.Selected = false;
            journalEntry.Category = categorySelected;
            ((JournalItemView)info.targetView).SetCategory(categorySelected, false);

            int c = transactionList.getChildCount();
            for(int i = 0; i < c; ++i) {
                JournalItemView jiw = (JournalItemView) transactionList.getChildAt(i);
                if (jiw.checkBox.isChecked()) {
                    _journalRepo.AssignCategory(jiw._journalEntry.JournalEntryId, item.getTitle().toString());
                    jiw._journalEntry.Selected = false;
                    jiw._journalEntry.Category = categorySelected;
                    jiw.checkBox.setChecked(false);
                    jiw.SetCategory(categorySelected, false);
                }
            }

            for(EnrichedJournalEntry je: adapter._entries)
                if (je.Selected) {
                    je.Selected = false;
                    _journalRepo.AssignCategory(je.JournalEntryId, categorySelected);
                    je.Category = categorySelected;
                }

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
}