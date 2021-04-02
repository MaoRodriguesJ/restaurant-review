package dev.restaurantreview.modules.mail.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailerSender {

	@Autowired
	private JavaMailSender mailSender;

	//	@Setter
	//	@Value("${app.url}")
	private String appUrl = "https://dev-restaurant-review.herokuapp.com";

	//	@Setter
	//	@Value("${spring.mail.origin}")
	private String mailOrigin = "3628561f98-429bdf@inbox.mailtrap.io";

	public void sendResetPasswordMail(String mailTo, String resetToken) throws Exception {
		MimeMessage message = this.mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.toString());
		helper.setSubject("Restaurant review - Reset password");
		try {
			helper.setFrom(this.mailOrigin, "Restaurant Review");
		} catch (UnsupportedEncodingException e) {
			helper.setFrom(this.mailOrigin);
		}
		helper.setTo(mailTo);
		String content = String.format("To reset the password follow the link: <a href=\"%s/reset-password/%s/%s\">Reset Password</a>", this.appUrl, resetToken, mailTo);
		helper.setText(content, true);
		this.mailSender.send(message);
	}
}
