package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room

// Экран для вывода, добавления, удаления и редактирования данных из базы ROOM
class ShowInfoFragment : Fragment() {

    private lateinit var db: AppDatabase
    private lateinit var editName: EditText
    private lateinit var editHeight: EditText
    private lateinit var editWeight: EditText
    private lateinit var listView: ListView

    private var currentPokemon: PokemonEntity? = null
    private var pokemonList: List<PokemonEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализируем базу данных ROOM
        db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "pokemon_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        // Привязываем элементы из XML
        editName = view.findViewById(R.id.editName)
        editHeight = view.findViewById(R.id.editHeight)
        editWeight = view.findViewById(R.id.editWeight)
        listView = view.findViewById(R.id.listView)

        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val btnDel = view.findViewById<Button>(R.id.btnDel)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        // Получаем данные, переданные из InputFragment
        val pokemonName = arguments?.getString("pokemonName") ?: ""
        val pokemonHeight = arguments?.getInt("pokemonHeight") ?: 0
        val pokemonWeight = arguments?.getInt("pokemonWeight") ?: 0

        // Заполняем поля этими данными
        editName.setText(pokemonName)
        editHeight.setText(pokemonHeight.toString())
        editWeight.setText(pokemonWeight.toString())

        // Загружаем список из базы
        updateList()

        listView.setOnItemClickListener { parent, view, position, id ->
            val selected = pokemonList[position]
            currentPokemon = selected
            editName.setText(selected.name)
            editHeight.setText(selected.height.toString())
            editWeight.setText(selected.weight.toString())
            Toast.makeText(requireContext(), "Выбран ID: ${selected.id}", Toast.LENGTH_SHORT).show()
        }

        btnAdd.setOnClickListener {
            val name = editName.text.toString()
            val height = editHeight.text.toString().toIntOrNull() ?: 0
            val weight = editWeight.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty()) {
                db.pokemonDao().insert(PokemonEntity(name = name, height = height, weight = weight))
                updateList()
                Toast.makeText(requireContext(), "Добавлено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Введите имя", Toast.LENGTH_SHORT).show()
            }
        }

        btnDel.setOnClickListener {
            if (currentPokemon != null) {
                db.pokemonDao().delete(currentPokemon!!)
                currentPokemon = null
                updateList()
                Toast.makeText(requireContext(), "Удалено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Выберите покемона из списка", Toast.LENGTH_SHORT).show()
            }
        }

        btnEdit.setOnClickListener {
            if (currentPokemon != null) {
                Toast.makeText(requireContext(), "Измените поля и нажмите Сохранить", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Выберите покемона из списка", Toast.LENGTH_SHORT).show()
            }
        }

        btnSave.setOnClickListener {
            if (currentPokemon != null) {
                currentPokemon!!.name = editName.text.toString()
                currentPokemon!!.height = editHeight.text.toString().toIntOrNull() ?: 0
                currentPokemon!!.weight = editWeight.text.toString().toIntOrNull() ?: 0

                db.pokemonDao().update(currentPokemon!!)
                currentPokemon = null
                updateList()
                Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Сначала выберите покемона из списка", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, InputFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    // Метод для обновления списка на экране
    private fun updateList() {
        // Получаем все записи из базы
        pokemonList = db.pokemonDao().getAll()

        // Формируем строки для отображения в ListView
        val displayList = pokemonList.map {
            "ID: ${it.id} | ${it.name} | Рост: ${it.height} | Вес: ${it.weight}"
        }

        // Устанавливаем адаптер для ListView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, displayList)
        listView.adapter = adapter
    }
}