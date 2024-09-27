package com.xy.he.qh_sign.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xy.he.qh_sign.R
import com.xy.he.qh_sign.databinding.ItemSignEntryBinding
import com.xy.he.qh_sign.http.EntrySignItem


class EntrySignShortAdapter(
    var list: List<EntrySignItem>
) :
    RecyclerView.Adapter<EntrySignShortAdapter.ViewHolder>() {

    lateinit var onItemLongClickListener: (EntrySignItem) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EntrySignShortAdapter.ViewHolder {
        val binding =
            ItemSignEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EntrySignShortAdapter.ViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.valueSymbol.text = "${this.symbol_name} ${this.symbol}"
                binding.valueDirection.text = if (this.direction == "l") {
                    "多 ${this.price}"

                } else {
                    "空 ${this.price}"
                }
                if (this.direction == "l") {
                    binding.valueDirection.setTextColor(
                        binding.valueDirection.resources.getColor(
                            R.color.long_color
                        )
                    )
                } else if (this.direction == "s") {
                    binding.valueDirection.setTextColor(
                        binding.valueDirection.resources.getColor(
                            R.color.short_color
                        )
                    )
                }

                binding.valuePeriod.text = this.period

                binding.valueTradeStats.text = if (this.trade_result == "止盈") {
                    "止盈 盈亏比${this.trade_plr}"
                } else {
                    this.trade_result ?: "未交易"
                }

                if (this.trade_result == "止盈") {
                    binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.long_color_90))
                } else if (this.trade_result == "止损") {
                    binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.short_color_90))
                } else {
                    binding.root.setBackgroundColor(binding.root.resources.getColor(R.color.white))
                }
                binding.valueTime.text = this.datatime_dt
                binding.valueTradeReason.text = this.mark

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


    inner class ViewHolder(val binding: ItemSignEntryBinding) :
        RecyclerView.ViewHolder(binding.root)


}
