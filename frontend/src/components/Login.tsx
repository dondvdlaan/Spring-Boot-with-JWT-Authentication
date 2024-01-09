import { Dispatch, SetStateAction, useState } from "react";
import axiosInstance from "../shared/ApiIntercept";
import { ErrorText } from "../shared/ErrorText";
import { useNavigate } from "react-router-dom";

interface Props {
    setToken: Dispatch<SetStateAction<undefined>>
    isRegister?: boolean
}


// ****************** Main ****************** 
export const Login = (props: Props) => {

    // Hooks
    const [userName, setUserName] = useState("");
    const [passWord, setPassWord] = useState("");
    const [loginErr, setLoginErr] = useState(0);
    const navigate = useNavigate();


    // ****************** Functions ****************** 
    const userLogin = (data: any) => {

        console.log("userLogin data:", data)

        // Check user input against DB
        axiosInstance.post("/login", data)
            .then((resp: any) => {
                if (resp.data.errors) {

                    console.log("/login error: ", resp.data)

                    setLoginErr(resp.data.errors[0]);
                }

                else if (resp.data) {

                    console.log("Succesful login resp.data: ", resp.data)
                    // Create the localStorage for jwt token
                    localStorage.setItem("jwt", resp.data.jwtToken);

                    // Create the localStorage for refreshToken
                    localStorage.setItem("refreshToken", resp.data.refreshToken);

                    // Respond to setToken in requesting page
                    props.setToken(resp.data.jwtToken)
                    props.setToken(resp.data["jwtToken"])
                    // Refresh page so that requesting page checks login
                    //navigate(0)

                }
            }
            )
    }
    // ****************** Eventhandlers ******************
    const onLogin = (e: React.FormEvent) => {
        // Prevent from refreshing
        e.preventDefault();

        let data = {
            userName,
            passWord
        }

        userLogin(data);
    }

    return (

        <section className="h-screen bg-yellow-50">
            <div className="px-6 h-full text-gray-800">
                <h1 className="text-5xl text-center pt-3">SPRING SECURITY AND JWT AUTHENTICATION</h1>
                <div className="flex xl:justify-center lg:justify-between justify-center items-center flex-wrap h-full g-6" >
                    <div className="xl:ml-20 xl:w-5/12 lg:w-5/12 md:w-8/12 mb-12 md:mb-0">
                        <form
                            onSubmit={onLogin}
                        >
                            {/* Login error message */}
                            <ErrorText error={loginErr} />

                            {/* Email input */}
                            <div className="mb-6">
                                <input
                                    type="text"
                                    className="form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="id1"
                                    placeholder="Username"
                                    value={userName}
                                    onChange={(e) => {
                                        setUserName(e.target.value);
                                    }}
                                />
                            </div>
                            {/* Password input */}
                            <div className="mb-6">
                                <input
                                    type="password"
                                    className="form-control block w-full px-4 py-2 text-xl font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-300 rounded transition ease-in-out m-0 focus:text-gray-700 focus:bg-white focus:border-blue-600 focus:outline-none"
                                    id="id2"
                                    placeholder="Password"
                                    onChange={(e) => {
                                        setPassWord(e.target.value);
                                    }}
                                />
                            </div>
                            {/* 
          <div className="flex justify-between items-center mb-6">
            <div className="form-group form-check">
              <input
                type="checkbox"
                className="form-check-input appearance-none h-4 w-4 border border-gray-300 rounded-sm bg-white checked:bg-blue-600 checked:border-blue-600 focus:outline-none transition duration-200 mt-1 align-top bg-no-repeat bg-center bg-contain float-left mr-2 cursor-pointer"
                id="exampleCheck2"
              />
              <label className="form-check-label inline-block text-gray-800" htmlFor="exampleCheck2"
                >Remember me
              </label>
            </div>
            <a href="#!" className="text-gray-800">Forgot password?</a>
          </div> */}

                            <div className="text-center lg:text-left">
                                <button
                                    type="submit"
                                    className="inline-block px-7 py-3 bg-blue-600 text-white font-medium text-sm leading-snug uppercase rounded shadow-md hover:bg-blue-700 hover:shadow-lg focus:bg-blue-700 focus:shadow-lg focus:outline-none focus:ring-0 active:bg-blue-800 active:shadow-lg transition duration-150 ease-in-out"
                                >
                                    Login
                                </button>
                                <p className="text-sm font-semibold mt-2 pt-1 mb-0">
                                    Don't have an account?
                                    <a
                                        href="createAccount"
                                        className="text-green-600 hover:text-red-700 focus:text-red-700 transition duration-200 ease-in-out"
                                    > Register</a >
                                </p>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>

    )
}