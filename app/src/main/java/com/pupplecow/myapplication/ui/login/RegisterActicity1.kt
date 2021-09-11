package com.pupplecow.myapplication.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pupplecow.myapplication.R
import com.pupplecow.myapplication.data.UserData
import kotlinx.android.synthetic.main.activity_register1.*
import java.util.concurrent.TimeUnit

class RegisterActicity1 : AppCompatActivity() {

    //2분 기다릴 수 있음
    companion object{
        private const val AUTH_TIMELIMIT=120L
    }

    private var mUserType: Int=-1
    private var resendToken:PhoneAuthProvider.ForceResendingToken?=null
    private var storedVerificationId: String = ""

    val mAuth=Firebase.auth  //어스는 앱 전체에서 지원되고 있음 ( 이미 구글 로그인 객체가 생성되어있음)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)

        register_message.isGone

        //010.1234.5678
        //010-1234-5678
        val phonenumberPattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$".toPattern()

        register_cellPhoneNumber_input.addTextChangedListener {
            //휴대폰 번호인지 아닌지 실시간 체크
            //정규표현식

            if(!phonenumberPattern.matcher(it.toString()).matches()){
                register_cellPhoneNumber_message.text="휴대전화번호를 정확하게 입력해주세요"
                //인증번호 받기 버튼 눌릴지 아닐지 결정
                register_id_button.isEnabled=false

            }else{
                register_cellPhoneNumber_message.text=""
                register_id_button.isEnabled=true
            }

        }

//        //123456-7890123
//        register_id_input.addTextChangedListener {
//            it.toString().replace("-","")
//            if(it.toString().length>6){
//                register_id_input.setText(
//                it.toString().substring(0,6)+"-"+it.toString().substring(6)
//                )
//            }
//        }



        type1Btn.setOnClickListener {
            mUserType=1
            register_text_type.text="근로 형태-상용직"
        }

