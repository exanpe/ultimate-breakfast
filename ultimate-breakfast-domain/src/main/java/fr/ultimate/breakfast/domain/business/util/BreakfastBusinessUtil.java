package fr.ultimate.breakfast.domain.business.util;

import org.apache.commons.lang.RandomStringUtils;

public class BreakfastBusinessUtil
{
    private static final int PASSWORD_GEN_LENGTH = 6;

    public static String generatePassword()
    {
        return RandomStringUtils.random(PASSWORD_GEN_LENGTH, true, true);
    }
}
