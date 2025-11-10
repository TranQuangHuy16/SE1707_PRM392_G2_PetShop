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

            // ... (code cũ của bạn để ghép chuỗi địa chỉ) ...
            val addressParts = listOf(address.ward, address.district, address.city)
            val fullAddress = addressParts
                .filter { !it.isNullOrBlank() }
                .joinToString(", ")
            binding.tvCity.text = fullAddress

            binding.tvPostalCode.text = "Postal code: ${address.postalCode}"

            // ... (code cũ của bạn để ẩn/hiện default) ...
            if (address.isDefault) {
                binding.tvIsDefault.text = "🏠 Default Address"
                binding.tvIsDefault.visibility = View.VISIBLE
            } else {
                binding.tvIsDefault.visibility = View.GONE
            }

            // [PHẦN SỬA ĐỔI]
            // Gán click vào layout thông tin, KHÔNG gán vào 'root'
            binding.layoutAddressInfo.setOnClickListener { onItemClick(address) }

            // Nút xóa vẫn như cũ
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
