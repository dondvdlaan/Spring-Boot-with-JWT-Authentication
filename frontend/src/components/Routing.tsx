import { ReactElement } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { Login } from "./Login";
import { Dashboard } from "./Dashboard";




export default function Routing(): ReactElement {
    return (
        <Routes>
            <Route path="/dashboard" element={<Dashboard />} />

            <Route path="/" element={<Navigate to="/dashboard" />} />

        </Routes>
    );
}