package com.fithun.ui.bottomTab

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.fithun.R
import com.fithun.api.responses.BrandListResult
import com.fithun.api.responses.ProductListDocs
import com.fithun.clickInterfaces.AddProductCart
import com.fithun.clickInterfaces.CommonClick
import com.fithun.clickInterfaces.ProductFilterInterface
import com.fithun.databinding.FragmentProductTabBinding
import com.fithun.ui.adapter.BannerAdapter
import com.fithun.ui.adapter.BrandsAdapter
import com.fithun.ui.adapter.ProductCatalogueAdapter
import com.fithun.ui.bottomSheet.ProductFilter
import com.fithun.uiModalClass.BannerImages
import com.fithun.utils.AndroidExtension
import com.fithun.utils.AndroidExtension.initLoader
import com.fithun.utils.DialogUtils
import com.fithun.utils.ManageStack
import com.fithun.utils.Progress
import com.fithun.utils.Resource
import com.fithun.utils.SavedPrefManager
import com.fithun.utils.displayToast
import com.fithun.utils.setSafeOnClickListener
import com.fithun.viewModel.ProductFlowViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductTabFragment : Fragment(), AddProductCart, ProductFilterInterface {


    private var _binding: FragmentProductTabBinding? =  null
    private val binding get() = _binding!!
    var brandId= ArrayList<String>()


    private val sliderHandler: Handler = Handler()


    private var  brandListData:List<BrandListResult> = listOf()
    private val viewModel: ProductFlowViewModel by viewModels()
    var token = ""
    var data :List<ProductListDocs> = listOf()
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true
    lateinit var adapter : ProductCatalogueAdapter



    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    lateinit var adapterBrand: BrandsAdapter


    private lateinit var brandsNameNew:TextView
    var priceValuesRequest = ""

    private var stopLoadingProgress: LinearLayout? = null
    private var startLoading: RelativeLayout? = null
    private var loaderAnimation: LottieAnimationView? = null


    companion object{
        @JvmStatic
        fun newInstance() = ProductTabFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentProductTabBinding.inflate(layoutInflater, container, false)

        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.AccessToken).toString()
        viewModel.productListApi(token=token, page=page,limit=limit, search="",brandId=brandId, priceRange = priceValuesRequest)
        viewModel.brandListApi(token=token)


        binding.swipeRefresh.setOnRefreshListener {
            pages = 0
            page = 1
            limit = 10
            dataLoadFlag = false
            loaderFlag = true
            viewModel.productListApi(token=token, page=page,limit=limit, search="",brandId=brandId, priceRange = priceValuesRequest)
            viewModel.brandListApi(token=token)
            binding.swipeRefresh.isRefreshing= false
        }





        binding.etSearch.addTextChangedListener(searchProduct)



        binding.filter.setSafeOnClickListener {
            val bottomSheetFragment = ProductFilter(this,brandListData)
            bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)

        }



        binding.scrollViewPost.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {

                dataLoadFlag = true
                page++
                binding.ProgressBarScroll.visibility = View.VISIBLE
                if (page > pages) {
                    binding.ProgressBarScroll.visibility = View.GONE
                } else {
                    viewModel.productListApi(token=token, page=page,limit=limit, search="",brandId=brandId, priceRange = priceValuesRequest)
                }
            }
        })


        val sliderRunnable = Runnable {
            lifecycleScope.launch {
                val currentItem = binding.storeViewpager.currentItem
                val lastItem = binding.storeViewpager.adapter?.itemCount?.minus(1) ?: 0


                if (currentItem == lastItem) {
                    binding.storeViewpager.currentItem = 0
                } else {
                    binding.storeViewpager.currentItem = currentItem + 1
                }
            }
        }




        val callback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 2000)
            }
        }
        binding.storeViewpager.registerOnPageChangeCallback(callback)


        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productResponseObserver()
        productAddToCartResponseObserver()
        brandListResponseObserver()
        updateCartObserver()
    }

    private fun setBannerImageAdaptor() {
        val bannerAdapter = BannerAdapter(requireContext(), BannerImages.data)
        binding.storeViewpager.adapter = bannerAdapter
        binding.indicator.setViewPager(binding.storeViewpager)

    }
    private fun setCatalogueAdaptor() {
        binding.productCatalogue.layoutManager = GridLayoutManager(requireContext(),2)
        adapter = ProductCatalogueAdapter(requireContext(),data, productClick = this)
        binding.productCatalogue.adapter = adapter

    }



    private fun productResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.productListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            if (loaderFlag){
                                Progress.start(requireContext())
                            }
                        }

                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {

                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        data = emptyList()
                                    }
                                    if (response.data.result.docs.isNotEmpty()) {
                                        binding.productCatalogue.isVisible = true
                                        binding.NotFound.isVisible = false
                                        data =  data + response.data.result.docs
                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setCatalogueAdaptor()
                                    } else {
                                        binding.productCatalogue.isVisible = false
                                        binding.NotFound.isVisible = true

                                    }

                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()

                        }

                        is Resource.Empty -> {

                        }

                    }

                }

            }
        }
    }


    private val searchProduct = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            loaderFlag = false
            dataLoadFlag = false
            page = 1
            limit = 10
            viewModel.productListApi(token=token, page=1,limit=10, search=s.toString(),brandId=brandId, priceRange = priceValuesRequest)
        }
    }

    override fun addToCartProduct(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: Int
    ) {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun addToCartProductTab(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: Int
    ) {
//        buyNowClick.isVisible = true
//        incDesign.isVisible = false
        data[position].quantity = 1
        adapter.notifyDataSetChanged()

        viewModel.addToCartApi(token=token,productId=productId, sizeValueId=productSizeValue, quantity="$quantity")
    }

    override fun incrementItem(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: String,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout,
        loader: LottieAnimationView
    ) {
        stopLoadingProgress= stopLoading
        startLoading =  loaderLl
        loaderAnimation = loader
        viewModel.updateQuantityApi(
            token = token,
            productId = productId,
            sizeValueId = position,
            quantity = quantity,
            quantityType = "increment"
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun decrementItem(
        productId: String,
        productSizeValue: String,
        quantity: Int,
        buyNowClick: RelativeLayout,
        incDesign: LinearLayout,
        position: String,
        stopLoading: LinearLayout,
        loaderLl: RelativeLayout,
        loader: LottieAnimationView
    ) {


        stopLoadingProgress= stopLoading
        startLoading =  loaderLl
        loaderAnimation = loader
        viewModel.updateQuantityApi(
            token = token,
            productId = productId,
            sizeValueId = position,
            quantity = quantity,
            quantityType = "decrement"
        )
    }



    private fun productAddToCartResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.addToCartResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {
                            Progress.start(requireContext())
                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.statusCode == 200) {
                                    displayToast(response.data.responseMessage)
//                                    startActivity(
//                                        Intent(requireContext(),
//                                        ProductCartActivity::class.java)
//                                    )
                                }


                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            Progress.stop()
                            response.message?.let {
                                AndroidExtension.alertBox(it, requireContext())
                            }

                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }

    private fun brandListResponseObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.brandListResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {


                        }


                        is Resource.Success -> {
                            try {
                                Progress.stop()
                                if (response.data?.responseCode == 200) {
                                    brandListData = response.data.result
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {


                        }


                        is Resource.Empty -> {


                        }


                    }


                }

            }
        }
    }

    override fun openBrands(brandsName: TextView) {
        brandsNameNew =  brandsName
        brandId.clear()
        priceValuesRequest = ""
        openPopUp()
    }

    override fun applyFilter(priceValues: String) {
        val brandList = brandListData.filter { it.isSelected }

        for (i in brandList.indices){
            brandId.add(brandList[i].id)
        }
        priceValuesRequest = when (priceValues) {
            "select price" -> {
                ""
            }
            "Low to high" -> {
                "lowToHigh"
            }
            else -> {
                "highToLow"
            }
        }
        page = 1
        limit = 10
        loaderFlag = true
        dataLoadFlag = false

        viewModel.productListApi(token=token, page=page,limit=limit, search="",brandId=brandId, priceRange = priceValuesRequest)
        brandListData.forEach { it.isSelected = false }
    }


    private fun openPopUp() {
        try {
            val binding = LayoutInflater.from(requireContext()).inflate(R.layout.state_city_popup, null)
            dialog = DialogUtils().createDialog(requireContext(), binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())


            val dialogTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialogBackButton = binding.findViewById<ImageView>(R.id.BackButton)
            val tick = binding.findViewById<LinearLayout>(R.id.tick)
            dialogBackButton.setOnClickListener { dialog.dismiss() }
            tick.isVisible = true
            dialogTitle.text  =  "Brands"
            val searchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)
            searchEditText.addTextChangedListener(textWatchers)


            setAdapterBrands()

            tick.setOnClickListener{
                val brandListData = brandListData.filter { it.isSelected }

                val formattedBrandsName = brandListData.joinToString(", ") { it.brandName }
                brandsNameNew.text =  formattedBrandsName
                dialog.dismiss()

            }






            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




    private fun setAdapterBrands() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterBrand = BrandsAdapter(requireContext(), brandListData)
        recyclerView.adapter = adapterBrand
    }


    private val textWatchers = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            filterData(s.toString())

        }
    }


    private fun filterData(searchText: String) {
        val filteredList = brandListData.filter { item ->
            try {
                item.brandName.contains(searchText, ignoreCase = true)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        adapterBrand.filterList(filteredList as ArrayList<BrandListResult>)
    }

    override fun onResume() {
        super.onResume()
        setBannerImageAdaptor()
    }


    private fun updateCartObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.updateCartQtyResponseData.collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }


                        is Resource.Success -> {
                            try {

                                if (response.data?.statusCode == 200) {
                                    startLoading!!.isVisible = false
                                    stopLoadingProgress!!.isVisible = true
                                    loaderAnimation!!.initLoader(false)
                                }

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        is Resource.Error -> {
                            startLoading!!.isVisible = false
                            stopLoadingProgress!!.isVisible = true
                            loaderAnimation!!.initLoader(false)
                        }


                        is Resource.Empty -> {

                        }


                    }


                }

            }
        }
    }



}