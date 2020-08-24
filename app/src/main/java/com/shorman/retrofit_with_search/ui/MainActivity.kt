package com.shorman.retrofit_with_search.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.lifecycle.Observer
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

        spinner.setSelection(2)

        adapter = CoronaAdapter()
        spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_selectable_list_item,countryList)
        spinner.adapter = spinnerAdapter
        setUpRv()

        svCountry.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getCoronaCases(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        spinner.onItemSelectedListener =object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                svCountry.setQuery(spinnerAdapter.getItem(position).toString(),true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                svCountry.setQuery("amman",true)
            }

        }

        subscribeToObserver()

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

        viewModel.countries.observe(this, Observer {
            when(it){
                is Resuorce.Loading-> progressBar.visibility = View.VISIBLE
                is Resuorce.Success -> {
                    for (i in it.data!!){
                        countryList.add(i.Country)
                    }
                    spinnerAdapter.notifyDataSetChanged()
                }
            }
        })

        viewModel.coronaCases.observe(this, Observer {


            when(it){

                is Resuorce.Loading ->{
                    progressBar.visibility  = View.VISIBLE
                    tvNothingFound.visibility  = View.GONE
                }

                is Resuorce.Success ->{
                    if(it.data?.isEmpty()!!){
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