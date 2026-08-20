//package com.avio.validator;
//
//public class gobalHandler {
//
//	
//	@RestControllerAdvice
//	public class GlobalExceptionHandler {
//
//	    @ExceptionHandler(DataIntegrityViolationException.class)
//	    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
//	        String rootMsg = ex.getMostSpecificCause().getMessage();
//	        String friendlyMessage = "A database error occurred. Please check your input.";
//
//	        if (rootMsg != null) {
//	            if (rootMsg.contains("users_email_key")) {
//	                friendlyMessage = "A user with this email already exists.";
//	            } else if (rootMsg.contains("users_phone") || rootMsg.contains("phone_key")) {
//	                friendlyMessage = "A user with this phone number already exists.";
//	            }
//	        }
//
//	        Map<String, String> body = new HashMap<>();
//	        body.put("errorMessage", friendlyMessage);
//	        body.put("status", "FAILED");
//	        return ResponseEntity.badRequest().body(body);
//	    }
//
//	    @ExceptionHandler(Exception.class)
//	    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
//	        Map<String, String> body = new HashMap<>();
//	        body.put("errorMessage", ex.getMessage());
//	        body.put("status", "FAILED");
//	        return ResponseEntity.badRequest().body(body);
//	    }
//	}
//}
