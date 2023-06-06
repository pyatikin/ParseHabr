import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) {
		Document document;
		try {
			document = Jsoup.connect("https://habr.com/ru/all/").get();
			Element firstArticle = document.selectFirst("article");
			System.out.println(firstArticle.selectFirst("h2").selectFirst("span"));
			//firstArticle.selectFirst("h2").selectFirst("span"); // Получкние заголовка todo убрать span
			Elements elements = firstArticle.siblingElements();

			Stream<Element> articles= elements.stream().filter(e -> e.is("article"));
			System.out.println(articles.findFirst());
		} catch (IOException e) {
			System.out.println("Сбой при подключении к сайту");
		}


	}
}
