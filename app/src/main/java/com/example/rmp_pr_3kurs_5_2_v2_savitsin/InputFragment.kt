package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

// Экран для ввода имени/ID покемона и запроса к API
class InputFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var nameOrId: EditText
    private lateinit var buttonSearch: Button
    private lateinit var buttonBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Кнопка поиска покемона
        buttonSearch = view.findViewById(R.id.btnSearch)
        buttonSearch.setOnClickListener {
            handler(view)
        }

        // Кнопка возврата на экран логина
        buttonBack = view.findViewById(R.id.btnBack)
        buttonBack.setOnClickListener {
            goBack(view)
        }
    }

    fun handler(view: View) {
        nameOrId = view.findViewById(R.id.nameOrIdInput)

        // Если поле не пустое, делаем запрос к API
        if (nameOrId.text.toString().isNotEmpty()) {
            var url = "https://pokeapi.co/api/v2/pokemon/${nameOrId.text}"
            val queue = Volley.newRequestQueue(requireContext())
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    try {
                        //Десериализируем ответ от сервера
                        val obj = JSONObject(response)
                        val name = obj.getString("name")
                        val height = obj.getInt("height")
                        val weight = obj.getInt("weight")

                        // Передаем данные на экран ShowInfoFragment через Bundle
                        val fragmentManager: FragmentManager = parentFragmentManager
                        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                        val showInfoFragment = ShowInfoFragment()
                        val bundle = Bundle()
                        bundle.putString("pokemonName", name)
                        bundle.putInt("pokemonHeight", height)
                        bundle.putInt("pokemonWeight", weight)
                        showInfoFragment.arguments = bundle

                        // Открываем второй экран
                        fragmentTransaction.replace(R.id.fragment_container, showInfoFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    } catch (e: Exception) {
                        Log.d("MyLog", "JSON Parsing error: ${e.message}")
                    }
                },
                {
                    // Если покемон не найден
                    val alert = AlertDialog.Builder(requireContext())
                        .setTitle("Ошибка")
                        .setMessage("Покемон не найден. Проверьте имя или ID.")
                        .setPositiveButton("OK", null)
                        .create()
                        .show()
                    Log.d("MyLog", "Volley error: $it")
                }
            )
            queue.add(stringRequest)
        } else {
            // Если поле пустое
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Введите название или id персонажа")
                .setPositiveButton("OK", null)
                .create()
                .show()
        }
    }

    fun goBack(view: View) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Возвращаемся на экран авторизации
        fragmentTransaction.replace(R.id.fragment_container, LoginFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}