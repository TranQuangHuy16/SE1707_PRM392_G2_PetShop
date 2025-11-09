package com.example.se1707_prm392_g2_petshop.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.se1707_prm392_g2_petshop.databinding.ItemAddressBinding
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress

class AddressListAdapter(
    private val onItemClick: (UserAddress) -> Unit,
    private val onDeleteClick: (UserAddress) -> Unit
) : ListAdapter<UserAddress, AddressListAdapter.AddressViewHolder>(AddressDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = ItemAddressBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ================================================================
    // BÊN TRONG CLASS AddressViewHolder
    // ================================================================
    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // THAY THẾ TOÀN BỘ HÀM BIND BẰNG HÀM NÀY
        fun bind(address: UserAddress) {
            binding.tvAddressLine.text = address.addressLine
            binding.tvPostalCode.text = "Postal code: ${address.postalCode}"

            // === 1. GHÉP CHUỖI ĐỊA CHỈ THÔNG MINH ===
            // Tạo một danh sách các phần của địa chỉ
            val addressParts = listOf(address.ward, address.district, address.city)

            // Lọc bỏ các phần bị rỗng hoặc null, sau đó ghép lại bằng ", "
            val fullAddress = addressParts
                .filter { !it.isNullOrBlank() }
                .joinToString(", ")

            binding.tvCity.text = fullAddress

            // === 2. ẨN/HIỆN MỤC "DEFAULT ADDRESS" ===
            if (address.isDefault) {
                binding.tvIsDefault.text = "🏠 Default Address"
                binding.tvIsDefault.visibility = View.VISIBLE // Hiện
            } else {
                binding.tvIsDefault.visibility = View.GONE // Ẩn hoàn toàn
            }

            binding.root.setOnClickListener { onItemClick(address) }
            binding.btnDeleteAddress.setOnClickListener { onDeleteClick(address) }
        }
    }
    // ================================================================

    class AddressDiffCallback : DiffUtil.ItemCallback<UserAddress>() {
        override fun areItemsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean {
            return oldItem.addressId == newItem.addressId
        }

        override fun areContentsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean {
            return oldItem == newItem
        }
    }
}
