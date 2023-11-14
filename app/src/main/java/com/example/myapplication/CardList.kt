package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class CardList : AppCompatActivity() {
    private lateinit var cardAdapter: CardAdapter
    private lateinit var cardList: MutableList<Card>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        cardList = loadCardData()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(cardList)
        recyclerView.adapter = cardAdapter
    }

    private fun loadCardData(): MutableList<Card> {
        val sharedPreferences = getSharedPreferences("CardData", Context.MODE_PRIVATE)
        val cardData = sharedPreferences.all.values
        val cardList = mutableListOf<Card>()

        for (cardJson in cardData) {
            val card = Gson().fromJson(cardJson.toString(), Card::class.java)
            cardList.add(card)
        }

        return cardList
    }

    inner class CardAdapter(private val cardList: List<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            return CardViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            val currentCard = cardList[position]

            currentCard.imageUri?.let { uriString ->
                holder.cardImage.setImageURI(Uri.parse(uriString))
            }
            holder.cardImage.setImageResource(R.drawable.ic_launcher_background) // Set a default or dummy image if the URI is null

            holder.originalWordText.text = currentCard.question
            holder.translationText.text = currentCard.translate
        }


        override fun getItemCount(): Int {
            return cardList.size
        }

        inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val cardImage: ImageView = itemView.findViewById(R.id.cardImage)
            val originalWordText: TextView = itemView.findViewById(R.id.originalWordText)
            val translationText: TextView = itemView.findViewById(R.id.translationText)
            private val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)

            init {
                deleteButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        deleteCard(position)
                    }
                }
            }
        }
    }

    fun deleteCard(position: Int) {
        val card = cardList[position]

        val sharedPreferences = getSharedPreferences("CardData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(card.id)
        editor.apply()

        cardList.removeAt(position)
        cardAdapter.notifyItemRemoved(position)
    }
}