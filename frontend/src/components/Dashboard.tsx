import { useState } from "react";
import { Login } from "./Login";
import axiosInstance from "../shared/ApiIntercept";
import { useNavigate } from "react-router-dom";


/**
 * Landing page
 * @returns 
 */
export const Dashboard = () => {

    // ********** Constants and variables **********
    // Hooks
    const [token, setToken] = useState(localStorage.jwt);
    const [error, setError] = useState("")
    const navigate = useNavigate();

    // If login token is not there, go to login
    console.log("Dashboard token: ", token)
    if (!token) return <Login setToken={setToken} />;

    // ---- Event triggers ----
    const onTest = () => {

        console.log(" in /test2")
        axiosInstance.get("/test2")
            .then((res: { data: any; }) => {

                console.log("\n *********** Testing**********")
                console.log("onTest res.data: ", res.data)
            })
            .catch((err: any) => {
                console.log("In Test err", err.message)

                // Check if token in storage is empty, then direct to login page
                if (localStorage.getItem("jwt") == null){
                    console.log("onTest catch in if" );
                    navigate(0)
                } 
            })

    }
    return (
        <>
            <h1 className="text-5xl text-center py-10">DASHBOARD</h1>
            <p className="text-1xl text-center py-5">JWT Authentication</p>
            <div className="text-1xl text-center py-5">
                <button
                    onClick={onTest}
                    type="button">Test with JWT Authentication
                </button>
            </div>

            <p>{error}</p>
        </>

    )
}