        type2Btn.setOnClickListener {
            mUserType=2
            register_text_type.text="근로 형태-일용직"
        }
        //인증번호 받기 버튼 클릭
        register_id_button.setOnClickListener {
            //핸드폰번호 저장

            //핸드폰번호 입력했는지 확인 (빈칸이면 메시지)
            val userCellPhoneNumber=register_cellPhoneNumber_input.text.toString()
            val userIDNumber=register_id_input.text.toString()
            val userName=register_name_input.text.toString()
            if(userCellPhoneNumber==""||userCellPhoneNumber.length!=11){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("휴대폰 번호를 입력해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }
            else if(userIDNumber==""||userIDNumber.length!=13){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("주민등록번호를 입력해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }
            else if(mUserType!=1&&mUserType!=2) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("근로형태를 선택해주세요")
                builder.setPositiveButton("네", null)
                builder.show()
            }
            else if(userName==""){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("이름를 입력해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }

            else{
            //val inputCellPhoneNumber
           // val userCellPhoneNumber=register_cellPhoneNumber_input.text.toString()
            val userCellPhoneNumberFront=userCellPhoneNumber.substring(0,3)
            val userCellPhoneNumberBack=userCellPhoneNumber.substring(7,11)


            //핸드폰 번호로 인증번호 전송 메시지
            //register_message.text=register_cellPhoneNumber_input.text
            register_message.text=userCellPhoneNumberFront+" **** "+userCellPhoneNumberBack+"로 인증번호가 전송되었습니다.\n회원가입을 계속하시려면 인증번호를 입력하세요."
            register_message.visibility= View.VISIBLE
            //인증번호 전송하기
                startPhoneNumberVerification()

            }
        }

        //다음버튼 클릭
        register_button1.setOnClickListener {

            //핸드폰번호 입력했는지 확인 (빈칸이면 메시지)
            val userCellPhoneNumber=register_cellPhoneNumber_input.text.toString()
            val userIDNumber=register_id_input.text.toString()
            val userName=register_name_input.text.toString()
            if(userCellPhoneNumber==""||userCellPhoneNumber.length!=11){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("휴대폰 번호를 입력해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }
            else if(userIDNumber==""||userIDNumber.length!=13){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("주민등록번호를 입력해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }
            else if(mUserType!=1&&mUserType!=2){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("근로형태를 선택해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }
            else if(userName==""){
                val builder= AlertDialog.Builder(this)
                builder.setTitle("")
                builder.setMessage("이름를 입력해주세요")
                builder.setPositiveButton("네",null)
                builder.show()
            }else{


            //인증번호 확인
            if(register_number_input.text.toString()==storedVerificationId) {
                //인증번호 확인되면
                    //이메일도 저장
                //이름, 주민등록번호, 전화번호 저장
                val userCellPhoneNumber=register_cellPhoneNumber_input.text.toString()
                var userName=register_name_input.text.toString()
                var userId=register_id_input.text.toString()

                //프로바이더 : 인증제공 업체
                val user= UserData().apply{
                     uid=mAuth.uid
                     email=mAuth.currentUser?.email
                     name=userName
                     photoUrl=mAuth.currentUser?.photoUrl.toString()
                     providerData= try {
                         mAuth.currentUser?.providerData?.get(1)?.providerId //0:파이어베이스, 1: 구글닷컴
                     }catch (e:Exception){
                         mAuth.currentUser?.providerData?.get(0)?.providerId
                     }


                    //로그인 방식
                     //fcmToken=  //푸시 알림
                     phoneNumber=userCellPhoneNumber //01012341234

//                    val birthPrefix=userIDNumber.split("-")[1].substring(0,1)  //-> 주민등록번호 뒷자리 한자리 뽑아옴
//                    var birthPostfix=userIDNumber.split("-")[0]  // 990727
                    //userIDNumber.split("-")[0] //19990727-1234567 -> [990727,1234567]
                    val birthPrefix=userIDNumber.substring(6,7)
                    var birthPostfix=userIDNumber.substring(0,6) // 990727
                    Log.e("birth","$userId$birthPrefix$birthPostfix")


                     birthDate= (if(birthPrefix=="1"||birthPrefix=="2")"19" else "20")+birthPostfix
                     userType= mUserType //0: 관리자, 1:일용직 , 2:상용직
                     ssid=userId  //990727-1234567
                }

                Firebase.database.reference.child("users").child(mAuth.uid!!).setValue(user)
                    .addOnSuccessListener {
                        //RegisterActivity_2로 넘어가기
                        val intent = Intent(this, RegisterActivity2::class.java)
                        intent.putExtra("userCellPhoneNumber",userCellPhoneNumber)
                        intent.putExtra("userName",userName)
                        intent.putExtra("userId",userId)
                        startActivity(intent)
                    }




            }
            else{
                //인증번호 틀리면
                register_message.text="인증번호가 틀렸습니다. 다시 인증해 주세요"
            }
        }}

    }
    //폰넘버 앞에 +82 붙여야함
    private fun startPhoneNumberVerification() {

        var phoneNumber="+82"+register_cellPhoneNumber_input.text.toString().replace(".","").replace("-","").substring(1)

        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(AUTH_TIMELIMIT, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            //여기서는 이거 필요 없음
            //이콜백은 두가지 상황일떄
            //1. 실기기로 테스트일때 내폰이면 인증번호 입력할 필요 없음
            //2. 구글 플레이 서비스가 자동적으로 체크해서 유저 액션 없이
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            //Log.d(TAG, "onVerificationCompleted:$credential")
            //폰번호로 가입을 시도
            //파이어 베이스에 어센틱 올라갈 필요 없음
            //signInWithPhoneAuthCredential(credential)

        }

        override fun onVerificationFailed(e: FirebaseException) {
            //폰넘버 포맷이 잘못 된경우
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            //문자 발송 되었을때
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            //인증 코드
            storedVerificationId = verificationId
            //재전송
            resendToken = token
        }
    }



}