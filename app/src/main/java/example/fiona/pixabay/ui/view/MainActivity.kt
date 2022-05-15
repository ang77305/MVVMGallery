package example.fiona.pixabay.ui.view

import android.app.SearchManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.SearchRecentSuggestions
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import example.fiona.pixabay.R
import example.fiona.pixabay.data.model.PixabayHitsData
import example.fiona.pixabay.databinding.ActivityMainBinding
import example.fiona.pixabay.ui.adapter.CellClickListener
import example.fiona.pixabay.ui.adapter.ImagesAdapter
import example.fiona.pixabay.ui.adapter.TagClickListener
import example.fiona.pixabay.ui.viewmodel.MainViewModel
import example.fiona.pixabay.utils.MySuggestionProvider
import example.fiona.pixabay.utils.SearchRecentSuggestionsLimited
import example.fiona.pixabay.utils.Status
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity :
    AppCompatActivity(),
    CellClickListener,
    TagClickListener {

    private val TAG = "MainActivity"
    private lateinit var databinding: ActivityMainBinding
    private lateinit var adapter: ImagesAdapter
    private var isLoading = false

    val adapterList = mutableListOf<PixabayHitsData>()

    lateinit var mainViewModel: MainViewModel
    fun View.hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databinding = setContentView(this, R.layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()

        databinding.imageViewModel = mainViewModel
        databinding.lifecycleOwner = this

    }

    private fun hideSoftKeyboard() {
        Handler().postDelayed({
            search_view.hideKeyboard()
        }, 100)
    }

    private fun updateList(imagess: List<PixabayHitsData>) {
        adapterList.clear()
        adapterList.addAll(imagess)
        adapter.updateList(imagess as ArrayList<PixabayHitsData>)
        adapter.notifyDataSetChanged()
    }

    private fun setupUI() {
        adapter = ImagesAdapter(this, adapterList as ArrayList<PixabayHitsData>, this, this)

        recyclerView.adapter = adapter
        recyclerView.setItemViewCacheSize(100)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        initScrollListener()

        val manager = getSystemService(SEARCH_SERVICE) as SearchManager
        val suggestions = SearchRecentSuggestionsLimited(
            this@MainActivity,
            MySuggestionProvider.AUTHORITY,
            MySuggestionProvider.MODE, 5
        )
        search_view.setSearchableInfo(manager.getSearchableInfo(componentName))
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                suggestions.saveRecentQuery(query, null)

                mainViewModel.fetchImages(query.toString(), true)
                hideSoftKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainViewModel.searchWord.set(newText)

                return true
            }

        })
        search_view.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                Log.d(TAG, "onSuggestionSelect")

                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                Log.d(TAG, "onSuggestionClick")

                val cursor: Cursor = search_view.getSuggestionsAdapter().getCursor()
                cursor.moveToPosition(position)
                val suggestion: String =
                    cursor.getString(2)
                search_view.setQuery(suggestion, true)

                return true
            }

        })

    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
    }

    private fun initScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.layoutManager is LinearLayoutManager) {
                    val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (!isLoading) {
                        if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 5
                        ) {
                            mainViewModel.loadMore(search_view.query.toString())
                            isLoading = true
                        }
                    }
                } else {

                    val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
                    if (!isLoading) {
                        if (gridLayoutManager != null &&
                            gridLayoutManager.findLastCompletelyVisibleItemPosition() == recyclerView.adapter!!.itemCount - 5
                        ) {
                            mainViewModel.loadMore(search_view.query.toString())
                            isLoading = true
                        }
                    }
                }
            }
        })
    }

    private fun setupObservers() {
        mainViewModel.getImages().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    isLoading = false
                    progressBar.visibility = View.GONE
                    it.data?.let { pixabayData -> updateList(pixabayData) }
                    hideSoftKeyboard()
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    isLoading = false
                    progressBar.visibility = View.GONE
                    showSnack(content, it.message.toString())
                }
            }
        })

        mainViewModel.getPageStatus().observe(this, Observer {
            adapterList.clear()
        })

        mainViewModel.getListStatus().observe(this, Observer {
            if (it) {
                ll_noResults.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            } else {
                ll_noResults.visibility = View.GONE
            }
        })

        mainViewModel.getDisplayType().observe(this) {
            if (!it) {
                iv_list.setImageResource(R.drawable.ic_list)
                recyclerView.layoutManager = LinearLayoutManager(this)
            } else {

                iv_list.setImageResource(R.drawable.ic_grid)
                recyclerView.layoutManager = GridLayoutManager(this, 2)
            }
            initScrollListener()
            recyclerView.adapter = adapter

        }
    }

    override fun onCellClickListener(image: PixabayHitsData) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setMessage("Do you want to see the details?")
        alertDialog.setPositiveButton("Yes") { dialog, id ->
            val dialogFragment = ImageDetailsDialog(image)
            dialogFragment.show(supportFragmentManager, "imageDetails")
        }
        alertDialog.setNegativeButton("Cancel") { dialog, id ->
        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun showSnack(view: View, message: String) {
        val snackbar = Snackbar.make(
            view, message,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("retry") {
            mainViewModel.fetchImages(search_view.query.toString(), false)
            hideSoftKeyboard()
        }
        val snackbarView = snackbar.view
        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.textSize = 14f
        snackbar.show()
    }

    override fun onTagClickListener(tag: String) {
        search_view.setQuery(tag, true)
    }


}