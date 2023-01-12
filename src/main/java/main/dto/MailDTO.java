package main.dto;

import java.io.Serializable;

public record MailDTO(String body, String toEmail, String subject)implements Serializable {
}
