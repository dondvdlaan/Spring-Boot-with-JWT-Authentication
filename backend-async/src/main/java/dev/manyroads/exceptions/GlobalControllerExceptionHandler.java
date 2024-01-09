package dev.manyroads.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /*
    Method to return Forbidden error when refresh token has expired
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<HttpErrorInfo> handleExpiredRefreshTokenException(
            ServerHttpRequest request,
            ExpiredJwtException ex) {

        return ResponseEntity.status(FORBIDDEN).body(createHttpErrorInfo(FORBIDDEN, request, ex));
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BadCredentialsException.class)
    public @ResponseBody HttpErrorInfo handleBadCredentialsException(
            ServerHttpRequest request, BadCredentialsException ex) {

        return createHttpErrorInfo(UNAUTHORIZED, request, ex);
    }


    private HttpErrorInfo createHttpErrorInfo(
            HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {

        final String path = request.getPath().pathWithinApplication().value();
        final String message = ex.getMessage();

        LOG.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorInfo(httpStatus, path, message);
    }
}
