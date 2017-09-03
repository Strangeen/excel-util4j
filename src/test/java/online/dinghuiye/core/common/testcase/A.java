package online.dinghuiye.core.common.testcase;

import online.dinghuiye.core.annotation.convert.BlankToNull;
import online.dinghuiye.core.annotation.convert.DateFormat;

/**
 * @author Strangeen on 2017/08/16
 */
public class A {

    @BlankToNull
    private int a;

    @BlankToNull
    @DateFormat("yyyy")
    private String b;
}
