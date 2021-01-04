package co.paulfran.ktor_note_app_client.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.paulfran.ktor_note_app_client.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}