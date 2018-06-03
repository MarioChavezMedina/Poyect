package requiem.pppoyo

import android.app.ProgressDialog
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class RegisterActivity : AppCompatActivity() {

    private var etName:EditText? = null
    private var etEmail:EditText? = null
    private var etPassword:EditText? = null
    private var btnRegister:Button? = null

    //FireBase references
    private var mDatabaseReference:DatabaseReference? = null
    private var mDatabase:FirebaseDatabase? = null
    private var mAuth:FirebaseAuth? = null

    private val TAG = "HeyThere"

    //global
    private var name:String? = null
    private var email:String? = null
    private var password:String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initialise()
    }

    //Initialice variables
    private fun initialise(){
        etName = findViewById(R.id.name)
        etEmail = findViewById(R.id.mail)
        etPassword = findViewById(R.id.password)
        btnRegister = findViewById(R.id.register)



        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        btnRegister!!.setOnClickListener{createNewAccount()}
    }

    private fun createNewAccount(){
        name = etName?.text.toString()
        email = etEmail?.text.toString()
        password = etPassword?.toString()


        mAuth!!
                .createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this){task ->

                    if(task.isSuccessful){
                        Log.d(TAG,"new user registered succefully")

                        val userId = mAuth!!.currentUser!!.uid

                        verifyEmail()

                        val currentUserDb = mDatabaseReference!!.child(userId)
                        currentUserDb.child("name").setValue(name)

                        updateUserInfoAndUI()
                    }else{
                        Log.w(TAG, "regisrer failure", task.exception)
                        Toast.makeText(this@RegisterActivity,"Auth failure",Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun verifyEmail(){
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        Toast.makeText(this@RegisterActivity,"email verification have sent to "+ mUser.getEmail(), Toast.LENGTH_SHORT ).show()
                    }else{
                        Toast.makeText(this@RegisterActivity,"cant send a email verification to "+ mUser.getEmail(), Toast.LENGTH_SHORT ).show()
                    }
                }
    }

    private fun updateUserInfoAndUI(){
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
