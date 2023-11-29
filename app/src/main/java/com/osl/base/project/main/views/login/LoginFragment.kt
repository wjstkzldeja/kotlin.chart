package com.osl.base.project.main.views.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayoutMediator
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentLoginBinding
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.main.views.login.adapter.LoginViewPagerAdapter
import com.osl.base.project.osl.views.OslFragment
import timber.log.Timber

class LoginFragment  : OslFragment<FragmentLoginBinding, LoginViewModel>() {
    override val layoutRes = R.layout.fragment_login
    override val destinationId = R.id.loginFragment
    override val viewModelClass = LoginViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java


    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        initSwipeview()
        initOnClickGoogleSignIn()
    }

    override fun addObservers() {

    }

    private fun initSwipeview() {
        ui.viewDataBinding.loginPager.adapter = LoginViewPagerAdapter(this)
        TabLayoutMediator(ui.viewDataBinding.loginTabLayout, ui.viewDataBinding.loginPager){ tab, position ->
            // callback 구현; 새로 생성된 탭의 텍스트 및 스타일 설정하는 곳.
        }.attach()
    }

    private lateinit var googleSignInClient : GoogleSignInClient
    private fun initOnClickGoogleSignIn(){
        // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정.(유저id, 기본 프로필 정보)
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // 위의 GoogleSignInOptions을 사용해 GoogleSignInClient 객체 만들기
        googleSignInClient = ui.root.context?.let {
            GoogleSignIn.getClient(
                it,
                googleSignInOption
            )
        }!!
        // 버튼 클릭시 signInIntent 만들고 이 인텐트를 start한다
        ui.viewDataBinding.googleLoginBtn.setOnClickListener {
            //로그인이 이미 되어있음 로그아웃하기
            val account = context?.let { GoogleSignIn.getLastSignedInAccount(it) }
            if (account!=null){
                println("already signed")
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val mGoogleSignInClient = this.let { GoogleSignIn.getClient(requireContext(), gso) }
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    println("logout --")
                }
            }

            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        // callback 구현; 인텐트로 start한 로그인 버튼 탭을 처리한다
        if(result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try {
            // sign 성공
            val account = completedTask.getResult(ApiException::class.java)
            // 처리
            val email = account.email.toString()
            println("login success! : $email")
            //약관 동의받기
            val bottomSheetFragment = TermsOfServiceBottomSheetFragment()
            //fragmentManager?.let { bottomSheetFragment.show(it,bottomSheetFragment.tag) }
            bottomSheetFragment.show(childFragmentManager,bottomSheetFragment.tag)
        }catch (e: ApiException) {
            Timber.tag("googleAuth").w("failed code=%s", e.stackTraceToString())
        }
    }
}