package com.segihovav.mylinks_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
     private lateinit var switchDarkMode: SwitchMaterial
     //private lateinit var useFirebaseInstanceURLS: SwitchMaterial
     private lateinit var myLinksURLs: Spinner

     private var darkModeToggled = false

     override fun onCreate(savedInstanceState: Bundle?) {
          DataService.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
          this.setTheme(if (DataService.sharedPreferences.getBoolean("DarkThemeOn", false)) DataService.darkMode else DataService.lightMode)

          super.onCreate(savedInstanceState)
          setContentView(R.layout.settings)

          val titleBar=findViewById<TextView>(R.id.TitleBar)
          titleBar.text = DataService.MyLinksTitle

          switchDarkMode = findViewById(R.id.switchDarkMode)

          myLinksURLs = findViewById(R.id.LinkURLSpinner)

          switchDarkMode.isChecked = DataService.sharedPreferences.getBoolean("DarkThemeOn", false)

          DataService.useFirebase = DataService.sharedPreferences.getBoolean("UseFirebase", false)

          DataService.myLinksInstancesDataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,DataService.getInstanceDisplayNames() as List<String>)

           // attaching data adapter to spinner
           myLinksURLs.adapter = DataService.myLinksInstancesDataAdapter

           DataService.myLinksInstancesDataAdapter.notifyDataSetChanged()

           if (DataService.sharedPreferences.getString("MyLinksActiveURL", "") != "") {
                for (i in DataService.instanceURLs.indices)
                     if (DataService.instanceURLs[i].url == DataService.sharedPreferences.getString("MyLinksActiveURL", ""))
                          myLinksURLs.setSelection(i+1) // add 1 because empty string is added to spinner values
           } else {
               DataService.alert(androidx.appcompat.app.AlertDialog.Builder(this), message = "Please select the active MyLinks URL", finish = { finish() }, OKCallback = null)
           }

          /*if (!DataService.useFirebase) {
               loadInstanceURLsFromREST()
          } else {
               loadInstanceURLsFromFirebase()
          }*/

          //useFirebaseInstanceURLS = findViewById(R.id.switchInstanceURLSource)
          //useFirebaseInstanceURLS.isChecked = DataService.useFirebase
     }

     fun darkModeClick(v: View?) {
          darkModeToggled = true
          Toast.makeText(applicationContext, "The app will close when you click on save for this to take effect" + if (switchDarkMode.isChecked) ". You must have Dark Mode enabled on Android " else "", Toast.LENGTH_SHORT).show()
     }

     fun goBackClick(v: View?) {
          val intent = Intent(this, MainActivity::class.java)
          startActivity(intent)
     }

     /*fun loadInstanceURLsFromFirebase() {
          DataService.myLinksInstancesDataAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,DataService.getInstanceDisplayNames() as List<String>)

          // attaching data adapter to spinner
          myLinksURLs.adapter = DataService.myLinksInstancesDataAdapter

          if (DataService.sharedPreferences.getString("MyLinksActiveURL", "") != "") {
               for (i in DataService.instanceURLs.indices)
                    if (DataService.instanceURLs[i].url == DataService.sharedPreferences.getString("MyLinksActiveURL", ""))
                         myLinksURLs.setSelection(i)
          } else {
               DataService.alert(androidx.appcompat.app.AlertDialog.Builder(this), message = "Please select the active MyLinks URL", finish = { finish() }, OKCallback = null)
          }

          // Show manage URLs add adjust top margin
          val addURLButton = findViewById<Button>(R.id.addURLButton)
          addURLButton.visibility = View.VISIBLE

          val addLinkURLsLbl=findViewById<TextView>(R.id.AddLinkURLsLbl)

          var params: ViewGroup.MarginLayoutParams = addLinkURLsLbl.layoutParams as ViewGroup.MarginLayoutParams
          params.setMargins(0, 150, 0, 0)
          addLinkURLsLbl.layoutParams=params

          params = myLinksURLs.layoutParams as ViewGroup.MarginLayoutParams
          params.setMargins(450, 150, 0, 0)
          myLinksURLs.layoutParams=params
     }*/

     fun manageURLsClick(v: View?) {
          val intent = Intent(this, ManageInstanceLinks::class.java)
          startActivity(intent)
     }

     fun saveClick(v: View?) {
          if (myLinksURLs.selectedItem == "") {
               Toast.makeText(applicationContext, "Please select the active MyLinks URL", Toast.LENGTH_LONG).show()
               return
           }

          val editor = DataService.sharedPreferences.edit()

          editor.putBoolean("DarkThemeOn", switchDarkMode.isChecked)
          //editor.putBoolean("UseFirebase", useFirebaseInstanceURLS.isChecked)

          //DataService.useFirebase=useFirebaseInstanceURLS.isChecked

          for (i in DataService.instanceURLs.indices) {
               if (DataService.instanceURLs[i].displayName == myLinksURLs.selectedItem) {
                    editor.putString("MyLinksActiveURL", DataService.instanceURLs[i].url)
                    DataService.MyLinksActiveURL = DataService.instanceURLs[i].url
                    break
               }
          }

          editor.apply()

          val intent = Intent(this, MainActivity::class.java)

          if (darkModeToggled)
               intent.putExtra(applicationContext.packageName + ".DarkModeToggled", true)

          if (darkModeToggled)
               finishAffinity()

          startActivity(intent)
     }

     /*fun useFirebaseClick(v: View?) {
          val editor = DataService.sharedPreferences.edit()

          //editor.putBoolean("UseFirebase", useFirebaseInstanceURLS.isChecked)

          //DataService.useFirebase=useFirebaseInstanceURLS.isChecked

          editor.apply()
     }*/
}
