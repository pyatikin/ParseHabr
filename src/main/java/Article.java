public class Article {
    String title;
    String author;
    String timeToRead;
    String views;
    String topics;
    String label;

    public Article(String title, String author, String timeToRead, String views, String topics, String label) {
        this.title = title;
        this.author = author;
        this.timeToRead = timeToRead;
        this.views = views;
        this.topics = topics;
        this.label = label;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", timeToRead='" + timeToRead + '\'' +
                ", views='" + views + '\'' +
                ", topics='" + topics + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
