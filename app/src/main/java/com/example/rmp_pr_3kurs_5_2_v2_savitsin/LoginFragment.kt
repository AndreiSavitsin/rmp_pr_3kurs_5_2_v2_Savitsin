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

        buttonLogin = view.findViewById(R.id.btnLog)

        buttonLogin.setOnClickListener {
            handler(view)
        }
    }

    fun handler(view: View)
    {
        login = view.findViewById(R.id.loginInput)
        password = view.findViewById(R.id.passInput)

        preferences = requireActivity().getSharedPreferences(PREFS_FILE, MODE_PRIVATE)

        var account: Account = Account(login.text.toString(), password.text.toString())

        var savedAccount = Gson().fromJson(preferences.getString("account", ""), Account::class.java)

        var firstLaunch: Boolean = preferences.getBoolean("firstLaunch", true)
        ed = preferences.edit()

        if (account.login == "" || account.password == "")
        {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Введите логин и пароль")
                .setPositiveButton("OK",null)
                .create()
                .show()
        }
        else if (firstLaunch)
        {
            val JSONAccount = Gson().toJson(account)

            ed.putString("account", JSONAccount)

            firstLaunch = false

            ed.putBoolean("firstLaunch", firstLaunch)
            ed.apply()

            val fragmentManager: FragmentManager = parentFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_container, InputFragment())
            fragmentTransaction.addToBackStack(LoginFragment.toString())
            fragmentTransaction.commit()
        }
        else
        {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Введите правильный логин и пароль")
                .setPositiveButton("OK",null)
                .create()
                .show()
        }
    }
}