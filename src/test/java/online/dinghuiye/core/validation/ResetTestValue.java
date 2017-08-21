package online.dinghuiye.core.validation;

/**
 * @author Strangeen on 2017/08/16
 */
public class ResetTestValue {

    public static void reset() {
        PhoneUniqueValidator.resetCache();
        UsernameUniqueValidator.resetCache();
        CertCardUniqueValidator.resetCache();
    }
}
