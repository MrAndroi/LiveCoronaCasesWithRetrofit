package com.shorman.retrofit_with_search.ui

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import com.shorman.retrofit_with_search.Adapter.CoronaAdapter
import com.shorman.retrofit_with_search.R
import com.shorman.retrofit_with_search.utility.Resuorce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var adapter: CoronaAdapter
    var countryList = ArrayList<String>()
    lateinit var spinnerAdapter: ArrayAdapter<String>
    val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val animation = constraint.background as AnimationDrawable
        animation.setEnterFadeDuration(2000)
        animation.setExitFadeDuration(2000)
        animation.start()

        adapter = CoronaAdapter()
        spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item,countryList)
        spinner.adapter = spinnerAdapter
        setUpRv()

        svCountry.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.equals(".") || query.equals("..")){
                    Toast.makeText(applicationContext,"you cant user . or .. for search",Toast.LENGTH_SHORT).show()
                }
                else if (query !in countryList){
                    tvNothingFound.visibility = View.VISIBLE
                    rvItems.visibility = View.INVISIBLE
                    tvNothingFound.text = "No such country with name $query \nlook at the spinner in the right corner for all country names"
                    adapter.notifyDataSetChanged()
                }
                else{
                    viewModel.getCoronaCases(query!!)
                    rvItems.visibility = View.VISIBLE
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        subscribeToObserver()

        spinner.onItemSelectedListener =object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                svCountry.setQuery(spinnerAdapter.getItem(position).toString(),true)
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

    }

    private fun subscribeToObserver() {
        viewModel.coronaCases.observe(this,  {
            when(it){

                is Resuorce.Loading -> progressBar.visibility = View.VISIBLE

                is Resuorce.Success -> {
                    progressBar.visibility = View.GONE
                    adapter.differ.submitList(it.data?.asReversed())
                    adapter.notifyDataSetChanged()
                }
                is Resuorce.Error -> {
                    progressBar.visibility = View.GONE
                }

            }
        })

        viewModel.countries.observe(this,  {
            when(it){
                is Resuorce.Loading-> progressBar.visibility = View.VISIBLE
                is Resuorce.Success -> {
                    for (i in it.data!!){
                        countryList.add(i.Country.toLowerCase())
                    }
                    countryList.sort()
                    spinnerAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.coronaCases.observe(this,  {
            when(it){
                is Resuorce.Loading ->{
                    progressBar.visibility  = View.VISIBLE
                    tvNothingFound.visibility  = View.GONE
                }

                is Resuorce.Success ->{
                    if(it.data?.isEmpty()!!){
                        progressBar.visibility = View.GONE
                        tvNothingFound.visibility  = View.VISIBLE
                        tvNothingFound.text = "Nothing found for ${svCountry.query}"
                    }
                    else{
                        tvNothingFound.visibility  = View.GONE
                    }
                }
            }



        })
    }


    private fun setUpRv(){
        rvItems.adapter = adapter
    }
}