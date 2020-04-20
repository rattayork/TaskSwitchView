package apps.rattayork.com.taskswitchview.widget;

public class QuarterOfWidth {

    public int firstQuarterStart;
    public int firstQuarterEnd;
    public int firstQuarterCenter;

    public int secondQuarterStart;
    public int secondQuarterEnd;
    public int secondQuarterCenter;

    public int thirdQuarterStart;
    public int thirdQuarterEnd;
    public int thirdQuarterCenter;

    public int fourthQuarterStart;
    public int fourthQuarterEnd;
    public int fourthQuarterCenter;


    public QuarterOfWidth (int QuarterWidth){

        firstQuarterStart = 0;
        firstQuarterEnd = 0 + QuarterWidth;
        firstQuarterCenter = (firstQuarterStart + firstQuarterEnd)/2;

        secondQuarterStart = firstQuarterEnd+1;
        secondQuarterEnd = secondQuarterStart + QuarterWidth;
        secondQuarterCenter = (secondQuarterStart + secondQuarterEnd)/2;

        thirdQuarterStart = secondQuarterEnd+1;
        thirdQuarterEnd = thirdQuarterStart + QuarterWidth;
        thirdQuarterCenter = (thirdQuarterStart + thirdQuarterEnd)/2;

        fourthQuarterStart = thirdQuarterEnd + 1;
        fourthQuarterEnd = fourthQuarterStart + QuarterWidth;
        fourthQuarterCenter = (fourthQuarterStart + fourthQuarterEnd)/2;


    }


}
