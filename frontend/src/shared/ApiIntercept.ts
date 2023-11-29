import axios from "axios";

/**
 * Create axios instance
 */
const instance = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
});

/**
 * Axios instance outgoing request configuration
 */
instance.interceptors.request.use(

    (config) => {
        // Retrieve token from storage
        console.log("interceptors.request Loading jwt")
        const token = localStorage.getItem("jwt");
        
        // Compose header
        if (token) {
            config.headers["Authorization"] = 'Bearer ' + token;
        }
        console.log("interceptors.request config:", config)
        // Return modified config to request
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

/**
 * Axios instance response configuration
 */
instance.interceptors.response.use(
    (res) => {
        return res;
    },
    async (err) => {

        // Remember origial config
        const originalConfig = err.config;

        // Debug
        console.log("in instance.interceptors.response.use err ", originalConfig)

        if (originalConfig.url !== "/login" && err.response) {

            console.log("in if url!= login")

            // Check if Token expired
            if (err.response.status === 403 && !originalConfig._retry) {

                console.log("in err.response.status === 403")

                // Remember first 401 token expired error to avoid looping between fr and be
                originalConfig._retry = true;

                // Clear token from local storage
                localStorage.removeItem("jwt")

                try {
                    // Request new token by way of refreshToken
                    const rs = await instance.post("/refreshtoken", {
                        refreshToken: localStorage.getItem("refreshToken"),
                    });

                    console.log("rs.data: ", rs.data)

                    const { token } = rs.data;
                    localStorage.setItem("jwt", token);

                    // Continue with original config in the response
                    return instance(originalConfig);

                } catch (_error) {
                    console.log("Refresh token failed")
                    return Promise.reject(_error);
                }
            }
        }

        return Promise.reject(err);
    }
);

export default instance;