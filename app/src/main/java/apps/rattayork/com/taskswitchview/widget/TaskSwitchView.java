package apps.rattayork.com.taskswitchview.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Switch;

import java.time.Duration;

import apps.rattayork.com.taskswitchview.R;


public class TaskSwitchView extends Switch {


    Context context;

    /*
    Section of constants
     */
    private static final int ANIMATION_DURATION = 300;

    private static final int DEFAULT_WIDTH = 120;      //width of SwitchButton
    private static final int DEFAULT_HEIGHT = (int)(DEFAULT_WIDTH *0.4);

    private static final int DEFAULT_SWITCH_OFF_COLOR = 0xFF7C7C7C;
    private static final int DEFAULT_SWITCH_ON_COLOR = 0xFFD1D1D1;
    private static final int DEFAULT_SWITCH_OFF_STROKE_COLOR = 0xFF008577;
    private static final int DEFAULT_SWITCH_ON_STROKE_COLOR = 0xFF00574B;
    public static final int DEFAULT_THUMB_COLOR = 0xFFFDFDFD;


    private int switchOnColor;
    private int switchOffColor;
    private int switchOnStrokeColor;
    private int switchOffStrokeColor;

    /*
    Section of VIEW Size and Boundaries
     */
    //View Position on Screen
    public float viewStartX;
    public float viewStartY;
    public float viewEndX;
    public float viewEndY;

    public int widthQuarter;
    public QuarterOfWidth quarterOfWidth;
    public float minCenterX;
    public float maxCenterX;
    public float maxRadius;
    public float minRadius;
    public float minTopY;
    public float minBottomY;
    public float maxTopY;
    public float maxBottomY;

    /*
    My Thumb instance, we will use to draw itself
     */
    private MyThumb myThumb;


    /*
    Section of status, position and scale of position
     */
    //private boolean mIsChecked;
    public float currentPositionX;
    public float startPositionX;
    public float endPositionX;
    private static final float POS_X_START_SCALED = 0.0F;
    private static final float POS_X_END_SCALED = 1.0F;


    /*
    Section of Touch event and/or Interaction.
     */
    static final int TOUCH_MODE_IDLE = 0;
    static final int TOUCH_MODE_DOWN = 1;
    static final int TOUCH_MODE_DRAGGING = 2;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private int mMinFlingVelocity;

    //private static final int NOT_MOVING = 0;
    //private static final int MOVING_RIGHT = 1;
    //private static final int MOVINT_LEFT = -1;






    /*
    SECTION of Constructs
     */

    public TaskSwitchView(Context context) {

        super(context);

        this.context = context;

        switchOnColor = DEFAULT_SWITCH_ON_COLOR;
        switchOffColor = DEFAULT_SWITCH_OFF_COLOR;
        switchOnStrokeColor = DEFAULT_SWITCH_ON_STROKE_COLOR;
        switchOffStrokeColor = DEFAULT_SWITCH_OFF_STROKE_COLOR;

        setClickable(true);


    }

