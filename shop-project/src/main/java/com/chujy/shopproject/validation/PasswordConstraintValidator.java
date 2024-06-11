package com.chujy.shopproject.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // 초기화 메소드 (초기화 작업 수행 안 해서 비워둠)
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        // 비밀번호가 null 이거나 길이가 8자 미만일 경우 false 반환
        if (password == null || password.length() < 8) {
            return false;
        }

        int count = 0;
        if (password.matches(".*[A-Z].*")) count++;     // 비밀번호에 영문 대문자가 포함되어 있으면 count 중가
        if (password.matches(".*[a-z].*")) count++;     // 비밀번호에 영문 소문자가 포함되어 있으면 count 증가
        if (password.matches(".*\\d.*")) count++;       // 비밀번호에 숫자가 포함되어 있으면 count 증가

        // count가 2 이상일 경우 true 반환, 아니면 false 반환
        return count >= 2;

    }

}
