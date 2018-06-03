package requiem.pppoyo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference



class MainActivity : AppCompatActivity() {

    private var email:String? = null
    private var password:String? = null
    private var TAG:String = "Hey there"

    private var etEmail:EditText? = null
    private var etPassword:EditText? = null
    private var btnInicia:Button? = null
    private var btnRegister:Button? = null
    private var tvForgot:TextView? = null

    private  var mAuth:FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialise()
    }

    private fun initialise(){
        etEmail = findViewById(R.id.usu1)
        etPassword = findViewById(R.id.contra1)
        btnInicia = findViewById(R.id.iniciaa)
        btnRegister = findViewById(R.id.register)
        tvForgot = findViewById(R.id.forgot)

        mAuth = FirebaseAuth.getInstance()

        btnRegister!!.setOnClickListener{startActivity(Intent(this, RegisterActivity::class.java))}
        tvForgot!!.setOnClickListener{startActivity(Intent(this, ForgotPasswordActivity::class.java))}
        btnInicia!!.setOnClickListener{logInUser()}
    }

    private fun logInUser(){

        email = etEmail?.text.toString()
        password = etPassword?.text.toString()

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            Log.d(TAG, "Iniciando")

            mAuth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this){task ->

                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    updateUI()
                } else {
                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this@MainActivity, "No se autentico.",
                            Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateUI(){
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
