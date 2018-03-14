package server;

public class Message {
    private final Client author;
    private final String content;

    public Message(Client author, String content) {
        this.author = author;
        this.content = content;
    }

    public Client getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
