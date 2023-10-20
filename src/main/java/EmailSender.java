import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public static void main(String[] args) {
        // Настройки почтового сервера (SMTP)
        final String username = "...@gmail.com"; // g-mail отправителя (ваш)
        final String password = "**** **** **** ****"; // указывайте пароль НЕ от почтового клиента, а ПАРОЛЬ ПРИЛОЖЕНИЯ! (см ниже)
        String smtpHost = "smtp.gmail.com"; // При использовании других почтовых сервисов, настраивайте smtp (см документацию на оф сайте яндекс, мейл и дт..)
        int smtpPort = 587; // если не меняли настройки подключений, 587 - smtp-порт по умолчанию

        // Настройки свойств
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        // Создание сеанса
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Создание сообщения
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("example@gmail.com")); // email получателя (любой необходимый)
            message.setSubject("Тест"); // тема письма
            message.setText("Проверка клиента"); // тело письма

            // Отправка сообщения
            Transport.send(message);

            System.out.println("Письмо успешно отправлено.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

/* При использовании старой классической схемы входа через имя-пароль почтового сервиса произойдет следующее:
программа завершается с кодом 0
и ошибкой "javax.mail.AuthenticationFailedException: 535-5.7.8 Username and Password not accepted.
Проблема в отмене гуглом функции поддержки менее безопасных приложений с 2022 года.

Решение проблемы:
1) проверяем, подключена ли ли двухэтапная аутентификация (в противном случае - подключаем).
2) после подключения двухэтапной аутентификации находим меню "Пароли приложений"
(без подключения аутентификации данное меню отображаться не будет).
3) Вписываем название приложения (App name).
4) Гугл автоматически генерирует 16-значный пароль. В описании вы увидите "Его не нужно запоминать.
Так же просим вас не записывать его и никому не показывать." Незамедлительно ЗАПИШИТЕ его: пароль будет необходим
для установления доступа к аккаунту. Именно этот 16-значный пароль необходимо вписать в качестве "final String password".
Ps.: гугл генерирует 16-значный пароль с тремя символами пробелов: Google Authenticator к данным пробелам не чувствителен.
Хотите - оставляйте пробелы для лучшей читабельности, хотите - удаляйте для компанктности кода.
*/
