import React, { useState } from 'react';
import '../App.css';
import { ApiJavaSimplified } from '../shared/ApiJavaBE';
import axiosInstance from '../shared/ApiIntercept'
import { useNavigate } from 'react-router-dom';

/**
 * 
 */
function LoginJava() {

  const [error, setError] = useState(" ")
  const navigate  = useNavigate();

  // ---- Event triggers ----
  const onAuthWithJWT = () => {


    axiosInstance.get("/test")
      .then((res: { data: any; }) => {

        console.log("\n *********** Login **********")
        console.log("RAW onJAVAAuth: ", res)
        console.log("onJAVAAuth: ", res.data)

      })
      .catch((err: any) => {
        console.log("Test err", err)
        
        // Check if token in storage is empty, then direct to login page
        if(localStorage.getItem("JWT") == null)  navigate("/login")
      })
      
  }

  const onLogin = () => {

    const data = {
      "userName": "test",
      "passWord": "testPW"
    }

    axiosInstance.post("/login", data)
      .then((res: { data: any; }) => {

        console.log("\n *********** Login **********")
        console.log("RAW onJAVAAuth: ", res)
        console.log("onJAVAAuth: ", res.data)

        if (res.data) {
          localStorage.setItem("jwt", res.data.token);
          localStorage.setItem("refreshToken", res.data.refreshToken);
        }

      })
      .catch((err: any) => console.log("Login err", err))
  }

  return (
    <>
      <div className="App">
        <h2>Login Java</h2>
        <p>JWT Authentication</p>
        <button onClick={onLogin} type="button">Login</button>
        <button onClick={onAuthWithJWT} type="button">Test with JWT Authentication</button>
        <p>{error}</p>
      </div>
    </>

  )
}

export default LoginJava
