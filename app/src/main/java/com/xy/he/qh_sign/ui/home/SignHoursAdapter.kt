package com.xy.he.qh_sign.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.RecyclerView
import com.xy.he.qh_sign.R
import com.xy.he.qh_sign.databinding.ItemSignHoursBinding
import com.xy.he.qh_sign.http.SignItemData
import java.math.RoundingMode


class SignHoursAdapter(
    var list: List<SignItemData>
) :
    RecyclerView.Adapter<SignHoursAdapter.ViewHolder>() {

    lateinit var onItemLongClickListener: (SignItemData) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignHoursAdapter.ViewHolder {
        val binding =
            ItemSignHoursBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SignHoursAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.valueSymbol.text = "${this.symbol_label} ${this.symbol}"
                binding.valueDirection.text = this.direction
                if (this.direction == "多") {
                    binding.valueDirection.setTextColor(
                        binding.valueDirection.resources.getColor(
                            R.color.long_color
                        )
                    )
                } else if (this.direction == "空") {
                    binding.valueDirection.setTextColor(
                        binding.valueDirection.resources.getColor(
                            R.color.short_color
                        )
                    )
                }

                binding.valuePeriod.text = this.period_label

                binding.valuePrice.text = "高:${this.high}|低:${this.low}|50%:${
                    (this.low + (this.high - this.low) * 0.5).toBigDecimal()
                        .setScale(2, RoundingMode.HALF_UP).toDouble()
                }"

                binding.valueSubscribeStats.text = if (this.is_subscribe) {
                    "监听中"
                } else {
                    "未监听"
                }

                binding.valueTime.text = this.datetime

                if (this.is_subscribe) {
                    binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.long_color_90))
                } else {
                    binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.white))
                }
            }

            itemView.setOnLongClickListener {
                onItemLongClickListener.invoke(list[position])
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class ViewHolder(val binding: ItemSignHoursBinding) :
        RecyclerView.ViewHolder(binding.root)


}
