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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InputFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InputFragment.
         */
        // TODO: Rename and change types and number of parameters
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

        buttonSearch = view.findViewById(R.id.btnSearch)

        buttonSearch.setOnClickListener {
            handler(view)
        }

        buttonBack = view.findViewById(R.id.btnBack)
        buttonBack.setOnClickListener {
            goBack(view)
        }
    }

    fun handler(view: View)
    {
        nameOrId = view.findViewById(R.id.nameOrIdInput)

        if (nameOrId.text.toString().isNotEmpty() && nameOrId.text.toString() != null) {
            var url = "https://pokeapi.co/"
            val queue = Volley.newRequestQueue(requireContext())
            val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                {
                        response->
                    try {
                        val obj = JSONObject(response)

                        val name = obj.getString("name")

                    }
                    catch (e: Exception)
                    {
                        Log.d("MyLog", "JSON Parsing error: ${e.message}")
                    }
                },
                {
                    Log.d("MyLog", "Volley error: $it")
                }
            )
            queue.add(stringRequest)
        }
        else
        {
            val alert = AlertDialog.Builder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Введите название или id персонажа")
                .setPositiveButton("OK",null)
                .create()
                .show()
        }
    }

    fun goBack(view: View)
    {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.fragment_container, ShowInfoFragment())
        fragmentTransaction.addToBackStack(LoginFragment.toString())
        fragmentTransaction.commit()
    }
}