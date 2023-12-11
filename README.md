<h2>Spring Boot with JWT Authentication</h2>

<p>Building block for JWT authentication with Spring Boot, where user signs in for the first time with username and 
password and for subsequent requests, the user is authenticated with a JWT token in the requesr header.</p>

<p>The JWT token has an expiration date and the backend will issue a 403 error if it detects the expiration. The 403
error is picked up by the axios interceptor at the frontend and a token refresh request is issued to the backend. The
backend will check the refresh token and if valid and not expired, will issue a new token and refresh token back to
the frontend.</p>

<p>If both the JWT token and the refresh token have expired, the frontend will prompt the user for her/his
credentials</p>

<h3>Summary</h3>
<ul>
  <li>Backend: Java Spring Security</li>
  <li>Frontend: REACT</li>
  <li>Database: in memory</li>
</ul>


