package com.segihovav.mylinks_android

import android.content.Context
import android.content.SharedPreferences
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class DataService: AppCompatActivity() {
     companion object {
          @JvmStatic val getLinksDataEndpoint = "LinkData.php?task=fetchData"
          @JvmStatic val getTypesDataEndpoint = "LinkData.php?task=fetchTypes"
          @JvmStatic val getSegiTypesDataEndpoint = "LinkData.php?task=fetchSegiTypes"
          @JvmStatic val deleteLinkDataEndpoint = "LinkData.php?task=deleteRow"
          @JvmStatic val lightMode = R.style.ThemeOverlay_MaterialComponents
          @JvmStatic val darkMode = R.style.ThemeOverlay_MaterialComponents_Dark
          @JvmStatic val JSONInstancesBaseURL: String="https://mylinks-instances.hovav.org/"
          @JvmStatic val JSONAuthToken: String=")j3s%3CltoEcv;eW=g0xX"
          @JvmStatic var useFirebase: Boolean = false
          @JvmStatic var MyLinksActiveURL: String = ""
          @JvmStatic var myLinksTypes: ArrayList<MyLinkType> = ArrayList()
          @JvmStatic var myLinksTypeNames: ArrayList<String> = ArrayList()
          @JvmStatic var MyLinksTitle: String = ""
          @JvmStatic var instanceURLs: ArrayList<InstanceURLType> = ArrayList()
          @JvmStatic lateinit var sharedPreferences: SharedPreferences
          @JvmStatic lateinit var myLinksInstancesDataAdapter: ArrayAdapter<String>

          @JvmStatic fun alert(builder: AlertDialog.Builder, message: String, closeApp: Boolean = false, confirmDialog: Boolean = false, finish: () -> Unit, OKCallback: (() -> Unit)?) {
               builder.setMessage(message).setCancelable(confirmDialog)

               builder.setPositiveButton("OK") { _, _ -> if (closeApp) finish() }

               if (confirmDialog) {
                    builder.setNegativeButton("Cancel") { _, _ -> if (closeApp) finish() }

                    if (OKCallback != null) {
                         builder.setPositiveButton("OK") { _, _ -> OKCallback() }
                    }
               }

               val alert = builder.create()

               alert.show()
          }

          @JvmStatic fun init(thisContext: Context) {
               for (currInstance: InstanceURLType in this.instanceURLs) {
                    if (this.MyLinksActiveURL == currInstance.url)
                         this.MyLinksTitle = currInstance.displayName
               }

               if (!useFirebase)
                    return

               // Read myLinks instance URLS from Firebase
               val database = FirebaseDatabase.getInstance()

               val myRef = database.getReference("MyLinks")

               // Firebase Event listener
               myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                         val dataList = dataSnapshot.children.toList()

                         instanceURLs.clear()

                         for (linkItem in dataList) {
                             instanceURLs.add(InstanceURLType(linkItem.key.toString(),linkItem.value.toString(),""))
                         }

                         try {
                           myLinksInstancesDataAdapter.notifyDataSetChanged()
                         }  catch(e: Exception) {}

                    }

                    override fun onCancelled(error: DatabaseError) {
                         // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException())
                    }
               })

               // Write the data to Firebase. Uncomment me if this data is deleted from the DB
               /*ar myRefAdd = database.getReference("MyLinks/AbaLinks")
               myRefAdd.setValue("https://abalinks.hovav.org");

               myRefAdd = database.getReference("MyLinks/EmaLinks")
               myRefAdd.setValue("https://emalinks.hovav.org");*/
          }

          fun getInstanceDisplayNames(isManagingInstances: Boolean = false): MutableList<String> {
               val myLinkInstanceURLSNames: MutableList<String> = mutableListOf()

               if (!isManagingInstances)
                    myLinkInstanceURLSNames.add("")

               for (currInstance in this.instanceURLs) {
                    myLinkInstanceURLSNames.add(currInstance.displayName)
               }

               return myLinkInstanceURLSNames
          }

          fun getActiveInstanceName(): String {
               for (currInstance in this.instanceURLs) {
                    if (this.MyLinksActiveURL == currInstance.url + if (currInstance.url.takeLast(1) != "/") "/" else "")
                         return currInstance.name
               }

               return ""
           }

          fun getActiveInstanceDisplayName(): String {
            for (currInstance in this.instanceURLs) {
                 if (this.MyLinksActiveURL == currInstance.url + if (currInstance.url.takeLast(1) != "/") "/" else "") {
                      this.MyLinksTitle = currInstance.displayName
                      return currInstance.displayName
                 }
            }

            return ""
          }
     } // End of companion object
}