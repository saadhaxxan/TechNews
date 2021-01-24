package com.example.technews

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        progressBar.visibility = View.VISIBLE
        val url = "https://newsapi.org/v2/everything?q=machinelearning&language=en&apiKey=YOUR_API_KEY"
        val jsonObjectRequest = object:JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                {
                    val newsJsonArray = it.getJSONArray("articles")
                    val newsArray = ArrayList<News>()
                    for(i in 0 until newsJsonArray.length()) {
                        val newsJsonObject = newsJsonArray.getJSONObject(i)
                        val news = News(
                                newsJsonObject.getString("title"),
                                newsJsonObject.getString("author"),
                                newsJsonObject.getString("url"),
                                newsJsonObject.getString("urlToImage")
                        )
                        newsArray.add(news)
                    }

                    mAdapter.updateNews(newsArray)
                    progressBar.visibility = View.GONE
                },
                {
                    Toast.makeText(this,"hiii",Toast.LENGTH_LONG).show()
                }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }   }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    var jsonObjectRequest: Any
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }

    fun JsonObjectRequest(
            get: Int,
            url: String,
            nothing: Nothing?,
            function: () -> Unit,
            function1: () -> Unit,
            function2: () -> Unit
    ): Any {
        TODO("Not yet implemented")
    }
}