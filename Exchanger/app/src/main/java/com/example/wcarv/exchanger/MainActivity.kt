package com.example.wcarv.exchanger

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrency()
    }

    private fun getCurrency (){

        val downloadData = DownloadCurrency()

        try {

            val url = "https://api.exchangeratesapi.io/latest"
            downloadData.execute(url)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun get (view: View) {

        if (spinnerOrigem.selectedItem == spinnerDestino.selectedItem) {
            Toast.makeText(this, R.string.same_item, Toast.LENGTH_SHORT).show()
            return
        }

        val downloadData = Download()

        try {

            val url = "https://api.exchangeratesapi.io/latest?base=" + spinnerOrigem.selectedItem.toString() + "&symbols=" + spinnerDestino.selectedItem.toString()
            downloadData.execute(url)


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class DownloadCurrency : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg args: String?): String {

            var result = ""
            var url : URL
            val httpURLConnection : HttpURLConnection

            try {

                url = URL(args[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

                return result

            } catch (e: Exception) {
                e.printStackTrace()
                return result
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {

                val jSONObject = JSONObject(result)

                val rates = jSONObject.getString("rates")

                val newJsonObject = JSONObject(rates)

                var resultado: ArrayList<String> = ArrayList()

                for (i in 0 until newJsonObject.length())
                    resultado.add(newJsonObject.names()[i].toString())
                resultado.add("EUR")

                var adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, resultado)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinnerOrigem.adapter = adapter
                spinnerDestino.adapter = adapter

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    inner class Download : AsyncTask<String, Void, String>(){

        override fun doInBackground(vararg args: String?): String {

            var result = ""
            var url : URL
            val httpURLConnection : HttpURLConnection

            try {

                url = URL(args[0])
                httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)

                var data = inputStreamReader.read()

                while (data > 0) {
                    val character = data.toChar()
                    result += character

                    data = inputStreamReader.read()
                }

                return result

            } catch (e: Exception) {
                e.printStackTrace()
                return result
            }

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {

                val jSONObject = JSONObject(result)
                Log.i("Exchanger", jSONObject.toString())

                val base = jSONObject.getString("base")
                Log.i("Exchanger", base)

                val rates = jSONObject.getString("rates")
                Log.i("Exchanger", rates)

                val newJsonObject = JSONObject(rates)

                Log.i("Exchanger", newJsonObject.getString(spinnerDestino.selectedItem.toString()))

                var resultado = "1 "
                resultado += spinnerOrigem.selectedItem.toString()
                resultado += " " + getString(R.string.to) + " "
                resultado += spinnerDestino.selectedItem.toString()
                resultado += " " + getString(R.string.worth) + " "
                resultado += newJsonObject.getString(spinnerDestino.selectedItem.toString()) + " "
                resultado += spinnerDestino.selectedItem.toString()

                textViewResultado.text = resultado

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
