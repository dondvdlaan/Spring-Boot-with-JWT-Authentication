<h2>Spring Boot with JWT Authentication</h2>

<p>Building block for JWT authentication with Spring Boot, where user signs in for the first time with username and 
password and for subsequent requests, the user is authenticated with a JWT token in the request header.</p>

<p>The JWT token has an expiration date and the backend will issue a 401 unauthorized error if it detects the 
expiration. The 401 error is picked up by the axios interceptor at the frontend and a token refresh request is 
issued to the backend. The backend will check the refresh token and if valid and not expired, will issue a new token
and refresh token back to the frontend.</p>

<p>If both the JWT token and the refresh token have expired, the frontend will prompt the user for her/his
credentials</p>

<h3>Backend for synchronous and asynchronous communication</h3>

<p>Two types of backends are available: one for synchronous communication on the Spring Tomcat server and on
for asynchronous communication on the Spring Reactor Netty server. The main difference between the two approaches is
that the Reactor Netty server is not based anymore on the Java Servlet container, but an event-driven non-blocking 
server framework. Spring uses the Reactor library for endpoint communication, communication between classes and 
communication with databases for example.</p>

<h3>Summary</h3>
<ul>
  <li>Backend: Java Spring Security</li>
  <li>Frontend: REACT</li>
  <li>Database: MySQL, MongoDB</li>
  <li>Containers: Docker</li>
</ul>


