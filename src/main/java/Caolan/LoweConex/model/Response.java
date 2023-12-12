package Caolan.LoweConex.model;

public class Response {
    private String message;

    public Response(String message) {
        this.message = message;
    }

    public String getResult() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
