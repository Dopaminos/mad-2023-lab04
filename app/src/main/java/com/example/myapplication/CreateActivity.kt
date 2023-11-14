package com.example.myapplication
    import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.util.UUID

class CreateActivity : AppCompatActivity() {

    private lateinit var questionEditText: EditText
    private lateinit var exampleEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var translateEditText: EditText
    private lateinit var imageInput: ImageView

    private val GALLERY_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        questionEditText = findViewById(R.id.questionEditText)
        exampleEditText = findViewById(R.id.exampleEditText)
        answerEditText = findViewById(R.id.answerEditText)
        translateEditText = findViewById(R.id.translateEditText)
        imageInput = findViewById(R.id.imageInput)

        imageInput.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
        val saveButton: Button = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            saveCard()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val selectedImage: Uri? = data.data
            imageInput.setImageURI(selectedImage)
            imageInput.tag = selectedImage
        }
    }

    private fun saveCard() {
        val question = questionEditText.text.toString().trim()
        val example = exampleEditText.text.toString().trim()
        val answer = answerEditText.text.toString().trim()
        val translate = translateEditText.text.toString().trim()

        if (question.isEmpty() || example.isEmpty() || answer.isEmpty() || translate.isEmpty()) {
            showAlertDialog("Please fill in all fields")
        } else if (imageInput.drawable == null) {
            showAlertDialog("Please select an image")
        } else {
            try {
                val sharedPreferences = getSharedPreferences("CardData", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val cardId = UUID.randomUUID().toString() // генерация id
                val imageUri = imageInput.tag as? Uri
                val card = Card(cardId, question, example, answer, translate, imageUri.toString())
                editor.putString(cardId, Gson().toJson(card))
                editor.apply()

                Toast.makeText(this, "Card saved successfully", Toast.LENGTH_SHORT).show()

                Log.d("CreateActivity", "Created Card: ${Gson().toJson(card)}")
            } catch (e: Exception) {
                showAlertDialog("Error saving card: ${e.message}")
            }
        }
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
}