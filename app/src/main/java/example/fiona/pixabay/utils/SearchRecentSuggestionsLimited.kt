package example.fiona.pixabay.utils

import android.content.ContentResolver
import android.content.Context
import android.provider.SearchRecentSuggestions



class SearchRecentSuggestionsLimited(
    context: Context?,
    authority: String?,
    mode: Int,
    private val limit: Int
) :
    SearchRecentSuggestions(context, authority, mode) {
    override fun truncateHistory(cr: ContentResolver, maxEntries: Int) {
        super.truncateHistory(cr, limit)
    }



}
