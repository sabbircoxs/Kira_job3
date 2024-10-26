package com.example.kirajob3.Adapter_06

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kirajob3.Model_06.User_06
import com.example.kirajob3.databinding.ItemUser06Binding


class UserAdapter_06(private var userList: List<User_06>): RecyclerView.Adapter<UserAdapter_06.UserViewHolder>() {
    class UserViewHolder(private val binding: ItemUser06Binding):RecyclerView.ViewHolder(binding.root){
        fun bind(user: User_06){
            binding.apply {
                binding.displayNameTxt.text = user.displayName
                binding.emailTxt.text = user.email
                binding.locationTxt.text = user.location
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUser06Binding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]

        holder.bind(user)

    }
    fun updateData(newList: List<User_06>) {
        userList = newList
        notifyDataSetChanged()
    }
}