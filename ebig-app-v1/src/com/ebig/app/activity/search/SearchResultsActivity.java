package com.ebig.app.activity.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import com.ebig.app.base.BaseFirmActivity;

public class SearchResultsActivity extends BaseFirmActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
      //  ...
		super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
      //  ...
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //通过某种方法，根据请求检索你的数据
            getToast().makeText(this, "通过某种方法，根据请求检索你的数据"+query, null);
            
            // Record the query string in the recent queries suggestions provider.
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, 
                    SearchSuggestionSampleProvider.AUTHORITY, SearchSuggestionSampleProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            
        }
        
//        // get and process search query here
//        final Intent queryIntent = getIntent();
//        final String queryAction = queryIntent.getAction();
//        if (Intent.ACTION_SEARCH.equals(queryAction)) { 
//            getToast().makeText(this, "doSearchQuery(queryIntent, onCreate())", null);
//        }
//        else {
//            getToast().makeText(this, "onCreate(), but no ACTION_SEARCH intent", null);
//        }
    }
  //  ...
}
