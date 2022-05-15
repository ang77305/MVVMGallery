package example.fiona.pixabay.utils

import android.content.SearchRecentSuggestionsProvider
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal

class MySuggestionProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "example.fiona.pixabay.utils.MySuggestionProvider"
        const val MODE: Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}