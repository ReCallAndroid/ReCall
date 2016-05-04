package com.example.kwongyo.recall.core;


import android.util.Log;

import com.example.kwongyo.recall.StaticInfomation;
import com.example.kwongyo.recall.interfaceOfRECALL.LoginInterface;
import com.example.kwongyo.recall.model.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kwongyo on 2016-05-03.
 */
public class LoginController {
    private static LoginController loginController;



    private Retrofit retrofit = null;
    /**a
     * 성공 했는지 아닌지
     */
    private boolean isSuccess=false;

    public boolean getIsSuccess(){return this.isSuccess;}

    static {
        loginController = new LoginController();
    }


    public static LoginController getInstance() {
        if(loginController==null)
            loginController= new LoginController();
        return loginController;
    }


    public boolean requestLogin(String email,String password) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfomation.RECALL_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();


        /*요청보낼 interface 객체 생성.*/
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<UserDTO> call = loginInterface.requestLogin(email, password);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess())
                    return ; // 아무 코드를 실행하지 않고 리턴.

                Log.d("로그인_성공코드 -",response.code()+""); // 디버깅용
                isSuccess = true; // 성공했다고 그냥 넣어놓음.
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                isSuccess = false;
            }
        });
        return isSuccess;
    }

}