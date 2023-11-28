import axios, { Method } from "axios";
//import UpdateHeaderInterceptor from '../interceptor/UpdateHeaderInterceptor'
//import UpdateCookiesInterceptor from "../interceptor/UpdateCookiesInterceptor";
//import Error401RefreshTokenInterceptor from "../interceptor/Error401RefreshTokenInterceptor";

// ********************* Constanten und Typen *********************
const baseUrl = `http://localhost:`;
const port    = process.env.REACT_APP_JAVA_PORT

const encodedData = () =>{

    let user            ="test"
    let pw              ="testPW"
    let userAndPW       = user + ":" + pw
    const encodedData   = window.btoa(userAndPW); 
    return encodedData
}

/**
 * Simplified Api for direct calling server and without callback function
 * 
 * @param   method  [Method]      : http method
 * @param   path    [string]      : relative path to baseUrl
 * @param   data    [JSON]        : optionally data can be send with message
 * @return  axios   [AxiosPromise]: return message to be captured with .then
 */
export function ApiJavaSimplified<T>(method: Method, path: string, headers ={}, data = {}) {

    const config ={
        headers,
        method,
        url: `${baseUrl}${port}/${path}`,
        data,
    } ;

    console.log('API config:',config);

    // ---- Interceptors ----
    //UpdateHeaderInterceptor(axios);
    //Error401RefreshTokenInterceptor(axios);
    //UpdateCookiesInterceptor(axios);

    return axios(config)
    // .then((response: AxiosResponse<T>) => response.data);
    // .then((response: AxiosResponse<T>) => console.log('response.data: ', response.data));
}

