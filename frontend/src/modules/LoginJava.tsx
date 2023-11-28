import React, { useState } from 'react';
import '../App.css';
import { ApiJavaSimplified } from '../shared/ApiJavaBE';

/**
 * 
 */
function LoginJava() {

  const [error, setError] = useState(" ")


  // ---- Event triggers ----
  const onAuthWirgJWT = () => {

    const headers = {
      'Content-type': 'application/json',
      'Authorization': 'Bearer ' + localStorage.getItem("JWT")
    }

    ApiJavaSimplified("GET", "test", headers)
      .then((res: { data: any; }) => {

        console.log("\n *********** Login **********")
        console.log("RAW onJAVAAuth: ", res)
        console.log("onJAVAAuth: ", res.data)

      })
  }

  const onLogin = () => {

    const headers = {
      'Content-type': 'application/json',
    }

    const data = {
      "userName": "test",
      "passWord": "testPW"
    }

    ApiJavaSimplified("POST", "login", headers, data)
      .then((res: { data: any; }) => {

        console.log("\n *********** Login **********")
        console.log("RAW onJAVAAuth: ", res)
        console.log("onJAVAAuth: ", res.data)

        if (res.data) localStorage.setItem("JWT", res.data);

      })
      .catch((err: any) => console.log("Login err", err))
  }

  return (
    <>
      <div className="App">
        <h2>Login Java</h2>
        <p>JWT Authentication</p>
        <button onClick={onLogin} type="button">Login</button>
        <button onClick={onAuthWirgJWT} type="button">Test with JWT Authentication</button>
        <p>{error}</p>
      </div>
    </>

  )
}

export default LoginJava
