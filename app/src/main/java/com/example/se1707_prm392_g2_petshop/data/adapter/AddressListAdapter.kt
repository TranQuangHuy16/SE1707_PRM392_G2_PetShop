package com.example.se1707_prm392_g2_petshop.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.se1707_prm392_g2_petshop.databinding.ItemAddressBinding
import com.example.se1707_prm392_g2_petshop.data.models.UserAddress

class AddressListAdapter(
    private val onItemClick: (UserAddress) -> Unit
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

    inner class AddressViewHolder(private val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(address: UserAddress) {
            binding.tvAddressLine.text = address.addressLine
            binding.tvCity.text = "${address.ward}, ${address.district}, ${address.city}"
            binding.tvPostalCode.text = "Postal code: ${address.postalCode}"
            binding.tvIsDefault.text =
                if (address.isDefault) "🏠 Default Address" else ""

            binding.root.setOnClickListener { onItemClick(address) }
        }
    }

    class AddressDiffCallback : DiffUtil.ItemCallback<UserAddress>() {
        override fun areItemsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean {
            return oldItem.addressId == newItem.addressId
        }

        override fun areContentsTheSame(oldItem: UserAddress, newItem: UserAddress): Boolean {
            return oldItem == newItem
        }
    }
}
