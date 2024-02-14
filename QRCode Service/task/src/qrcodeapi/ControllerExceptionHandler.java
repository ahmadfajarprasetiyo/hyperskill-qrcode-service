package qrcodeapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {

        ErrorResponse body = new ErrorResponse(e.getMessage());
        HttpStatusCode httpCode = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(body, httpCode);
    }
}
