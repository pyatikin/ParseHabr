import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Main {

	static Integer amount;
	static Integer page = 1;
	static StatefulBeanToCsv<Article> beanWriter;
	public static void main(String[] args) {

		try (Scanner scanner = new Scanner(System.in);
			 FileWriter writer = new FileWriter("Articles.csv");
			 CSVWriter csvWriter = new CSVWriter(writer)) {

			makeWriter(csvWriter); // Создаем объекты для записи в файл csv
			List<Article> articleList = new ArrayList<>(20); // На одной странице 20 статей
			boolean flag = true; // Флаг проверки верности введенных данных
			while (flag) {
				try {
					System.out.println("Введите количество записей: ");
					amount = scanner.nextInt();
					while (amount > 0) {
						parseAmount(articleList); // Получение данных о статьях
						writeCSV(articleList); // запись данных о статьях
						articleList.clear(); // очистка данных с текущей страницы
					}
					flag = false;
				} catch (Exception e) {
					System.out.println("Неверно введены данные, попробуйте снова");
				}
			}
		} catch (IOException e) {
			System.out.println("Укажите другое имя файла");
		}
	}

	public static void parseAmount(List<Article> articleList) {
		try {
			Document document = Jsoup.connect("https://habr.com/ru/all/" + (page > 1 ? ("page" + page) : "")).get(); //Получение новой страницы
			Elements elements = document.select("article");// Получение всех статей
			for (Element element : elements) { // Добавление последующих статей
				articleList.add(getArticle(element));
			}
			amount -= articleList.size();
			page++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Article getArticle(Element element) {
		//Получение нужных данных из html разметки статьи
		return new Article(
				element.select("h2").select("span").text(),
				Optional.ofNullable(element.selectFirst(".tm-user-info__username")).map(Element::text).orElse(""),
				Optional.ofNullable(element.selectFirst(".tm-article-reading-time__label")).map(Element::text).orElse(""),
				Optional.ofNullable(element.selectFirst(".tm-icon-counter__value")).map(Element::text).orElse(""),
				Optional.ofNullable(element.selectFirst(".tm-article-snippet__hubs")).map(Element::text).orElse("").replace("*", ""),
				Optional.ofNullable(element.selectFirst(".tm-article-snippet__labels-container")).map(Element::text).orElse("")
		);
	}

	public static void makeWriter(CSVWriter csvWriter) {
		// Создание объектов и натройка разметки для записи в csv
		ColumnPositionMappingStrategy<Article> mappingStrategy = new ColumnPositionMappingStrategy<>();
		mappingStrategy.setType(Article.class);
		String[] columns = Arrays.stream(Article.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
		csvWriter.writeNext(columns);
		mappingStrategy.setColumnMapping(columns);
		StatefulBeanToCsvBuilder<Article> builder = new StatefulBeanToCsvBuilder<>(csvWriter);
		beanWriter = builder.withMappingStrategy(mappingStrategy).build();
	}

	public static void writeCSV(List<Article> articleList) {
		try {
			beanWriter.write(articleList);
		} catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
			e.printStackTrace();
		}
	}
}
