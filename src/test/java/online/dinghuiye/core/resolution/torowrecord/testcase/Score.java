package online.dinghuiye.core.resolution.torowrecord.testcase;

import online.dinghuiye.core.annotation.excel.SheetTitleName;

/**
 * @author Strangeen on 2017/08/17
 */
public class Score {

    @SheetTitleName("学科")
    private String scoreName;

    @SheetTitleName("分数")
    private Integer score;

    @Override
    public String toString() {
        return "Score{" +
                "scoreName='" + scoreName + '\'' +
                ", score=" + score +
                '}';
    }
}
