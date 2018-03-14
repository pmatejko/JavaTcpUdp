package server;

public class Message {
    private final ClientThread author;
    private final String content;

    public Message(ClientThread author, String content) {
        this.author = author;
        this.content = content;
    }

    public ClientThread getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
