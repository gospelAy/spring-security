package africa.semicolon.regcrow.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class AppUtils {

    public static final int ONE = 1;
    public static final int FOUR = 4;

    public static final int ZERO = 0;

    public static final String JSON_PATCH_CONSTANT="application/json-patch+json";

    public static  final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_LIMIT = 10;

    public static Pageable buildPageRequest(int page, int items){
        if (page<=ZERO) page=DEFAULT_PAGE_NUMBER;
        if (items<=ZERO) items = DEFAULT_PAGE_LIMIT;
        page-=ONE;
        return PageRequest.of(page, items);
    }
}
