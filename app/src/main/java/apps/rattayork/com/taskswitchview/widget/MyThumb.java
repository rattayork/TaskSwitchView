package apps.rattayork.com.taskswitchview.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class MyThumb {


    //SwitchView instance
    TaskSwitchView taskSwitchView;

    //Boundaries of Drawing
    public float minCenterX;
    public float maxCenterX;
    public float maxRadius;
    public float minRadius;
    public float bufferRadius;
    public float minTopY;
    public float minBottomY;
    public float maxTopY;
    public float maxBottomY;

    //Thumb size and position
    public float thumbWidth;
    public float thumbHeight;
    public float thumbStart;
    public float thumbEnd;
    public float thumbTop;
    public float thumbBottom;
    public float thumbCenterPositionX;
    public int thumbCenterPositionY;
    private float posStartXscaled;
    private float posEndXscaled;
    private float currentXscaled;
    public int clickTolerence;


    //Thumb Painting
    private Paint thumbPaint = new Paint();
    private int thumbColor;





    MyThumb(TaskSwitchView taskSwitchView){

        this.taskSwitchView = taskSwitchView;

        this.minCenterX = taskSwitchView.minCenterX;
        this.maxCenterX = taskSwitchView.maxCenterX;
        this.maxRadius = taskSwitchView.maxRadius;
        this.minRadius = taskSwitchView.minRadius;
        this.minTopY = taskSwitchView.minTopY;
        this.minBottomY = taskSwitchView.minBottomY;
        this.maxTopY = taskSwitchView.maxTopY;
        this.maxBottomY = taskSwitchView.maxBottomY;
        this.thumbWidth = taskSwitchView.widthQuarter;
        this.thumbHeight = taskSwitchView.getHeight();


        setThumbPaint();

        posStartXscaled = taskSwitchView.getPosXStartScaled();
        posEndXscaled = taskSwitchView.getPosXEndScaled();


    }

    private void setThumbPaint(){
        thumbColor = taskSwitchView.DEFAULT_THUMB_COLOR;
        thumbPaint.setColor(thumbColor);
        thumbPaint.setStyle(Paint.Style.STROKE);
        thumbPaint.setStrokeWidth(8);
    }

    /*
    Set position of X and Boundery limit of left and right side
    */
    //For manually setXposition for Thumb

    public void setPosition(float x){

        if( x < taskSwitchView.quarterOfWidth.firstQuarterCenter){
            thumbCenterPositionX = taskSwitchView.quarterOfWidth.firstQuarterCenter;
        }
        else if(x > taskSwitchView.quarterOfWidth.fourthQuarterCenter){
            thumbCenterPositionX = taskSwitchView.quarterOfWidth.fourthQuarterCenter;
        }
        else{
            thumbCenterPositionX = x;
        }

        calculateCurrentPosXscaled();
        setThumbCoordinate();

    }

    public void setThumbCoordinate(){

        thumbStart = thumbCenterPositionX - (thumbWidth/2);
        thumbEnd = thumbCenterPositionX + (thumbWidth/2);
        thumbTop = 0;
        thumbBottom = taskSwitchView.getHeight();
        thumbCenterPositionY = (taskSwitchView.getHeight())/2;

    }

    //For manually setXposition for Thumb in manner/fashion of Scaled from Start to End (0,0~1,0)

    public void setCurrentXscaled(float positionScaled){
        currentXscaled = positionScaled;
    }

    //For self setXposition of Thumb in manner/fashion of Scaled from Start to End (0,0~1,0)

    private float calculateCurrentPosXscaled(){
        currentXscaled = ((thumbCenterPositionX * posEndXscaled)/taskSwitchView.getWidth());
        return currentXscaled;
    }


    public boolean isHitThumb(float x, float y){


        return x > (float) (thumbStart-clickTolerence) && x < (float) (thumbEnd+clickTolerence)
                && y > (float)(thumbTop-clickTolerence) && y < (float)(thumbEnd+clickTolerence);

    }


    /*
    Main method for Drawing Thumb itself will be called by onDraw from SwitchView Obj.
     */

    public void drawThumb(Canvas canvas){

        if(thumbCenterPositionX < taskSwitchView.quarterOfWidth.secondQuarterEnd) {

            // canvas.drawCircle(thumbCenterPositionX, thumbCenterPositionY, (float) (thumbWidth-(thumbWidth*0.1))/2, thumbPaint);
            canvas.drawCircle(thumbCenterPositionX, thumbCenterPositionY, (float) (thumbWidth-(thumbWidth*calculateCurrentPosXscaled()))/2, thumbPaint);
        }
        else{
            canvas.drawLine(thumbCenterPositionX
                    ,minTopY-(maxTopY*calculateCurrentPosXscaled())
                    ,thumbCenterPositionX
                    ,minBottomY+(maxTopY*calculateCurrentPosXscaled())
                    ,thumbPaint);
        }
    }

    /*
    Drawing Thumb on Dragging Mode
     */
    public void drawThumbOnDragging(Canvas canvas){
        canvas.drawCircle(thumbCenterPositionX, thumbCenterPositionY, (float) (thumbWidth-(thumbWidth*0.1))/2, thumbPaint);
    }


    //Use this method for draw switch off

    public void drawThumbOff(Canvas canvas){
        canvas.drawCircle(thumbCenterPositionX, thumbCenterPositionY, (float) (thumbWidth-(thumbWidth*0.1))/2, thumbPaint);
    }



    //Use this method for draw switch On

    public void drawThumbOn(Canvas canvas){
        canvas.drawLine(thumbCenterPositionX, maxTopY, thumbCenterPositionX ,maxBottomY, thumbPaint);
    }



    //Use for Test switch off halfway

    public void drawThumbOffHaftWay(Canvas canvas){
        canvas.drawCircle(thumbCenterPositionX, thumbCenterPositionY, (float) (thumbWidth-(thumbWidth*0.8))/2, thumbPaint);
    }


    //Use this method for test switch on Halfway

    public void drawThumbOnHaftWay(Canvas canvas){
        canvas.drawLine(thumbCenterPositionX, minTopY, thumbCenterPositionX, minBottomY, thumbPaint);
    }



}
