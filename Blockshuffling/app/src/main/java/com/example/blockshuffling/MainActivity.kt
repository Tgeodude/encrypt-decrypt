package com.example.blockshuffling

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.example.blockshuffling.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val permutation = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.decrypt.setOnClickListener {
            if (binding.enterText.text.isNullOrEmpty() || binding.enterKey.text.isNullOrEmpty()) {
                Toast.makeText(application, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                binding.enterKey.text.map {
                    permutation.add(it.digitToInt())
                }
                binding.result.text = blockPermutationDecrypt(binding.enterText.text.toString(), permutation)
                binding.enterText.text.clear()
                binding.enterKey.text.clear()
                permutation.clear()
            }
        }

        binding.encrypto.setOnClickListener {
            if (binding.enterText.text.isNullOrEmpty() || binding.enterKey.text.isNullOrEmpty()) {
                Toast.makeText(application, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                binding.enterKey.text.map {
                    permutation.add(it.digitToInt())
                }
                binding.result.text = blockPermutationEncrypt(binding.enterText.text.toString(), permutation)
                binding.enterText.text.clear()
                binding.enterKey.text.clear()
                permutation.clear()
            }
        }

        binding.copy.setOnClickListener {
            if (binding.result.text.isNullOrEmpty() || binding.result.text == getString(R.string.result)) {
                Toast.makeText(application, "Пока ничего не заполнено", Toast.LENGTH_SHORT).show()
            } else {
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Текст для копирования", binding.result.text.toString())
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(application, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun blockPermutationEncrypt(plaintext: String, permutation: List<Int>): String {
        val blockSize = permutation.size
        var ciphertext = ""
        if (plaintext.length % blockSize != 0) {
            Toast.makeText(application, "Длина текста не кратна размеру блока!", Toast.LENGTH_SHORT).show()
            return ""
        }

        val numBlocks = plaintext.length / blockSize

        for (i in 0 until numBlocks) {
            for (j in 0 until blockSize) {
                ciphertext += plaintext[i * blockSize + permutation[j] - 1]
            }
        }

        return ciphertext
    }

    private fun blockPermutationDecrypt(ciphertext: String, permutation: List<Int>): String {
        val blockSize = permutation.size
        var plaintext = ""
        if (ciphertext.length % blockSize != 0) {
            Toast.makeText(application, "Длина текста не кратна размеру блока!", Toast.LENGTH_SHORT).show()
            return ""
        }

        val numBlocks = ciphertext.length / blockSize

        for (i in 0 until numBlocks) {
            for (j in 0 until blockSize) {
                for (k in 0 until blockSize) {
                    if (permutation[k] == j + 1) {
                        plaintext += ciphertext[i * blockSize + k]
                        break
                    }
                }
            }
        }

        return plaintext
    }
}