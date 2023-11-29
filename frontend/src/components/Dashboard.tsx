import { useState } from "react";
import { Login } from "./Login";


/**
 * Landing page
 * @returns 
 */
export const Dashboard = () =>{

// ********** Constants and variables **********
// Hooks
const [token, setToken] = useState(localStorage.jwt);

// If login token is not there, go to login
if(!token) return <Login setToken={setToken} />;

return(

        <h1 className="text-5xl text-center py-10">DASHBOARD</h1>
    )
}