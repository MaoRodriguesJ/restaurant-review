package dev.restaurantreview.modules.validation;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

	private static final Pattern AT_LEAST_A_LETTER = Pattern.compile("^(?=.*[a-zA-Z]).*$");
	private static final Pattern AT_LEAST_A_NUMBER = Pattern.compile("^(?=.*[0-9]).*$");

	@Override
	public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
		return password != null && AT_LEAST_A_LETTER.matcher(password).matches() && AT_LEAST_A_NUMBER.matcher(password).matches();
	}
}
