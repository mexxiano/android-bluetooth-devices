package com.mdeb.bluetoothdevices

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mdeb.bluetoothdevices.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var btPermission = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Bluetooth Example Kotlin"
    }

    fun scanBt(view: View) {

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
           Toast.makeText(this,"No soporta Bluetooth", Toast.LENGTH_LONG).show()
        } else {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT) //Manifest.permission.BLUETOOTH_CONNECT
            } else {
                blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }
        }
    }

    private val blueToothPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        isGranted:Boolean->
        if(isGranted){
            val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter?= bluetoothManager.adapter
            btPermission = true
            if(bluetoothAdapter?.isEnabled == false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            }
            else {
                scanBT()
            }
        }
    }

    private val btActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode == RESULT_OK){
            scanBT()
        }
    }


    @SuppressLint("MissingPermission")
    private fun scanBT()
    {
        val bluetoothManager:BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter?= bluetoothManager.adapter

        val builder = AlertDialog.Builder(this@MainActivity)

        val inflater = layoutInflater
        val dialogView:View = inflater.inflate(R.layout.scan_bt, null)

        builder.setCancelable(false)
        builder.setView(dialogView)

        val btlst = dialogView.findViewById<ListView>(R.id.bt_list)
        val dialog = builder.create()
        val pairedDevices:Set<BluetoothDevice> = bluetoothAdapter?.bondedDevices as Set<BluetoothDevice>
        val ADAhere:SimpleAdapter
        var data:MutableList<Map<String?,Any?>?>? = null
        data = ArrayList()

        if(pairedDevices.isNotEmpty()){

            val datanum1: MutableMap<String?, Any?> =  HashMap()
            datanum1["A"]   =  ""
            datanum1["B"]   =  ""
            data.add(datanum1)

            for(device in pairedDevices){
                val datanum:MutableMap<String?,Any?> = HashMap()
                datanum["A"] = device.name
                datanum["B"] = device.address
                data.add(datanum)
            }

            val fromwhere = arrayOf("A")
            val viewshwere = intArrayOf(R.id.item_name)
            ADAhere = SimpleAdapter(this@MainActivity, data, R.layout.item_list,fromwhere,viewshwere)
            btlst.adapter = ADAhere
            ADAhere.notifyDataSetChanged()
            btlst.onItemClickListener = AdapterView.OnItemClickListener{adapterView, view, position, l ->
                val string = ADAhere.getItem(position) as HashMap<String, String>
                val deviceName = string["A"] + " - MAC: " + string["B"]
                binding.deviceName.text = deviceName
                dialog.dismiss()
            }

        } else{
            val value = "No Devices found"
            Toast.makeText(this, value, Toast.LENGTH_LONG).show()
            return
        }

        dialog.show()
    }

    fun sendHexFile(view: View) {

        // Mostramos en el Log qué archivos hay en /Assets
        listAssetFiles()

        val sourceFile = File("/data/user/0/com.mdeb.bluetoothdevices/cache/avrdude")
        val targetFile = File("/storage/emulated/0/avrdude")

        try{
            sourceFile.copyTo(targetFile)
            Log.i("Copiando","$sourceFile is copied to $targetFile")
        }catch (e:Exception){
            e.printStackTrace()
            Log.w("Copiando", "error al copiar archivos")
        }

        val fileName = "/data/user/0/com.mdeb.bluetoothdevices/cache/avrdude"
        var file = File(fileName)
        var fileExists = file.exists()

        val filePath =  getFileFromAssets(this, "avrdude").absolutePath

        Log.i("Cache file", filePath)

        if(fileExists){
            Log.i("Chequando si existe avrdude","$fileName exists.")
        } else {
            Log.w("Chequando si existe avrdude","$fileName does not exist.")
        }

        try {
            //Runtime.getRuntime().exec("/data/user/0/com.mdeb.bluetoothdevices/cache/avrdude -F -V -c arduino -p ATMEGA328P -P /dev/bus/usb/002/002  -b 115200 -C /data/user/0/com.mdeb.bluetoothdevices/avrdude.conf -U flash:w:/data/user/0/com.mdeb.bluetoothdevices/files/Sketch_Blink.hex")
            // este da error 13 >>>> Runtime.getRuntime().exec("/data/user/0/com.mdeb.bluetoothdevices/cache/avrdude -c arduino -p ATMEGA328P -P /dev/bus/usb/002/002  -b 115200 -C /data/user/0/com.mdeb.bluetoothdevices/avrdude.conf -U flash:w:/data/user/0/com.mdeb.bluetoothdevices/files/Sketch_Blink.hex")
            Runtime.getRuntime().exec("/data/user/0/com.mdeb.bluetoothdevices/cache/avrdude -v")

        } catch(e:Exception){
            Log.e("Runtime error", e.message.toString())
        }
    }

    @Throws(IOException::class)
    fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
        .also {
            if (!it.exists()) {
                it.outputStream().use { cache ->
                    context.assets.open(fileName).use { inputStream ->
                        inputStream.copyTo(cache)
                    }
                }
            }
        }

    private fun listAssetFiles() {
        val list: Array<String>

        list = assets.list("")!!

        Log.d("TAGListAst", "listAssetFiles:s: ${list?.size}")
        if (list!!.isNotEmpty()) {
            for (file in list) {
                if (file != "images" && file != "webkit") {

                    val assetList = mutableListOf<String>()

                    assetList.add(file)

                    val path = this.filesDir.absolutePath

                    val pathAndFile = File("$path/$file")

                    Log.i("File", pathAndFile.toString())
                }
            }

        }


    }
}