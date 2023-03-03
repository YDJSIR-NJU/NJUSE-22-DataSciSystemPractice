package org.example.gateway.service.validation;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class UrlWhiteList {
    private static List<String> patternList;

    static {
        patternList = new ArrayList<>();
        patternList.add("/userservice/user/login/?");
        patternList.add("/userservice/user/register/?");
    }

    public static boolean allowUrl(String url) {
        for (String pattern : patternList) {
            if (Pattern.matches(pattern, url)) {
                log.info("URL {} matches pattern {}", url, pattern);
                return true;
            }
        }
        log.info("URL {} matches no pattern", url);
        return false;
    }

}
