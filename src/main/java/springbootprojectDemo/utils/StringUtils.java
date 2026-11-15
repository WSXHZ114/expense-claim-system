package springbootprojectDemo.utils;

import springbootprojectDemo.BusinessException;

public final class StringUtils {
    private StringUtils() {
    }

    public static void assess(String str, String strName, int min, int max) {
        if (str.isBlank()) throw new BusinessException(strName + "不能为空");
        if (str.trim().length() > max) throw new BusinessException(strName + "最长" + max + "字");
        if (str.trim().length() < min) throw new BusinessException(strName + "最短" + min + "字");
    }
}
