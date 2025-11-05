package com.example.se1707_prm392_g2_petshop.ui.address.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.se1707_prm392_g2_petshop.R
import com.example.se1707_prm392_g2_petshop.data.adapter.AddressListAdapter
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress
import com.example.se1707_prm392_g2_petshop.data.repositories.UserAddressRepository
import com.example.se1707_prm392_g2_petshop.databinding.ActivityAddressListBinding
import com.example.se1707_prm392_g2_petshop.ui.address.add_edit.AddEditAddressActivity

class AddressListActivity : AppCompatActivity(), AddressListContract.View {

    private lateinit var presenter: AddressListPresenter
    private lateinit var adapter: AddressListAdapter
    private lateinit var binding: ActivityAddressListBinding

    private lateinit var addressResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // =============================================
        // ✅ THÊM NÚT BACK TẠI ĐÂY
        binding.btnBack.setOnClickListener {
            finish()
        }
        // =============================================

        val repo = UserAddressRepository.getInstance(this)
        presenter = AddressListPresenter(this, repo, this)

        // 🔹 Đăng ký nhận kết quả khi Add/Edit xong
        addressResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                presenter.loadAddresses() // 🔄 Load lại danh sách
            }
        }

        setupRecyclerView()
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditAddressActivity::class.java)
            addressResultLauncher.launch(intent)
        }

        presenter.loadAddresses()
    }

    private fun setupRecyclerView() {
        adapter = AddressListAdapter { address ->
            val intent = Intent(this, AddEditAddressActivity::class.java)
            intent.putExtra("address", address)
            addressResultLauncher.launch(intent)
        }
        binding.rvAddresses.layoutManager = LinearLayoutManager(this)
        binding.rvAddresses.adapter = adapter
    }


    override fun showAddresses(addresses: List<UserAddress>) {
        adapter.submitList(addresses)
    }

    override fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
