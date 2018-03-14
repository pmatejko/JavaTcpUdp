package server.dto;

public class Message {
    private final Client author;
    private final String content;
    private final Protocol protocol;

    public Message(Client author, String content, Protocol protocol) {
        this.author = author;
        this.content = content;
        this.protocol = protocol;
    }

    public Client getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
