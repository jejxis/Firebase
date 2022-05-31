package net.flow9.thisisKotlin.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import net.flow9.thisisKotlin.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    val database = Firebase.database("https://android-kotlin-firebase-debb2-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myRef = database.getReference("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnPost.setOnClickListener {
                val name = editName.text.toString()
                val password = editPassword.text.toString()
                val user = User(name, password)
                addItem(user)
            }
        }

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.textList.setText("")

                for(item in snapshot.children){//snapshot을 users 노드로 보면 될듯...그러니까 item은 각 User 정보
                    //item의 value를 꺼내 User 클래스로 캐스팅
                    //value가 없을 수도 있으므로 let 스코프 함수로 처리
                    item.getValue(User::class.java)?.let{user ->
                        binding.textList.append("${user.name} : ${user.password} \n")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        /*일회성 값 조회
       val database = Firebase.database
        val myRef = database.getReference("bbs")
        myRef.child("name").setValue("Scott")
        myRef.child("age").setValue(19)

        myRef.child("name").get().addOnSuccessListener {//일회성 값 조회
            Log.d("fb", "name=${it.value}")
        }.addOnFailureListener {
            Log.d("fb", "error=${it}")
        }

        myRef.child("name").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("myfb", "${snapshot.value}")
                print(snapshot.value)//값이 바뀔 때마다 호출
            }

            override fun onCancelled(error: DatabaseError) {
                print(error.message)
            }
        })*/
    }
    fun addItem(user: User){
        val id = myRef.push().key!!
        user.id = id
        myRef.child(id).setValue(user)
    }
}
class User{
    var id:String=""
    var name:String = ""
    var password:String=""
    constructor()

    constructor(name:String, password: String){
        this.name = name
        this.password = password
    }
}