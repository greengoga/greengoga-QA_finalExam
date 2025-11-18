# Предусловия

Перед запуском убедиться, что установлены:

- Android Studio (последняя стабильная версия)  
- JDK 11  
- Android SDK и Android Emulator API 29 (AVD) или физическое Android-устройство  
- Установлен и настроен ADB (`adb devices` должен видеть эмулятор/устройство)  

---

# Установка зависимостей

1. Скачать [архив с приложением](https://drive.google.com/drive/u/1/folders/14Sl8CAiIzFqtyXx6BAmWVlbu3_cXXzH1), открыть его в Android Studio и установить на устройство.  
2. Открыть проект в Android Studio и дождаться окончания синхронизации Gradle.  
3. Убедиться, что в файле `build.gradle(:app)` подключены правильные версии зависимостей Allure и Espresso.  
4. Запустить Gradle сборку проекта:

```bash
./gradlew clean assembleDebug assembleDebugAndroidTest
```

---

# Запуск тестов

## Способ 1: Через Android Studio

- Выбрать любой тестовый класс (`AboutTest`, `QuoteTest`, `ControlPanelTest` и др.) и нажать “Run” рядом с аннотацией `@Test` или над именем класса.

## Способ 2: Через командную строку

```bash
./gradlew connectedDebugAndroidTest
```

---

# Структура тестов

Тесты реализованы в следующих классах:

- `AboutTest.kt` — проверка экрана “О приложении”  
- `QuoteTest.kt` — проверка цитат  
- `ControlPanelTest.kt` — панель управления блоком новостей (создание, редактирование, удаление, фильтрация)  
- `AuthTest.kt` — проверка авторизации  
- `NewsTest.kt` — проверка страницы новостей  

---

# Отчёты Allure

1. После успешного прохождения тестов Allure-результаты сохраняются на SD-карте устройства (эмулятора).  
2. Для генерации HTML-отчёта:

   Перенести папку `allure-results` в корень проекта и набрать в командной строке:

   ```bash
   allure serve
   ```
