import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
	public static void main(String[] args) {
		Document document;
		try {
				/*System.setProperty("http.proxyHost", "1.0.205.87");
				System.setProperty("http.proxyPort", "8080");*/
			document = Jsoup.connect("https://habr.com/ru/all/").get();
			Element firstArticle = document.selectFirst("article");
			//System.out.println(firstArticle.selectFirst("h2").selectFirst("span"));
			//firstArticle.selectFirst("h2").selectFirst("span"); // Получкние заголовка todo убрать span
			Elements elements = firstArticle.siblingElements().stream().filter(element -> element.is("article")).collect(Collectors.toCollection(Elements::new));

			elements.add(0, firstArticle);
			elements.forEach(Main::show);
			
			//elements.forEach(e->System.out.println(e.select("h2").select("span"))); //Название статьи
			/*System.out.println("Статья: " + elements.get(0).select("h2").select("span").text());
			System.out.println("Автор: " + elements.get(0).select(".tm-user-info__username").first().text());
			System.out.println("Время чтения: " + elements.get(0).select(".tm-article-reading-time__label").first().text());
			System.out.println("Количество просмотров: " + elements.get(0).select(".tm-icon-counter__value").first().text());
			System.out.println("Технологии : " + elements.get(0).select(".tm-article-snippet__hubs").first().text().replace("*", ""));
			System.out.println("Лэйбл: " + elements.get(0).select(".tm-article-snippet__labels-container").first().text());*/
			//elements.forEach(e->System.out.println(e.select("div").select("div").select("div").select("span")));

			//Stream<Element> articles= elements.stream().filter(e -> e.is("article"));
			//System.out.println(articles.findFirst());


			//Elements elements = document.body().children();
			//elements.forEach(System.out::println);

		} catch (IOException e) {
			System.out.println("Сбой при подключении к сайту");
		}


	}

	public static void show(Element element){
		System.out.println("Статья: " + element.select("h2").select("span").text());
		System.out.println("Автор: " + Objects.requireNonNull(element.select(".tm-user-info__username").first()).text());
		System.out.println("Время чтения: " + Objects.requireNonNull(element.select(".tm-article-reading-time__label").first()).text());
		System.out.println("Количество просмотров: " + Objects.requireNonNull(element.select(".tm-icon-counter__value").first()).text());
		System.out.println("Технологии : " + Objects.requireNonNull(element.select(".tm-article-snippet__hubs").first()).text().replace("*", ""));
		if(!Objects.isNull(element.selectFirst(".tm-article-snippet__labels-container")))
			System.out.println("Лэйбл: " + Objects.requireNonNull(element.select(".tm-article-snippet__labels-container").first()).text());
		else
			System.out.println("Лэйбл: " + null);
	}
}
