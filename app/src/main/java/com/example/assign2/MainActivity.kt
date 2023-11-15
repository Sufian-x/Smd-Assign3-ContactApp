package com.example.assign2

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.assign2.ui.theme.Assign2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assign2Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    ContactApp()
                }
            }
        }
    }
}



@Composable
fun ContactApp() {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        val contactsList = getContacts(LocalContext.current)
        DisplayContacts(contacts = contactsList)
    }
}

data class Contact(val id : Long, val name: String, val number: String)

fun getContacts (context: Context) : List<Contact>{

    ActivityCompat.requestPermissions(
        context as ComponentActivity,
        arrayOf(Manifest.permission.READ_CONTACTS)
        ,0

    )

    val contactsList = mutableListOf<Contact>()

    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    )

    val contentResolver = context.contentResolver
    contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        null,
        null,
        null )
        ?.use {
                cursor ->
            val contactID = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val contactName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val contactNumber = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val contactPhoto = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)


            while (cursor.moveToNext()){
                val id = cursor.getLong(contactID)
                val name = cursor.getString(contactName)
                val number = cursor.getString(contactNumber)
                val img = cursor.getString(contactPhoto)

//                var imgURI: Uri? = null
//                if (img != null) imgURI = Uri.parse(img)
                //else getContactUri(id)

                contactsList.add(Contact(id, name , number))
            }
        }
    return contactsList
}

@Composable
private fun DisplayContacts(contacts : List<Contact>){

    LazyColumn(modifier = Modifier.fillMaxSize())
    {
       items(contacts){ contact->
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {

                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = contact.name)
                    Text(text = contact.number)
                }
            }
           Divider()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assign2Theme {
        ContactApp()
    }
}