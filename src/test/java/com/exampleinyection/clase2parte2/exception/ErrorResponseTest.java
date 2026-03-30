package com.exampleinyection.clase2parte2.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private static class ErrorResponseChildStrict extends ErrorResponse {
        ErrorResponseChildStrict(int status, String message) {
            super(status, message);
        }
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    private static class ErrorResponseChild extends ErrorResponse {
        ErrorResponseChild(int status, String message) {
            super(status, message);
        }
    }

    @Test
    void testErrorResponse() {
        ErrorResponse response1 = new ErrorResponse(404, "Not Found");
        ErrorResponse response2 = new ErrorResponse();
        response2.setStatus(404);
        response2.setMessage("Not Found");

        assertEquals(404, response1.getStatus());
        assertEquals("Not Found", response1.getMessage());

        assertEquals(response1.getStatus(), response2.getStatus());
        assertEquals(response1.getMessage(), response2.getMessage());

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertTrue(response1.toString().contains("404"));
        assertTrue(response1.toString().contains("Not Found"));

        ErrorResponse response3 = new ErrorResponse(500, "Error");
        assertNotEquals(response1, response3);
        assertNotEquals(response1, null);
        assertNotEquals(response1, new Object());

        assertEquals(response1, response1);

        ErrorResponse nullMessage1 = new ErrorResponse(404, null);
        ErrorResponse nullMessage2 = new ErrorResponse(404, null);
        assertEquals(nullMessage1, nullMessage2);
        assertNotEquals(nullMessage1, response1);
        assertNotEquals(response1, nullMessage1);

        ErrorResponse diffStatus = new ErrorResponse(500, "Not Found");
        assertNotEquals(response1, diffStatus);

        assertNotNull(nullMessage1.hashCode());

        assertTrue(nullMessage1.toString().contains("null"));

        ErrorResponse strictChild = new ErrorResponseChildStrict(404, "Not Found");
        assertFalse(response1.equals(strictChild));
        assertTrue(strictChild.equals(response1));

        ErrorResponse normalChild = new ErrorResponseChild(404, "Not Found");
        assertEquals(response1, normalChild);
        assertEquals(normalChild, response1);
        assertEquals(response1.hashCode(), normalChild.hashCode());
    }
}
