package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

// Главный экран приложения, который загружается при запуске
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Запускаем экран авторизации при старте приложения
        fragmentTransaction.replace(R.id.fragment_container, LoginFragment())
        fragmentTransaction.commit()

    }
}