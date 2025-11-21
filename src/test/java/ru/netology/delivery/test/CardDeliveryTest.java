package ru.netology.delivery.test;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    //добавляем листенер в тестовый класс перед выполнением тестов
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    //удаляем листенер после выполнения всех тестов
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void should() {
        var registration = DataGenerator.Registration.generateUser("ru");

        var dateFirst = DataGenerator.generateData(5);
        var dataSecond = DataGenerator.generateData(10);

        $("[data-test-id='city'] input").setValue(registration.getCity());
        $("[data-test-id='date'] input").press(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dateFirst);
        $("[data-test-id='name'] input").setValue(registration.getName());
        $("[data-test-id='phone'] input").setValue(registration.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.text("Запланировать")).click();
        $("[data-test-id='success-notification']")
                .should(Condition.text("Успешно!"))
                .should(Condition.text("Встреча успешно запланирована на " + dateFirst));

        $("[data-test-id='date'] input").press(Keys.CONTROL + "a", Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dataSecond);
        $$("button").find(Condition.text("Запланировать")).click();
        $("[data-test-id='replan-notification']")
                .should(Condition.text("Необходимо подтверждение"))
                .should(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .should(Condition.visible);
        $$("button").find(Condition.text("Перепланировать")).click();
        $("[data-test-id='success-notification']")
                .should(Condition.text("Успешно!"))
                .should(Condition.text("Встреча успешно запланирована на " + dataSecond));
    }
}

