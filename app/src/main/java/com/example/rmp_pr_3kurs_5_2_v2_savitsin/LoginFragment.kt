package com.example.rmp_pr_3kurs_5_2_v2_savitsin

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
// Экран авторизации
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var login: EditText
    private lateinit var password: EditText
    private lateinit var buttonLogin: Button

    private lateinit var preferences: SharedPreferences
    private lateinit var ed: SharedPreferences.Editor

    var PREFS_FILE = "Account"
    var PREF_NAME = "Name"

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Кнопка входа
        buttonLogin = view.findViewById(R.id.btnLog)

        buttonLogin.setOnClickListener {
            handler(view)
        }
    }

    fun handler(view: View)
    {
        login = view.findViewById(R.id.loginInput)
        password = view.findViewById(R.id.passInput)

        // Инициализируем SharedPreferences для хранения данных аккаунта
        preferences = requireActivity().getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
        ed = preferences.edit()

        var account: Account = Account(login.text.toString(), password.text.toString())

        if (account.login == "" || account.password == "") {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Введите логин и пароль")
                .setPositiveButton("OK",null)
                .create()
                .show()
            return
        }

        // Достаем сохраненный аккаунт из SharedPreferences
        var savedAccount = Gson().fromJson(preferences.getString("account", ""),
            Account::class.java)

        // Если аккаунта нет — сохраняем его
        if (savedAccount == null) {
            val JSONAccount = Gson().toJson(account)
            ed.putString("account", JSONAccount)
            ed.apply()

            goToInputFragment()
        }
        else {
            // Если аккаунт есть — проверяем совпадение логина и пароля
            if (account.login == savedAccount.login && account.password == savedAccount.password) {
                goToInputFragment()
            } else {
                val alert = AlertDialog.Builder(requireContext())
                    .setTitle("Ошибка")
                    .setMessage("Неправильный логин или пароль")
                    .setPositiveButton("OK",null)
                    .create()
                    .show()
            }
        }
    }

    fun goToInputFragment() {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Переход на экран поиска покемона
        fragmentTransaction.replace(R.id.fragment_container, InputFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}