    public TaskSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TaskSwitchView);
        switchOnColor = a.getColor(R.styleable.TaskSwitchView_switchOnColor, DEFAULT_SWITCH_ON_COLOR);
        switchOffColor = a.getColor(R.styleable.TaskSwitchView_switchOffColor, DEFAULT_SWITCH_OFF_COLOR);
        switchOnStrokeColor = a.getColor(R.styleable.TaskSwitchView_switchOnStrokeColor, DEFAULT_SWITCH_ON_STROKE_COLOR);
        switchOffStrokeColor = a.getColor(R.styleable.TaskSwitchView_switchOffStrokeColor, DEFAULT_SWITCH_OFF_STROKE_COLOR);
        a.recycle();

        setClickable(true);

    }



    /*
    Utils method to calculate DP to Pixel
     */
    public int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float dp2pxFloat(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }


    /*
    Thumb need these method to calculate scaling
     */
    public float getPosXStartScaled(){
        return POS_X_START_SCALED;
    }

    public float getPosXEndScaled(){
        return POS_X_END_SCALED;
    }


    /*
    Callback method when the View get measured
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = dp2px(DEFAULT_WIDTH) + getPaddingLeft() + getPaddingRight();
        int height = dp2px(DEFAULT_HEIGHT) + getPaddingTop() + getPaddingBottom();

        if (widthSpecMode != MeasureSpec.AT_MOST) {
            width = Math.max(width, widthSpecSize);
        }

        if (heightSpecMode != MeasureSpec.AT_MOST) {
            height = Math.max(height, heightSpecSize);
        }

        setMeasuredDimension(width, height);
        setViewLocation(width, height);

    }

    /*
    Initial our View variable and boundery
     */
    private void setViewLocation(int width, int height){
        int[] viewStartPoint = new int[2];
        getLocationOnScreen(viewStartPoint);

        viewStartX = viewStartPoint[0];
        viewStartY = viewStartPoint[1];
        viewEndX = viewStartX + width;
        viewEndY = viewStartY + height;

        widthQuarter = width/4;
        quarterOfWidth = new QuarterOfWidth(widthQuarter);

        setupInitialCurrentPositionX();
        setupDrawnShapeBoundaries();
        setupMyThumb();


        //Set Touching Sloppy from system initial and commitment

        final ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop();


    }


    /*
    Set initial state
     */
    private void setupInitialCurrentPositionX(){
        if(!isChecked()){
            currentPositionX = 0;
        }
        else {
            currentPositionX = getWidth();
        }

        /*
        Crucial variable for launch animating purpose
         */
        startPositionX = 0;
        endPositionX = getWidth();

    }


    private void setupDrawnShapeBoundaries(){
        minCenterX = quarterOfWidth.firstQuarterCenter;
        maxCenterX = quarterOfWidth.fourthQuarterCenter;
        maxRadius = (float) ((widthQuarter-(widthQuarter*0.1))/2);
        minRadius = (float) (widthQuarter-(widthQuarter*0.8))/2;
        minTopY = (float) ((getHeight()/2)-5);
        minBottomY = (float) (float) ((getHeight()/2)+5);
        maxTopY = (float)(0+20);
        maxBottomY = (float)(0+getHeight()-20);

    }


    private void setupMyThumb(){

        myThumb = new MyThumb(this);
        myThumb.setPosition(currentPositionX);

    }


    /*
    Set Center (X) position of Thumb
    */
    private void setThumbPosition(float x){
        myThumb.setPosition(x);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        int pr = getPaddingRight();
        int pb = getPaddingBottom();
        int wp = w - pl - pr;
        int hp = h - pt - pb;
        int sw = dp2px(DEFAULT_WIDTH);
        int sh = dp2px(DEFAULT_HEIGHT);

        int dx = pl + (wp - sw) / 2;
        int dy = pt + (hp - sh) / 2;
        canvas.translate(dx, dy);




        if(currentPositionX < quarterOfWidth.secondQuarterEnd){
            drawBackground(canvas, switchOffColor);
        }
        else{
            drawBackground(canvas, switchOnColor);
        }

        myThumb.drawThumb(canvas);


    }

    private void drawBackground(Canvas canvas, int color) {
        int sw = dp2px(DEFAULT_WIDTH);
        int sh = dp2px(DEFAULT_HEIGHT);

        float left = dp2pxFloat((float) 2.4);
        float right = sw - left;
        float top = dp2pxFloat((float) 2.4);
        float bottom = sh - top;
        float radius = (bottom - top) * 0.5f;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(dp2pxFloat((float) 3.6));
        RectF rectF = new RectF();
        rectF.set(left, top, right, bottom);
        canvas.drawRoundRect(rectF, radius, radius, paint);

    }

    private boolean isTouchBoundView(float x){
        return ((x > 0) && (x < getWidth()));
    }

    @Override
    public boolean performClick() {
        toggle();

        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }

        return handled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        mVelocityTracker.addMovement(ev);

        final int action = ev.getActionMasked();
        switch (action) {

            //Action Down case
            case MotionEvent.ACTION_DOWN: {

                final float x = ev.getX();
                final float y = ev.getY();

                if (isEnabled() && myThumb.isHitThumb(x, y)) {
                    mTouchMode = TOUCH_MODE_DOWN;
                    mTouchX = x;
                    mTouchY = y;
                }
                break;
            }
            //Action Move Case -> Also interceptino between DOWN and DRAGGING
            case MotionEvent.ACTION_MOVE: {
                switch (mTouchMode) {
                    case TOUCH_MODE_IDLE:
                        // Didn't target the thumb, treat normally.
                        break;

                    case TOUCH_MODE_DOWN: {
                        final float x = ev.getX();
                        final float y = ev.getY();
                        if (Math.abs(x - mTouchX) > mTouchSlop ||
                                Math.abs(y - mTouchY) > mTouchSlop) {
                            mTouchMode = TOUCH_MODE_DRAGGING;
                            getParent().requestDisallowInterceptTouchEvent(true);
                            mTouchX = x;
                            mTouchY = y;
                            return true;
                        }
                        break;
                    }

                    case TOUCH_MODE_DRAGGING: {
                        mTouchMode = TOUCH_MODE_DRAGGING;

                        final float x = ev.getX();
                        if(isTouchBoundView(x)){
                            currentPositionX = x;
                            setThumbPosition(x);
                        }
                        return true;
                    }
                }
                break;
            }

            //Action UP and CANCEL Case
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mTouchMode == TOUCH_MODE_DRAGGING) {

                    //
                    stopDrag(ev);
                    //

                    // Allow super class to handle pressed state, etc.
                    super.onTouchEvent(ev);
                    return true;
                }
                mTouchMode = TOUCH_MODE_IDLE;
                mVelocityTracker.clear();
                break;
            }

        //End of Switch/Case
        }

        return super.onTouchEvent(ev);
    }


    private void stopDrag(MotionEvent ev) {


        mTouchMode = TOUCH_MODE_IDLE;

        // Commit the change if the event is up and not canceled and the switch
        // has not been disabled during the drag.

        final boolean commitChange = ev.getAction() == MotionEvent.ACTION_UP && isEnabled();

        currentPositionX = ev.getX();

        final boolean oldState = isChecked();
        final boolean newState;
        if (commitChange) {
           newState = findStateFromPosition(ev.getX());

        } else {
            newState = oldState;
        }

        // Always call setChecked so that the thumb is moved back to the correct edge
        setChecked(newState);
        cancelSuperTouch(ev);

    }


    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);

        // Calling the super method may result in setChecked() getting called
        // recursively with a different value, so load the REAL value...
        checked = isChecked();

        if (isAttachedToWindow() && isLaidOut()) {
            //If 'TRUE' animate to the switchOn (end of View)
            if(checked){

                while(currentPositionX < endPositionX){
                    setThumbPosition(currentPositionX);
                    currentPositionX++;
                }
            }
            //If 'FALSE' animate to switch Off (start of View)
            else{
                while(currentPositionX > startPositionX){
                    setThumbPosition(currentPositionX);
                    currentPositionX--;
                }
            }
        }

    }


    @Override
    public void toggle() {

    }


    private boolean findStateFromPosition(float x){

        if(x >= quarterOfWidth.secondQuarterEnd){
            return true;
        }
        else{
            return false;
        }

    }


    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }



}


