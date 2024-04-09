package com.govtech.serviceImpl;

import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.govtech.dtos.EmailDetail;

import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailTriggerServiceImpl implements EmailTriggerService {

	@Value("${spring.mail.host}")
	private String mailHost;

	@Value("${spring.mail.port}")
	private Integer mailPort;

	@Value("${spring.mail.username}")
	private String userName;

	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${govtech.app.email.trigger.option}")
	private String emailOption;
	
	

	@Override
	public void emailTrigger(EmailDetail emailDetail) {
		try {
			if(null!=emailOption && emailOption.equals("yes")) {
			sendUsingSmtp(emailDetail);
			}
		} catch (Exception e) {
			log.error("Exception in trigger email", e);
		}

	}

	private void sendUsingSmtp(EmailDetail emailDetail) {
		log.info("mail trigger start");
		Properties props = new Properties();
		props.put("mail.host", mailHost);
		props.put("mail.port", mailPort);
		props.put("mail.username", userName);
		props.put("mail.password", password);
		props.put("mail.protocol", "smtp");

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", "*");

		Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(userName));
			msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(emailDetail.getToAddress()));
			msg.setSubject(emailDetail.getSubject());
			msg.setContent(emailDetail.getContent(), "text/html");
			msg.setSentDate(new Date());

			msg.setText(emailDetail.getContent());
			Transport.send(msg);
		} catch (MessagingException e) {
			log.error("Exception in trigger email", e);

		} catch (Exception e) {
			log.error("Exception in trigger email", e);

		}

		log.info("email sent");
	}

}
