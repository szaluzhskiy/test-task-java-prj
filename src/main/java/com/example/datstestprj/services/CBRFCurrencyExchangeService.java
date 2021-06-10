package com.example.datstestprj.services;

import com.example.datstestprj.domain.Currency;
import com.example.datstestprj.exception.WrongCurrencyException;
import com.example.datstestprj.repository.CurrencyRepository;
import com.example.datstestprj.types.CurrencyType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CBRFCurrencyExchangeService {
    Map<CurrencyType, XPathExpression> xPathCurrency;
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    final String cbrURL = new String("http://www.cbr.ru/scripts/XML_daily.asp?date_req=");
    final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    final String expression = "//CharCode[text()=\"{0}\"]/../Value";
    final CurrencyRepository currencyRepository;
    final List<NotificationService> notificationServices;

    @PostConstruct
    private void init() throws XPathExpressionException {
        try {
            xPathCurrency = Map.of(
                    CurrencyType.EUR, XPathFactory.newInstance().newXPath().compile(
                            MessageFormat.format("//CharCode[text()=\"{0}\"]/../Value", CurrencyType.EUR)),
                    CurrencyType.GBP, XPathFactory.newInstance().newXPath().compile(
                            MessageFormat.format("//CharCode[text()=\"{0}\"]/../Value", CurrencyType.GBP)),
                    CurrencyType.USD, XPathFactory.newInstance().newXPath().compile(
                            MessageFormat.format("//CharCode[text()=\"{0}\"]/../Value", CurrencyType.USD))
            );
        } catch (XPathExpressionException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public String getExchangeRate(CurrencyType currencyType, LocalDate dateOn) {
       Currency currency = currencyRepository.findCurrencyBySourceCurrencyAndDateOnEquals(currencyType, dateOn);
       if (currency != null) {
           return currency.getRate().toString();
       } else if (currencyType == CurrencyType.RUB) {
          throw new WrongCurrencyException(CurrencyType.RUB.name());
       }
       return "";
    }

    @Scheduled(cron = "0 0 0,01 * * *")
    public void parse(LocalDate processingDate) throws IOException, InterruptedException, SAXException, ParserConfigurationException, XPathExpressionException, ParseException {
        processingDate =  processingDate == null ? LocalDate.now() : processingDate;
        String strDate = processingDate.format(DATE_FORMAT);
        String url = cbrURL.concat(strDate);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document xmlDocument = builder.parse(new InputSource(new StringReader(response.body())));
        for (CurrencyType currencyType : CurrencyType.values()) {
            //skip RUB
            if (currencyType != CurrencyType.RUB) {
                String value = (String) xPathCurrency.get(currencyType).evaluate(xmlDocument, XPathConstants.STRING);

                Number rate = NumberFormat.getNumberInstance().parse(value.replace(',', '.'));

                Currency c = new Currency();
                c.setBaseCurrency(CurrencyType.RUB);
                c.setSourceCurrency(currencyType);
                c.setRate(rate.doubleValue());
                c.setDateOn(processingDate);
                currencyRepository.save(c);

                notificationServices.stream().forEach(ns -> ns.notify(currencyType + " rate = " + value));
            }
            //org.hibernate.exception.ConstraintViolationException
        }
    }
}
