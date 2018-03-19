package com.myst3ry.moneytrackerxe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class DiagramView extends View {

    private long expenses;
    private long incomes;

    private final int space = 15;
    private final Paint expensesPaint = new Paint();
    private final Paint incomesPaint = new Paint();

    private float expAngle;
    private float incAngle;

    private int xMargin;
    private int yMargin;

    private long expensesHeight;
    private long incomesHeight;

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        expensesPaint.setColor(getResources().getColor(R.color.color_ginger));
        incomesPaint.setColor(getResources().getColor(R.color.color_golden));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawPieDiagram(canvas);
        } else {
            drawRectDiagram(canvas);
        }
    }


    public void update(long expenses, long incomes) {
        this.expenses = expenses;
        this.incomes = incomes;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void drawPieDiagram(Canvas canvas) {
        if (expenses + incomes == 0) {
            return;
        }

        int size = Math.min(getHeight(), getWidth()) - space * 2;
        expAngle = 360.f * expenses / (expenses + incomes);
        incAngle = 360.f * incomes / (expenses + incomes);

        xMargin = (getWidth() - size) / 2;
        yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space, getHeight() - yMargin, 180 - expAngle / 2, expAngle, true, expensesPaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space, getHeight() - yMargin, 360 - incAngle / 2, incAngle, true, incomesPaint);
    }

    public void drawRectDiagram(Canvas canvas) {
        if (expenses + incomes == 0) {
            return;
        }

        long max = Math.max(expenses, incomes);
        expensesHeight = getHeight() * expenses / max;
        incomesHeight = getHeight() * incomes / max;
        int rectWidth = getWidth() / 4;

        canvas.drawRect(rectWidth / 2, getHeight() - expensesHeight, rectWidth * 3 / 2, getHeight(), expensesPaint);
        canvas.drawRect(5 * rectWidth / 2, getHeight() - incomesHeight, rectWidth * 7 / 2, getHeight(), incomesPaint);
    }
}
