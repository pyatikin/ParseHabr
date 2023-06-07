import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
	static FileWriter writer;
	static CSVWriter csvWriter;
	static Integer page = 1;
	static StatefulBeanToCsv beanWriter;
	public static void main(String[] args) {
		try {
			writer = new FileWriter("Articles.csv");
			csvWriter = new CSVWriter(writer);
			ColumnPositionMappingStrategy mappingStrategy=
					new ColumnPositionMappingStrategy();
			mappingStrategy.setType(Article.class);

			String [] columns = Arrays.stream(Article.class.getDeclaredFields()).map(field -> field.getName()).toArray(String [] ::new);/*.forEach(field -> System.out.println(field.getName()));*/
			mappingStrategy.setColumnMapping(columns);
			StatefulBeanToCsvBuilder<Article> builder=
					new StatefulBeanToCsvBuilder<>(writer);
			beanWriter =
					builder.withMappingStrategy(mappingStrategy).build();
			csvWriter.writeNext(columns);

		} catch (IOException e) {
			System.out.println("Укажите другое имя файла");
		}
		/*LocalDateTime dateTime = LocalDateTime.of(2023, 06,6,13,52, 54, 000);
		System.out.println(dateTime.isBefore(LocalDateTime.now()));*/
		List<Article> articleList = new ArrayList<>(20);
		boolean flag = true;
		while (flag) {
			try {
				Scanner scanner = new Scanner(System.in);
				int amount;
				System.out.println("Введите количество записей: ");
				amount = scanner.nextInt();
				while (amount > 0) {
					amount = parseAmount(articleList, amount);
					writeCSV(articleList);
					articleList.clear();
					page++;
				}
				System.out.println(amount);
				flag = false;
			} catch (Exception e) {
				System.out.println("Неверно введены данные, попробуйте снова");
			}
		}

		/*Document document;*/
		//try {
			/*document = Jsoup.connect("https://habr.com/ru/all/").get();
			Element firstArticle = document.selectFirst("article");
			//System.out.println(firstArticle.selectFirst("h2").selectFirst("span"));
			//firstArticle.selectFirst("h2").selectFirst("span"); // Получкние заголовка todo убрать span
			Elements elements = firstArticle.siblingElements().stream().filter(element -> element.is("article")).collect(Collectors.toCollection(Elements::new));

			elements.add(0, firstArticle);
			elements.forEach(Main::show);*/




			/*List<Article> articleList = new ArrayList<>(50);
			parse(articleList);
			writeCSV(articleList);*/




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

		/*} catch (IOException e) {
			System.out.println("Сбой при подключении к сайту");
		}*/


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
	public static Article getArticle(Element element){
		String label = null;
		if(!Objects.isNull(element.selectFirst(".tm-article-snippet__labels-container")))
			label =  Objects.requireNonNull(element.select(".tm-article-snippet__labels-container").first()).text();
		return new Article(element.select("h2").select("span").text(),
				Objects.requireNonNull(element.select(".tm-user-info__username").first()).text(),
				Objects.requireNonNull(element.select(".tm-article-reading-time__label").first()).text(),
				Objects.requireNonNull(element.select(".tm-icon-counter__value").first()).text(),
				Objects.requireNonNull(element.select(".tm-article-snippet__hubs").first()).text().replace("*", ""),
				label);
	}

	public static int parseAmount(List<Article> articleList, Integer amount) {
		try {
			Document document = Jsoup.connect("https://habr.com/ru/all/" + (page>1?("page" + page):"")).get(); //Получение главной страницы
			Element firstArticle = document.selectFirst("article"); // Получение первого элемента на странице, который содержит статью
			Elements elements = firstArticle.
					siblingElements().
					stream().
					filter(element -> element.is("article")).collect(Collectors.toCollection(Elements::new)); // Получение остальных статьей и фильтрация от элементов на том же уровне, но которые не являются статьей
			elements.add(0, firstArticle);
			for (Element element: elements) {
				articleList.add(getArticle(element));
			}
			return(amount - articleList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}

	public static void writeCSV(List<Article> articleList) {

		try {

			beanWriter.write(articleList);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
